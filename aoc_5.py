
def load_vents(filename):
    file = open(filename, "r")
    try:
        vents = []
        for line in file.readlines():
            p = line.split('->')
            vents.append(Vents(parse_point(p[0]), parse_point(p[1])))
        return vents
    finally:
        file.close()

def parse_point(p):
    xy = p.split(',')
    return Point(int(xy[0]), int(xy[1]))

class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

class Vents:
    def __init__(self, p0, p1):
        self.p0 = p0
        self.p1 = p1

    def is_linear(self):
        return self.p0.x == self.p1.x or self.p0.y == self.p1.y

    def plot(self, grid):
        n = max(abs(self.p0.x - self.p1.x), abs(self.p0.y - self.p1.y))
        dx = int((self.p1.x - self.p0.x) / n)
        dy = int((self.p1.y - self.p0.y) / n)

        for i in range(0, n+1):
            x = self.p0.x + (dx * i)
            y = self.p0.y + (dy * i)
            grid[x][y] += 1

def create_grid(vents):
    xmax = 0
    ymax = 0
    for vent in vents:
        xmax = max(xmax, max(vent.p0.x, vent.p1.x))
        ymax = max(ymax, max(vent.p0.y, vent.p1.y))
    return [[0]*(xmax+1) for i in range(0, ymax+1)]

def count_intersections(grid):
    count = 0
    for row in grid:
        count = count + len(list(filter(lambda x: x >= 2, row)))
    return count

def part_a(filename):
    vents = load_vents(filename)
    grid = create_grid(vents)
    for vent in vents:
        if vent.is_linear():
            vent.plot(grid)
    return count_intersections(grid)

def part_b(filename):
    vents = load_vents(filename)
    grid = create_grid(vents)
    for vent in vents:
        vent.plot(grid)
    return count_intersections(grid)
