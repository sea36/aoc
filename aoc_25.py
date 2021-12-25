
def load_region(filename):
    with open(filename, 'r') as file:
        return [[x for x in line.strip()] for line in  file.readlines()]

def move_east(region):
    moved = False
    xmax  = len(region[0])
    ymax = len(region)

    for y in range(ymax):
        moves = []
        for x in range(xmax):
            if region[y][x] == '>':
                x1 = (x + 1) % xmax
                if region[y][x1] == '.':
                    moves.append((x, x1))
        for (x0, x1) in moves:
            region[y][x0] = '.'
            region[y][x1] = '>'
            moved = True
    return moved

def move_south(region):
    moved = False
    xmax  = len(region[0])
    ymax = len(region)

    for x in range(xmax):
        moves = []
        for y in range(ymax):
            if region[y][x] == 'v':
                y1 = (y + 1) % ymax
                if region[y1][x] == '.':
                    moves.append((y, y1))
        for (y0, y1) in moves:
            region[y0][x] = '.'
            region[y1][x] = 'v'
            moved = True
    return moved


def move_cucumbers(region):
    moved = move_east(region)
    moved |= move_south(region)
    return moved

def find_first_step_with_no_moves(region):

    steps = 0
    while move_cucumbers(region):
        steps += 1

    return steps + 1

def part_a(filename):
    region = load_region(filename)
    return find_first_step_with_no_moves(region)

