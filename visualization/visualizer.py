#from mpl_toolkits import mplot3d
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from dataparser import *
from matplotlib.ticker import AutoMinorLocator

simdata = data_parser("../data/output.txt")

if simdata.sim_type == "3D":
    def get_xyz(mat):
        x, y, z = [], [], []
        for i in range(len(mat)):
            for j in range(len(mat[i])):
                for k in range(len(mat[i])):
                    if mat[i][j][k] == 1:
                        x.append(i)
                        y.append(j)
                        z.append(k)
        return x, y, z

    ax = plt.gca(projection='3d')
    x, y, z = get_xyz(simdata.frames[0].mat)
    scat = plt.scatter(x, y, z)

    def update_func3d(frame, *fargs):
        global scat
        x, y, z = get_xyz(frame.mat)
        scat.remove()
        scat = plt.scatter(x, y, z)
        return scat
        
    ani = FuncAnimation(plt.gcf(), update_func3d,frames=simdata.frames, interval=500, repeat=True, repeat_delay=500, save_count=len(simdata.frames))
    
    plt.show()
else:
    mat = plt.matshow(simdata.frames[0].mat, extent=[0, simdata.sim_size[0], 0, simdata.sim_size[1]])

    def update_func2d(frame, *fargs):
        global mat
        mat.set_data(frame.mat)
        return mat
        
    ani = FuncAnimation(plt.gcf(), update_func2d, frames=simdata.frames, interval=500, repeat=True, repeat_delay=500, save_count=len(simdata.frames))

    ax = plt.gca()
    minor_ticks_x = range(1, simdata.sim_size[0], 1)
    minor_ticks_y = range(1, simdata.sim_size[1], 1)
    ax.set_xticks(minor_ticks_x, minor=True)
    ax.set_yticks(minor_ticks_y, minor=True)
    ax.grid(b=True, which='minor', color='gray', linestyle='-', linewidth=0.5)
    plt.show()
