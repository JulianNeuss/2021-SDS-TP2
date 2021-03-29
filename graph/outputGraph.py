import matplotlib.pyplot as plt
import numpy as np
import os

FILE_PATH = '../data/test.txt'
DIR = '../graph/testResults/selected/2D'
SEPARATOR = ' '
TEST_TYPE_SEPARATOR = '\n'


class TestResult:
    def __init__(self, alive_qty, max_distance):
        self.alive_qty = alive_qty
        self.max_distance = max_distance

    def __repr__(self):
        return "{{alive_qty:{},max_distance:{}}}".format(self.alive_qty, self.max_distance)

rules = []
alive_quantities_list = []
max_distances_list = []
percentage_list = []
outputs = []
dimension = 0
total_rows = 0
total_columns = 0
total_depths = 0
initial_rows = 0
initial_columns = 0
initial_depths = 0


for subdir, dirs, files in os.walk(DIR):
    for file in files:
        filepath = subdir + os.sep + file

        alive_qty_list = []
        max_distance_list = []
        f = open(filepath, 'r')
        line_number = 0
        dimension = f.readline().strip()
        rule = f.readline().strip().split(SEPARATOR)
        total_sizes = f.readline().strip().split(SEPARATOR)
        total_rows = int(total_sizes[0])
        total_columns = int(total_sizes[1])
        if dimension == "3D":
            total_depths = int(total_sizes[2])
        initial_sizes = f.readline().strip().split(SEPARATOR)
        initial_rows = int(initial_sizes[0])
        initial_columns = int(initial_sizes[1])
        if dimension == "3D":
            initial_depths = int(initial_sizes[2])
        percentage_list = []
        for line in f.readlines():
            if line == "\n":
                line_number = 0
            elif line_number == 0:
                percentage_list.append(float(line))
                line_number += 1
            elif line_number == 1:
                iterationAnalytics = line.split(SEPARATOR)
                alive_qty_list.append([])
                max_distance_list.append([])
                alive_qty_list[len(alive_qty_list) - 1].append(float(iterationAnalytics[1]))
                max_distance_list[len(max_distance_list) - 1].append(float(iterationAnalytics[2]))
                line_number += 1
            else:
                iterationAnalytics = line.split(SEPARATOR)
                alive_qty_list[len(alive_qty_list) - 1].append(float(iterationAnalytics[1]))
                max_distance_list[len(max_distance_list) - 1].append(float(iterationAnalytics[2]))
                line_number += 1

        output = []
        for i, percentage in enumerate(percentage_list):
            accum = 0
            for iteration, alive_qty in enumerate(alive_qty_list[i]):
                accum += iteration * alive_qty
            output.append(accum)
        rules.append(rule)
        alive_quantities_list.append(alive_qty_list)
        max_distances_list.append(max_distance_list)
        outputs.append(output)

percentage_labels = [str(i*100) + "%" for i in percentage_list]

x = np.arange(len(percentage_labels))  # the label locations
width = 0.1  # the width of the bars
bars = []
fig, ax = plt.subplots()
for i in range(len(outputs)//2):
    bars.append(ax.bar(x - (i+1)*width, outputs[i], width, log=False, label='{}/{}'.format(rules[i][0], rules[i][1])))

if len(outputs) % 2 != 0:
    bars.append(ax.bar(x, outputs[len(outputs)//2], width, log=False, label='{}/{}'.format(rules[len(outputs)//2][0],
                                                                              rules[len(outputs)//2][1])))
for i in range(len(outputs)//2 + 1, len(outputs)):
    bars.append(ax.bar(x + (i-len(outputs)//2)*width, outputs[i], width, log=False, label='{}/{}'.format(rules[i][0], rules[i][1])))

# Add some text for labels, title and custom x-axis tick labels, etc.
ax.set_ylabel('Output')
ax.set_xlabel('Porcentaje inicial')
if(dimension == "2D"):
    plt.title("Output en función del input\n"
              "{} - Espacio inicial = {}x{} - Espacio Total = {}x{}".format(dimension, initial_rows, initial_columns, total_rows, total_columns))
else:
    plt.title("Output en función del input\n"
              "{} - Espacio inicial = {}x{}x{} - Espacio Total = {}x{}x{}".format(dimension, initial_rows, initial_columns, initial_depths, total_rows, total_columns, total_depths))
ax.set_xticks(x)
ax.set_xticklabels(percentage_labels)

rects = ax.patches
labels = []
for i in outputs:
    for j in i:
        labels.append(int(j))

for rect, label in zip(rects, labels):
    height = rect.get_height()
    ax.text(rect.get_x() + rect.get_width() / 2, height + 5, label, ha='center', va='bottom')

ax.legend()
fig.tight_layout()

plt.show()
