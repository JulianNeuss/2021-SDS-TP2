import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from dataparser import *

simdata = data_parser("../data/output.txt")

grid = simdata.frames[0].mat

mat = plt.matshow(grid)

def update_func(frame, *fargs):
    mat.set_data(frame.mat)
    return mat
    
ani = FuncAnimation(plt.gcf(), update_func,frames=simdata.frames, interval=1000,repeat=True,repeat_delay=1000,save_count=len(simdata.frames))
plt.show()