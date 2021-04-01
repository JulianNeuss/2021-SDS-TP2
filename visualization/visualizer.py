#from mpl_toolkits import mplot3d
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from dataparser import *
from matplotlib.colors import ListedColormap
import numpy as np
import math
import matplotlib.colors as mptcolors
from matplotlib import cm
import os
import argparse

DATA_PATH = "../data/output.txt"
ANIMATIONS_PATH = "animations"
ANIMATIONS_FILENAME = "animation"
save_video = True

argp = argparse.ArgumentParser(description="Game of life visualizer")
argp.add_argument('--output-file-path', dest='output_path', help='path to output of sim')
argp.add_argument('--save-video', dest='save_video', help='save video:True or False')
argp.add_argument('--animations-path', dest='animation_path', help='animations path')
argp.add_argument('--filename', dest='filename', help='animations filename')

args = argp.parse_args()

if args.output_path:
    DATA_PATH = args.output_path

if args.save_video:
    save_video = args.save_video.lower() in ['true', '1']
if args.animation_path:
    ANIMATIONS_PATH = args.animation_path

if args.filename:
    ANIMATIONS_FILENAME = args.filename

print("Parsing data")
simdata = data_parser(DATA_PATH)
print("Data parsed")

def print_progress_helper(frame,total,save_type):
    aux = round(total/20)
    if frame%aux == 0:
        print("Progress saving {} file: {} of {} frames ({}%)".format(save_type,frame,total,math.floor((frame/total) * 100)))

if simdata.sim_type == "3D":
    x,y,z = np.indices(map(lambda x:x+1,simdata.sim_size))
    MAX_DIST = math.dist((0,0,0),(simdata.sim_size[0]/2,simdata.sim_size[1]/2,simdata.sim_size[2]/2))
    COLORMAPPER = cm.get_cmap('magma',simdata.sim_size[0])
    colors = []
    CENTER = (simdata.sim_size[0]/2,simdata.sim_size[1]/2,simdata.sim_size[2]/2)
    print("Calculating color grid")
    for i in range(simdata.sim_size[0]):
        colors.append([])
        for j in range(simdata.sim_size[1]):
            colors[i].append([])
            for k in range(simdata.sim_size[2]):
                colors[i][j].append(COLORMAPPER(
                    math.dist(
                        CENTER,
                        (i,j,k)
                    )/MAX_DIST
                ))
    print("Color grid calculated")
    colors = np.array(colors)

    ax = plt.gca(projection='3d')
    ax.figure.set_size_inches((12, 12))
    ax.set_title("Time:{}, Alive cells:{}".format(simdata.frames[0].time,simdata.frames[0].alive_cells), fontdict={'fontsize': 20})
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
                        save_count=len(simdata.frames), fargs=simdata.sim_size)
    
    if save_video:
        if not os.path.exists(ANIMATIONS_PATH):
            os.makedirs(ANIMATIONS_PATH)
        print("Saving videos")
        ani.save(os.path.join(ANIMATIONS_PATH,ANIMATIONS_FILENAME + ".avi"),progress_callback=lambda f,t:print_progress_helper(f,t,"avi"))
        ani.save(os.path.join(ANIMATIONS_PATH,ANIMATIONS_FILENAME + ".gif"),progress_callback=lambda f,t:print_progress_helper(f,t,"gif"))
        print("Videos saved")
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
    ax.set_title("Time:{}, Alive cells:{}".format(simdata.frames[0].time,simdata.frames[0].alive_cells), fontdict={'fontsize': 20})
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
    if save_video:
        if not os.path.exists(ANIMATIONS_PATH):
            os.makedirs(ANIMATIONS_PATH)
        print("Saving videos")
        ani.save(os.path.join(ANIMATIONS_PATH,ANIMATIONS_FILENAME + ".avi"),progress_callback=lambda f,t:print_progress_helper(f,t,"avi"))
        ani.save(os.path.join(ANIMATIONS_PATH,ANIMATIONS_FILENAME + ".gif"),progress_callback=lambda f,t:print_progress_helper(f,t,"gif"))
        print("Videos saved")
    plt.show()
