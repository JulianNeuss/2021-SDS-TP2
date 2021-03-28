#from mpl_toolkits import mplot3d
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from dataparser import *
from matplotlib.colors import ListedColormap
import numpy as np
import math

simdata = data_parser("../data/output.txt")

if simdata.sim_type == "3D":
    x,y,z = np.indices(map(lambda x:x+1,simdata.sim_size))
    colors = np.zeros((*simdata.sim_size,3)) 

    ax = plt.gca(projection='3d')
    ax.figure.set_size_inches((12, 12))
    ax.set_title("Time:{}".format(simdata.frames[0].time), fontdict={'fontsize': 20})
    vox = ax.voxels(x,y,z,np.array(simdata.frames[0].mat),facecolors=colors,edgecolor="black")
    def update_func3d(frame, *fargs):
        global ax
        global vox
        global x,y,z
        for k in vox:
            vox[k].remove()
        ax.set_title("Time:{}, Alive cells:{}".format(frame.time,frame.alive_cells), fontdict={'fontsize': 20})
        vox = ax.voxels(x,y,z,np.array(frame.mat),facecolors=colors,edgecolor="black")
        return ax.figure
        
    ani = FuncAnimation(ax.figure, update_func3d, frames=simdata.frames, interval=500, repeat=True, repeat_delay=500,
                        save_count=len(simdata.frames), fargs=simdata.sim_size, blit=False)
    
    plt.show()
else:
    mat = plt.matshow(simdata.frames[0].mat, cmap=ListedColormap(['w', 'k']))

    def update_func2d(frame, *fargs):
        global mat
        # TODO:check
        global ax
        ax.set_title("Time:{}, Alive cells:{}".format(frame.time,frame.alive_cells), fontdict={'fontsize': 20})
        mat.set_data(frame.mat)
        return mat
        
    ani = FuncAnimation(plt.gcf(), update_func2d, frames=simdata.frames, interval=500, repeat=True, repeat_delay=500,
                        save_count=len(simdata.frames))

    ax = plt.gca()
    ax.figure.set_size_inches((12, 12))
    minor_ticks_x = np.arange(0.5, simdata.sim_size[0] - 0.5, 1)
    minor_ticks_y = np.arange(0.5, simdata.sim_size[1] - 0.5, 1)
    ax.set_xticks(minor_ticks_x, minor=True)
    ax.set_yticks(minor_ticks_y, minor=True)
    ax.grid(b=True, which='minor', color='gray', linestyle='-', linewidth=0.5)

    for tic in ax.xaxis.get_major_ticks():
        tic.tick1On = tic.tick2On = False
    for tic in ax.yaxis.get_major_ticks():
        tic.tick1On = tic.tick2On = False

    plt.show()
