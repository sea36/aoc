
def load_cavern(filename):
    file = open(filename, "r")
    try:
        lines = [line.strip() for line in file.readlines()]
        return Cavern([(lambda x: [int(i) for i in x])(line) for  line in lines])
    finally:
        file.close()

class Cavern:

    def __init__(self, grid):
        self.grid = grid
        self.step_count = 0
        self.flash_count = 0
        self.sync_count = 0

    def step(self):
        self.step_count += 1
        self.sync_count = 0
        for i in range(0, 10):
            for j in range(0, 10):
                self.inc(i, j)
        self.reset()

    def inc(self, i, j):
        # out of range
        if i < 0 or i > 9 or j < 0 or j > 9:
            return

        # already flashed this step
        if self.grid[i][j] > 9:
            return

        self.grid[i][j] += 1
        if self.grid[i][j] > 9:
            self.flash(i, j)

    def flash(self, i, j):
        self.flash_count += 1
        self.sync_count += 1
        for k in range(-1, 2):
            for l in range(-1, 2):
                if not (k == 0 and l == 0):
                    self.inc(i + k, j + l)

    def reset(self):
        for i in range(0, 10):
            for j in range(0, 10):
                if self.grid[i][j] > 9:
                    self.grid[i][j] = 0




def count_flashes(filename, steps):
    cavern = load_cavern(filename)
    for i in range(0, steps):
        cavern.step()
    return cavern.flash_count

def find_sync_step(filename):
    cavern = load_cavern(filename)
    while cavern.sync_count != 100:
        cavern.step()
    return cavern.step_count