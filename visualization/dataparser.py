class Frame:
    def __init__(self,time=0,mat=[],distance_from_border = 0):
        self.time = time
        self.mat = mat
        self.distance_from_border = distance_from_border
    def __repr__(self):
        return "{{time:{}, mat:\n{}\n}}".format(self.time,self.mat)


class SimulationData:

    def __init__(self):
        self.frames = []
        self._sim_type = None
        self._sim_size = []

    def sim_type(self):
        return self._sim_type

    def sim_type(self, t):
        self._sim_type = t

    def sim_size(self):
        return self._sim_size
    def sim_size(self, s):
        self._sim_size = s

    def add_frame(self,frame):
        self.frames.append(frame)

    def __repr__(self):
        return "{{sim_type:{},sim_size:{},\nframes:{}}}".format(
                self.sim_type,self.sim_size,self.frames
            )


def parse2d(f,simdata):
    mat = [[0 for x in range(simdata.sim_size[1])] for y in range(simdata.sim_size[0])]
    for i in range(simdata.sim_size[0]):
        line = f.readline().strip().split(" ")
        for j in range(simdata.sim_size[1]):
            mat[i][j] = int(line[j])
    return mat

def parse3d(f,simdata):
    mat = [[[0 for x in range(simdata.sim_size[2])] for y in range(simdata.sim_size[1])] for z in range(simdata.sim_size[0])]
    for i in range(simdata.sim_size[0]):
        for j in range(simdata.sim_size[1]):
            line = f.readline().strip().split(" ")
            for k in range(simdata.sim_size[2]):
                mat[i][j][k] = int(line[j])
        f.readline()
    return mat


def data_parser(filepath):
    TYPE_2D = "2D"
    TYPE_3D = "3D"

    f = open(filepath)
    simdata = SimulationData()
    simdata.sim_type = f.readline().strip()
    simdata.sim_size = list(map(int, f.readline().strip().split(" ")))
    
    finished=False
    while not finished:
        line = f.readline()
        while line == "\n":
            line = f.readline()
        if line == "":
            finished = True
            continue
        line = line.strip().split(" ")
        frame = Frame(time=int(line[0]), distance_from_border=float(line[1]))
        line = f.readline()
        if simdata.sim_type == TYPE_3D:
            mat = parse3d(f,simdata)
        else:
            mat = parse2d(f,simdata)
        frame.mat = mat
        simdata.add_frame(frame)
    return simdata