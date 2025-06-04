
def calc_dimensions(grid):
    xdim = len(grid[0])
    ydim = len(grid)
    return [xdim, ydim]


def load_grid(filename):
    file = open(filename, "r")
    try:
        grid = [[int(x) for x in line.strip()] for line in file.readlines()]
        return grid
    finally:
        file.close()

class ShortestPaths:

    def __init__(self, grid):
        self.grid = grid
        dim = calc_dimensions(grid)
        self.xdim = dim[0]
        self.ydim = dim[1]

        self.costs = [[9999999999] * self.xdim for i in range(0, self.ydim)]
        self.prev = [[-1] * self.xdim for i in range(0, self.ydim)]


    def calc_shortest_paths(self):
        frontier = [(0, 0)]
        self.costs[0][0] = 0

        while len(frontier) > 0:
            current_frontier = frontier
            frontier = []

            for node in current_frontier:
                x0 = node[0]
                y0 = node[1]

                node_path_cost = self.costs[y0][x0]

                neighbours = [(-1,0), (1,0), (0,-1), (0,1)]
                for n in neighbours:
                    x = x0 + n[0]
                    y = y0 + n[1]
                    if self.visit_neighbour(node, node_path_cost, x, y):
                        frontier.append((x, y))

    def visit_neighbour(self, node, node_path_cost, x, y):
        if x < 0 or x >= self.xdim or y < 0 or y >= self.ydim:
            return False

        neighbour_cost = self.grid[y][x]
        neighbour_path_cost = self.costs[y][x]
        new_path_cost = node_path_cost + neighbour_cost

        if new_path_cost < neighbour_path_cost:
            self.costs[y][x] = new_path_cost
            self.prev[y][x] = node
            return True

        return False


def expand_grid(grid):
    xdim = len(grid[0])
    ydim = len(grid)

    big_grid = [[0] * (xdim*5) for i in range(0, ydim*5)]
    for i in range(0, 5):
        x0 = xdim*i
        for x in range(0, xdim):
            for y in range(0, ydim):
                new_risk = grid[y][x] + i
                while new_risk >= 10:
                    new_risk -= 9
                big_grid[y][x0 + x] = new_risk

    for i in range(1, 5):
        y0 = ydim * i
        for x in range(0, xdim*5):
            for y in range(0, ydim):
                new_risk = big_grid[y][x] + i
                while new_risk >= 10:
                    new_risk -= 9
                big_grid[y0 + y][x] = new_risk

    return big_grid

def part_a(filename):
    grid = load_grid(filename)
    paths = ShortestPaths(grid)
    paths.calc_shortest_paths()
    return paths.costs[paths.xdim-1][paths.ydim-1]


def part_b(filename):
    grid = load_grid(filename)
    expanded_grid = expand_grid(grid)
    paths = ShortestPaths(expanded_grid)
    paths.calc_shortest_paths()
    return paths.costs[paths.xdim-1][paths.ydim-1]