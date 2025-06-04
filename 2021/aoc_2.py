from functools import reduce

def load_path(filename):
    file = open(filename, "r")
    try:
        lines = file.readlines()
        return [s.strip() for s in lines]
    finally:
        file.close()

def follow(result, step):
    posn = result["posn"]
    depth = result["depth"]
    parts = step.split()
    if parts[0] == "forward":
        posn += int(parts[1])
    elif parts[0] == "down":
        depth += int(parts[1])
    elif parts[0] == "up":
        depth -= int(parts[1])
    else:
        print(step)
    return {"posn": posn, "depth" : depth}

def follow_aim(result, step):
    posn = result["posn"]
    depth = result["depth"]
    aim = result["aim"]
    parts = step.split()
    if parts[0] == "forward":
        posn += int(parts[1])
        depth += int(parts[1]) * aim
    elif parts[0] == "down":
        aim += int(parts[1])
    elif parts[0] == "up":
        aim -= int(parts[1])
    else:
        print(step)
    return {"posn": posn, "depth" : depth, "aim": aim}

def part_a(filename):
    path = load_path(filename)
    state = {"posn": 0, "depth" : 0}
    dest = reduce(follow, path, state)
    return dest["posn"] * dest["depth"]

def part_b(filename):
    path = load_path(filename)
    state = {"posn": 0, "depth" : 0, "aim": 0}
    dest = reduce(follow_aim, path, state)
    return dest["posn"] * dest["depth"]
