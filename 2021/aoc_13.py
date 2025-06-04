
def calc_dimensions(lines):
    xmax = 0
    ymax = 0
    for line in lines:
        if ',' in line:
            xy = line.split(',')
            xmax = max(xmax, int(xy[0]))
            ymax = max(ymax, int(xy[1]))
    return [xmax, ymax]

def load_paper(filename):
    file = open(filename, "r")
    lines = file.readlines()

    [xmax, ymax] = calc_dimensions(lines)
    grid = [[0] * (ymax+1) for i in range(0,xmax+1)]
    for line in lines:
        if ',' in line:
            xy = line.split(',')
            grid[int(xy[0])][int(xy[1])] = 1

    folds = list(filter(lambda line: "fold along" in line, lines))
    return Paper(grid, folds)


class Paper:

    def __init__(self, grid, folds):
        self.grid = grid
        self.folds = folds
        self.xmax = len(grid)
        self.ymax = len(grid[0])

    def xfold(self, xfold):
        for x in range(xfold+1,self.xmax):
            for y in range(0,self.ymax):
                x1 = xfold - (x-xfold)
                self.grid[x1][y] = self.grid[x1][y] | self.grid[x][y]
        self.xmax = xfold

    def yfold(self, yfold):
        for y in range(yfold+1,self.ymax):
             for x in range(0,self.xmax):
                 y1 = yfold - (y-yfold)
                 self.grid[x][y1] = self.grid[x][y1] | self.grid[x][y]
        self.ymax = yfold

    def print(self):
        for y in range(0, self.ymax):
            line = ["."] * self.xmax
            for x in range(0, self.xmax):
                if self.grid[x][y] == 1:
                    line[x] = '#'
            print("".join(line))

    def fold(self, fold):
        if "fold along x=" in fold:
            self.xfold(int(fold.split("=")[1]))
        elif "fold along y=" in fold:
            self.yfold(int(fold.split("=")[1]))

    def fold_all(self):
        for fold in self.folds:
            self.fold(fold)

    def count(self):
        count = 0
        for x in range(0, self.xmax):
            for y in range(0, self.ymax):
                if self.grid[x][y] == 1:
                    count += 1
        return count

def part_a(filename):
    paper = load_paper(filename)
    paper.fold(paper.folds[0])
    return paper.count()

def part_b(filename):
    paper = load_paper(filename)
    paper.fold_all()
    paper.print()
    return paper.count()
