#from mpl_toolkits import mplot3d
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from dataparser import *
from matplotlib.colors import ListedColormap
import numpy as np
import math

simdata = data_parser("../data/output.txt")

if simdata.sim_type == "3D":
    def get_xyz(space, sim_size):
        sim_center = tuple(map(lambda total_size: total_size/2, sim_size))
        x_coords, y_coords, z_coords, distances = [], [], [], []
        for x_coord in range(len(space)):
            for y_coord in range(len(space[x_coord])):
                for z_coord in range(len(space[x_coord][y_coord])):
                    if space[x_coord][y_coord][z_coord] == 1:
                        x_coords.append(x_coord)
                        y_coords.append(y_coord)
                        z_coords.append(z_coord)
                        distances.append(math.dist(sim_center, (x_coord, y_coord, z_coord)))
        return x_coords, y_coords, z_coords, distances
    
    ax = plt.gca(projection='3d')
    ax.figure.set_size_inches((12, 12))
    ax.set(xlim=(0, simdata.sim_size[0]), ylim=(0, simdata.sim_size[1]), zlim=(0, simdata.sim_size[2]))
    x, y, z, dists = get_xyz(simdata.frames[0].mat, simdata.sim_size)
    ax.set_title("Time:{}".format(simdata.frames[0].time), fontdict={'fontsize': 20})
    scat = ax.scatter(x, y, z, c=dists)

    def update_func3d(frame, *fargs):
        global scat
        global ax
        ax.set_title("Time:{}, Alive cells:{}".format(frame.time,frame.alive_cells), fontdict={'fontsize': 20})
        x_coords, y_coords, z_coords, distances = get_xyz(frame.mat, fargs)
        scat.remove()
        scat = ax.scatter(x_coords, y_coords, z_coords, c=distances)
        return scat
        
    ani = FuncAnimation(ax.figure, update_func3d, frames=simdata.frames, interval=500, repeat=True, repeat_delay=500,
                        save_count=len(simdata.frames), fargs=simdata.sim_size)
    
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
