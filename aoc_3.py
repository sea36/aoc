from functools import reduce

def load_diagnostics(filename):
    file = open(filename, "r")
    try:
        lines = file.readlines()
        return [s.strip() for s in lines]
    finally:
        file.close()

def count_bits_values(values, posn):
    result = [0, 0]
    for value in values:
        if (value[posn] == '0'):
            result[0] = result[0] + 1
        else:
            result[1] = result[1] + 1
    return result

def part_a(filename):
    diagnostics = load_diagnostics(filename)
    diagnostic_bits = map(lambda value: [1 if x == '1' else -1 for x in value], diagnostics)

    bit_count = len(diagnostics[0])
    init = [0] * bit_count
    result = reduce(lambda accuml, entry: [acc + ent for acc, ent in zip(accuml, entry)], diagnostic_bits, init)

    # 1 if most commonly 1, else 0
    gamma_str = "".join(["1" if x > 0 else "0" for x in result])
    gamma = int(gamma_str, 2)

    # 0 if most commonly 1, else 1
    epsilon_str = "".join(["0" if x > 0 else "1" for x in result])
    epsilon = int(epsilon_str, 2)

    return gamma * epsilon

def part_b(filename):
    diagnostics = load_diagnostics(filename)

    remaining = diagnostics
    posn = 0
    while len(remaining) > 1:
        bit_counts = count_bits_values(remaining, posn)
        mask = "0" if bit_counts[0] > bit_counts[1] else "1"
        remaining = list(filter((lambda x: x[posn] == mask), remaining))
        posn = posn + 1

    oxygen_rating = int(remaining[0], 2)

    remaining = diagnostics
    posn = 0
    while len(remaining) > 1:
        bit_counts = count_bits_values(remaining, posn)
        mask = "0" if bit_counts[0] <= bit_counts[1] else "1"
        remaining = list(filter((lambda x: x[posn] == mask), remaining))
        posn = posn + 1

    co2_rating = int(remaining[0], 2)

    return oxygen_rating * co2_rating
