import matplotlib.pyplot as plt
import numpy as np

FILE_PATH = '../data/test2d.txt'
SEPARATOR = ' '
TEST_TYPE_SEPARATOR = '\n'


class TestResult:
    def __init__(self, alive_qty, max_distance):
        self.alive_qty = alive_qty
        self.max_distance = max_distance

    def __repr__(self):
        return "{{alive_qty:{},max_distance:{}}}".format(self.alive_qty, self.max_distance)


alive_qty_list = []
max_distance_list = []
percentage_list = []
f = open(FILE_PATH, 'r')
line_number = 0
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
        alive_qty_list[len(percentage_list) - 1].append(float(iterationAnalytics[1]))
        max_distance_list[len(percentage_list) - 1].append(float(iterationAnalytics[2]))
        line_number += 1
    else:
        iterationAnalytics = line.split(SEPARATOR)
        print(iterationAnalytics)
        alive_qty_list[len(percentage_list) - 1].append(float(iterationAnalytics[1]))
        max_distance_list[len(percentage_list) - 1].append(float(iterationAnalytics[2]))
        line_number += 1


plt.rcParams["figure.figsize"] = (10, 10)
plt.rcParams.update({'font.size': 16})
plt.figure()
plt.title("Celdas vivas a lo largo del tiempo \n"
          "según el porcentaje de celdas vivas en la condicion inicial")
plt.xlabel("Iteración")
plt.ylabel("Cantidad de celdas")
for i in range(0, len(alive_qty_list)):
    times = range(0, len(alive_qty_list[i]))
    plt.plot(times, alive_qty_list[i], label="{:.3f}%".format(percentage_list[i]))
plt.legend(loc='best')
plt.show(block=False)

plt.figure()
plt.title("Distancia máxima del centro a lo largo del tiempo \n"
          "según el porcentaje de celdas vivas en la condicion inicial")
plt.xlabel("Iteración")
plt.ylabel("Distancia")
for i in range(0, len(max_distance_list)):
    times = range(0, len(max_distance_list[i]))
    plt.plot(times, max_distance_list[i], label="{:.3f}%".format(percentage_list[i]))
plt.legend(loc='best')
plt.show()
