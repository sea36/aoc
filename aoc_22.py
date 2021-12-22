import re

class Action:
    def __init__(self, on, cube):
        self.on = on
        self.cube = cube


def parse_line(line):
    m = re.search("(on|off) x=([-\d]+)..([-\d]+),y=([-\d]+)..([-\d]+),z=([-\d]+)..([-\d]+)", line)
    if m:
        return (m.group(1) == 'on', (int(m.group(2)), int(m.group(3))), (int(m.group(4)), int(m.group(5))),
                (int(m.group(6)), int(m.group(7))))
    else:
        print('Parse fail:', line)
        return None


def load_actions(filename):
    with open(filename, 'r') as file:
        actions = []
        for line in file.readlines():
            (on, x, y, z) = parse_line(line)
            actions.append(Action(on, (x, y, z)))
        return actions


def in_range(x):
    return x >= -50 and x <= 50


def filter_action(action):
    ((x0, x1), (y0, y1), (z0, z1)) = action.cube
    return all([in_range(x) for x in [x0, x1, y0, y1, z0, z1]])


def find_intersection(a, b):
    ((ax0, ax1), (ay0, ay1), (az0, az1)) = a
    ((bx0, bx1), (by0, by1), (bz0, bz1)) = b

    x0 = max(ax0, bx0)
    x1 = min(ax1, bx1)
    y0 = max(ay0, by0)
    y1 = min(ay1, by1)
    z0 = max(az0, bz0)
    z1 = min(az1, bz1)

    result = ((x0, x1), (y0, y1), (z0, z1))
    return result if is_valid_cube(result) else None


def is_valid_cube(a):
    ((x0, x1), (y0, y1), (z0, z1)) = a
    return x1 >= x0 and y1 >= y0 and z1 >= z0


def split_cube(cube, intersection):
    ((ax0, ax1), (ay0, ay1), (az0, az1)) = cube
    ((bx0, bx1), (by0, by1), (bz0, bz1)) = intersection

    # Think of a 3x3x3 rubiks cube
    # Intersection will be one segment
    # Depending on positioning, some segments may be sized 0 - so discard later

    candidates = [
        ((ax0, bx0 - 1), (ay0, by0 - 1), (az0, bz0 - 1)),
        ((bx0, bx1),     (ay0, by0 - 1), (az0, bz0 - 1)),
        ((bx1 + 1, ax1), (ay0, by0 - 1), (az0, bz0 - 1)),
        ((ax0, bx0 - 1), (by0, by1),     (az0, bz0 - 1)),
        ((bx0, bx1),     (by0, by1),     (az0, bz0 - 1)),
        ((bx1 + 1, ax1), (by0, by1),     (az0, bz0 - 1)),
        ((ax0, bx0 - 1), (by1 + 1, ay1), (az0, bz0 - 1)),
        ((bx0, bx1),     (by1 + 1, ay1), (az0, bz0 - 1)),
        ((bx1 + 1, ax1), (by1 + 1, ay1), (az0, bz0 - 1)),

        ((ax0, bx0 - 1), (ay0, by0 - 1), (bz0, bz1)),
        ((bx0, bx1),     (ay0, by0 - 1), (bz0, bz1)),
        ((bx1 + 1, ax1), (ay0, by0 - 1), (bz0, bz1)),
        ((ax0, bx0 - 1), (by0, by1),     (bz0, bz1)),
        ((bx0, bx1),     (by0, by1),     (bz0, bz1)),
        ((bx1 + 1, ax1), (by0, by1),     (bz0, bz1)),
        ((ax0, bx0 - 1), (by1 + 1, ay1), (bz0, bz1)),
        ((bx0, bx1),     (by1 + 1, ay1), (bz0, bz1)),
        ((bx1 + 1, ax1), (by1 + 1, ay1), (bz0, bz1)),

        ((ax0, bx0 - 1), (ay0, by0 - 1), (bz1 + 1, az1)),
        ((bx0, bx1),     (ay0, by0 - 1), (bz1 + 1, az1)),
        ((bx1 + 1, ax1), (ay0, by0 - 1), (bz1 + 1, az1)),
        ((ax0, bx0 - 1), (by0, by1),     (bz1 + 1, az1)),
        ((bx0, bx1),     (by0, by1),     (bz1 + 1, az1)),
        ((bx1 + 1, ax1), (by0, by1),     (bz1 + 1, az1)),
        ((ax0, bx0 - 1), (by1 + 1, ay1), (bz1 + 1, az1)),
        ((bx0, bx1),     (by1 + 1, ay1), (bz1 + 1, az1)),
        ((bx1 + 1, ax1), (by1 + 1, ay1), (bz1 + 1, az1)),
    ]

    return list(filter(lambda c: is_valid_cube(c), candidates))


def calculate_core(actions):
    core = set()
    for action in actions:

        for part in list(core):
            intersection = find_intersection(action.cube, part)
            if intersection is not None:
                core.discard(part)
                for fragment in split_cube(part, intersection):
                    if find_intersection(action.cube, fragment) is None:
                        core.add(fragment)

        if action.on:
            core.add(action.cube)
    return core


def calc_cube_volume(a):
    ((x0, x1), (y0, y1), (z0, z1)) = a
    return (x1 - x0 + 1) * (y1 - y0 + 1) * (z1 - z0 + 1)


def calculate_core_volume(core):
    return sum([calc_cube_volume(x) for x in core])


def generate_core(filename, initialization=False):
    actions = load_actions(filename)
    actions = list(filter(lambda x: filter_action(x), actions)) if initialization else actions
    core = calculate_core(actions)
    return calculate_core_volume(core)

