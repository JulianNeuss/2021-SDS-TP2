import matplotlib.pyplot as plt
import numpy as np

FILE_PATH = '../data/test.txt'
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
dimension = f.readline().strip()
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
        alive_qty_list[len(percentage_list) - 1].append(float(iterationAnalytics[1]))
        max_distance_list[len(percentage_list) - 1].append(float(iterationAnalytics[2]))
        line_number += 1


plt.rcParams["figure.figsize"] = (10, 10)
plt.rcParams.update({'font.size': 12})
plt.figure()
if(dimension == "2D"):
    plt.title("Celdas vivas a lo largo del tiempo según el porcentaje de celdas vivas en la condicion inicial\n"
              "{} - Espacio inicial = {}x{} - Espacio Total = {}x{}".format(dimension, initial_rows, initial_columns, total_rows, total_columns))
else:
    plt.title("Celdas vivas a lo largo del tiempo según el porcentaje de celdas vivas en la condicion inicial\n"
              "{} - Espacio inicial = {}x{}x{} - Espacio Total = {}x{}x{}".format(dimension, initial_rows, initial_columns, initial_depths, total_rows, total_columns, total_depths))
plt.xlabel("Tiempo")
plt.ylabel("Cantidad de celdas vivas")
for i in range(0, len(alive_qty_list)):
    times = range(0, len(alive_qty_list[i]))
    plt.plot(times, alive_qty_list[i], label="{:.2f}%".format(percentage_list[i] * 100))
plt.legend(loc='best')
plt.show(block=False)

plt.figure()
if(dimension == "2D"):
    plt.title("Distancia máxima del centro a lo largo del tiempo según el porcentaje de celdas vivas en la condicion inicial\n"
              "{} - Espacio inicial = {}x{} - Espacio Total = {}x{}".format(dimension, initial_rows, initial_columns, total_rows, total_columns))
else:
    plt.title("Distancia máxima del centro a lo largo del tiempo según el porcentaje de celdas vivas en la condicion inicial\n"
              "{} - Espacio inicial = {}x{}x{} - Espacio Total = {}x{}x{}".format(dimension, initial_rows, initial_columns, initial_depths, total_rows, total_columns, total_depths))

plt.xlabel("Tiempo")
plt.ylabel("Distancia máxima del centro")
for i in range(0, len(max_distance_list)):
    times = range(0, len(max_distance_list[i]))
    plt.plot(times, max_distance_list[i], label="{:.2f}%".format(percentage_list[i] * 100))
plt.legend(loc='best')
plt.show()
