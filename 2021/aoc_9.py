def load_grid(filename):
    file = open(filename, "r")
    try:
        lines = [line.strip() for line in file.readlines()]
        grid = [(lambda x: [int(i) for i in x])(line) for  line in lines]
        return Caves(grid)
    finally:
        file.close()

class Point(object):
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __eq__(self, o: object) -> bool:
        return (self.x == o.x and self.y == o.y) if isinstance(o, Point) else False

    def __ne__(self, o: object) -> bool:
        return not self.__eq__(o)

class Caves:

    def __init__(self, grid):
        self.grid = grid
        self.xmax = len(grid[0])
        self.ymax = len(grid)

    def height(self, x, y):
        if x >= 0 and x < self.xmax and y >= 0 and y < self.ymax:
            return self.grid[y][x]
        return 9999999999

    def risk_level(self, x, y):
        return self.height(x, y) + 1

    def is_lowpoint(self, x, y):
        h = self.height(x, y)
        h0 = self.height(x-1, y)
        h1 = self.height(x+1, y)
        h2 = self.height(x, y-1)
        h3 = self.height(x, y+1)
        return h < h0 and h < h1 and h < h2 and h < h3

    def find_low_points(self):
        low_points = []
        for x in range(0, self.xmax):
            for y in range(0, self.ymax):
                if self.is_lowpoint(x, y):
                    low_points.append(Point(x,y))
        return low_points

    def calculate_risk_levels(self):
        low_points = self.find_low_points()
        risk_levels = [self.risk_level(p.x, p.y) for p in low_points]
        return sum(risk_levels)

    def map_point(self, x, y, h, basin_points):
        h0 = self.height(x, y)
        if (h0 > h and h0 < 9):
            self.map_points(x, y, basin_points)

    def map_points(self, x, y, basin_points):
        p = (x,y)
        if not p in basin_points:
            basin_points.add(p)
            h = self.height(x, y)
            self.map_point(x-1, y, 0, basin_points)
            self.map_point(x+1, y, 0, basin_points)
            self.map_point(x, y-1, 0, basin_points)
            self.map_point(x, y+1, 0, basin_points)

    def map_basin(self, x, y):
        basin_points = set()
        self.map_points(x, y, basin_points)
        return basin_points

    def basin_size(self, x, y):
        basin_points = self.map_basin(x, y)
        return len(basin_points)

    def calculate_basin_sizes(self):
        low_points = self.find_low_points()
        return [self.basin_size(p.x, p.y) for p in low_points]

def part_a(filename):
    grid = load_grid(filename)
    return grid.calculate_risk_levels()

def part_b(filename):
    grid = load_grid(filename)
    basins = grid.calculate_basin_sizes()
    basins.sort(reverse=True)
    return basins[0] * basins[1] * basins[2]
