
def load_lines(filename):
    file = open(filename, "r")
    try:
        return file.readlines()
    finally:
        file.close()

CLOSURES = {'(': ')', '[': ']', '{': '}', '<': '>'}

def find_corruption(line):
    closures = []
    for ch in line:
        if ch in CLOSURES:
            closures.append(CLOSURES[ch])
        else:
            expected = closures.pop()
            if ch != expected:
                return ch
    return None

def find_incomplete(line):
    closures = []
    for ch in line:
        if ch in CLOSURES:
            closures.append(CLOSURES[ch])
        else:
            expected = closures.pop()
            if ch != expected:
                return None
    closures.reverse()
    return closures

def corruption_score(corruption):
    if corruption == ')':
        return 3
    if corruption == ']':
        return 57
    if corruption == '}':
        return 1197
    if corruption == '>':
        return 25137

def incomplete_score(incomplete):
    score = 0
    for ch in incomplete:
        if ch == ')':
            score = (score * 5) + 1
        elif ch == ']':
            score = (score * 5) + 2
        elif ch == '}':
            score = (score * 5) + 3
        elif ch == '>':
            score = (score * 5) + 4
    return score

def part_a(filename):
    lines = load_lines(filename)
    score = 0
    for line in lines:
        corruption = find_corruption(line.strip())
        if corruption is not None:
            score += corruption_score(corruption)
    return score

def part_b(filename):
    lines = load_lines(filename)
    scores = []
    for line in lines:
        incomplete = find_incomplete(line.strip())
        if incomplete is not None:
            scores.append(incomplete_score(incomplete))

    scores.sort()

    l = len(scores)
    mid = int((l-1)/2)    # zero-indexed

    return scores[mid]
