
def load_positions(filename):
    file = open(filename, 'r')
    try:
        line = file.readline()
        return [int(x) for x in line.split(",")]
    finally:
        file.close()

def cost_a(target, positions):
    return sum([abs(target - p) for p in positions])

def triangular(x):
    return (x * (x+1)) / 2

def cost_b(target, positions):
    return sum([triangular(abs(target - p)) for p in positions])

def part_a(filename):
    positions = load_positions(filename)
    xmax = max(positions)
    costs = [cost_a(i, positions) for i in range(0, xmax)]
    return min(costs)

def part_b(filename):
    positions = load_positions(filename)
    xmax = max(positions)
    costs = [cost_b(i, positions) for i in range(0, xmax)]
    return min(costs)
