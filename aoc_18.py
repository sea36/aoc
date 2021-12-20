from math import floor,ceil

def add(left, right):
    return right if left is None else [left, right]

def split(number, outer, position):
    outer[position] = [floor(number/2), ceil(number/2)]
    return outer

def explode_left(path, x):
    for prev in path:
        prev_pair = prev[0]
        prev_pos = prev[1]

        if isinstance(prev_pair[prev_pos], int):
            prev_pair[prev_pos] += x
            return

def explode_right(outer, position, stack, y):

    if position == 0:
        if do_explode_right(outer, 1, y):
            return

    for prev in stack:
        prev_pair = prev[0]
        prev_pos = prev[1]
        if prev_pos == 0:
            if do_explode_right(prev_pair, 1, y):
                return

def do_explode_right(pair, pos, y):
    for i in range(pos, 2):
        if isinstance(pair[i], int):
            pair[i] += y
            return True
        if do_explode_right(pair[i], 0, y):
            return True


def explode(pair, outer, position, path, stack):
    x = pair[0]
    y = pair[1]
    outer[position] = "*"

    explode_left(path, x)
    explode_right(outer, position, stack, y)

    outer[position] = 0

    return outer

def reduce_split(number, outer = None, position = 0):

    if isinstance(number, int):
        if number >= 10:
            split(number, outer, position)
            return True
    else:
        for i in [0,1]:
            if reduce_split(number[i], number, i):
                return True

    return False


def reduce_explode(number, outer = None, position = 0, depth = 1, path = None, stack = None):

    if stack is None:
        stack = []
    if outer is not None:
        stack = [(outer, position)] + stack
        path.insert(0, (outer, position))

    if not isinstance(number, int):

        if depth == 5:
            explode(number, outer, position, path, stack)
            return True

        for i in [0,1]:
            if reduce_explode(number[i], number, i, depth+1, path, stack):
                return True

    return False

def reduce(number):
    reducing = True
    while reducing:
        reducing = reduce_explode(number, path = [])
        if not reducing:
            reducing = reduce_split(number)

    return number

def sum(values):
    result = None
    for value in values:
        result = add(result, value)
        reduce(result)

    return result

def calc_magnitude(value):
    magnitude = 0
    if isinstance(value[0], int):
        magnitude += 3 * value[0]
    else:
        magnitude += 3 * calc_magnitude(value[0])

    if isinstance(value[1], int):
        magnitude += 2 * value[1]
    else:
        magnitude += 2 * calc_magnitude(value[1])

    return magnitude


def sum_to_magnitude(values):
    return calc_magnitude(sum(values))

def copy(value):
    return [x if isinstance(x, int) else copy(x) for x in value]

def max_magnitude(values):

    max_mag = 0
    for i in range(0, len(values)):
        for j in range(0, len(values)):
            if i != j:
                max_mag = max(max_mag, sum_to_magnitude([copy(values[i]), copy(values[j])]))
                max_mag = max(max_mag, sum_to_magnitude([copy(values[j]), copy(values[i])]))
    return max_mag

