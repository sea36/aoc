from functools import reduce

def load_depths(filename):
    file = open(filename, "r")
    try:
        lines = file.readlines()
        return [int(s) for s in lines]
    finally:
        file.close()

def accumulate_deeper(result, next):
    count = result["count"]
    if next > result["last_depth"]:
        count = count+1
    return {"last_depth": next, "count": count}

def accumulate_deeper_window(result, next):
    d0 = result["d0"]
    d1 = result["d1"]
    d2 = result["d2"]
    last_window = d0 + d1 + d2
    this_window = d1 + d2 + next
    count = result["count"]
    if this_window > last_window:
        count = count+1
    return {"d0" : d1, "d1": d2, "d2": next, "count": count}

def part_a(filename):
    depths = load_depths(filename)
    state = {"last_depth": depths[0], "count": 0}
    result = reduce(accumulate_deeper, depths, state)
    return result["count"]

def part_b(filename):
    depths = load_depths(filename)
    state = {"d0" : depths[0], "d1": depths[1], "d2": depths[2], "count": 0}
    result = reduce(accumulate_deeper_window, depths, state)
    return result["count"]
