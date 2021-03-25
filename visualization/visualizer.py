#from mpl_toolkits import mplot3d
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from dataparser import *
from matplotlib.colors import ListedColormap
import numpy as np
import math

simdata = data_parser("../data/output.txt")

if simdata.sim_type == "3D":
    def get_xyz(mat,sim_size):
        sim_center = tuple(map(lambda x:x/2,sim_size))
        x, y, z, dists = [], [], [], []
        for i in range(len(mat)):
            for j in range(len(mat[i])):
                for k in range(len(mat[i])):
                    if mat[i][j][k] == 1:
                        x.append(i)
                        y.append(j)
                        z.append(k)
                        dists.append(math.dist(sim_center,(i,j,k)))
        return x, y, z, dists
    
    ax = plt.gca(projection='3d')
    ax.figure.set_size_inches((12, 12))
    ax.set(xlim=(0,simdata.sim_size[0]),ylim=(0,simdata.sim_size[1]),zlim=(0,simdata.sim_size[2]))
    x, y, z, dists = get_xyz(simdata.frames[0].mat,simdata.sim_size)
    scat = ax.scatter(x, y, z)
    def update_func3d(frame, *fargs):
        global scat
        x, y, z,dists = get_xyz(frame.mat,fargs)
        scat.remove()
        scat = ax.scatter(x, y, z,c=dists)
        return scat
        
    ani = FuncAnimation(ax.figure, update_func3d,frames=simdata.frames, interval=500, repeat=True, repeat_delay=500, save_count=len(simdata.frames), fargs=simdata.sim_size)
    
    plt.show()
else:
    mat = plt.matshow(simdata.frames[0].mat, cmap=ListedColormap(['w', 'k']))

    def update_func2d(frame, *fargs):
        global mat
        # TODO:check
        global ax
        ax.set_title("Time:{}".format(frame.time),fontdict={'fontsize':20})
        mat.set_data(frame.mat)
        return mat
        
    ani = FuncAnimation(plt.gcf(), update_func2d, frames=simdata.frames, interval=500, repeat=True, repeat_delay=500, save_count=len(simdata.frames))

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
