
def load_caves(filename):
    file = open(filename, "r")
    lines = file.readlines()
    caves = CaveSystem()
    for line in lines:
        passage = line.strip().split("-")
        caves.add_passage(passage[0], passage[1])
    return caves

class CaveSystem:

    def __init__(self):
        self.caves = {}

    def add_passage(self, name1, name2):
        cave1 = self.get_cave(name1)
        cave2 = self.get_cave(name2)
        cave1.neighbours.append(cave2)
        cave2.neighbours.append(cave1)

    def get_cave(self, name):
        if name in self.caves:
            return self.caves[name]
        cave = Cave(name)
        self.caves[name] = cave
        return cave

class Cave:
    def __init__(self, name):
        self.name = name
        self.small = name.islower()
        self.big = name.isupper()
        self.neighbours = []

class Searcher:

    def __init__(self, caves, max_small_visits):
        self.caves = caves
        self.paths = []
        self.path = []
        self.start = caves.get_cave("start")
        self.end = caves.get_cave("end")
        self.max_small_visits = max_small_visits

    def can_visit(self, cave):
        if cave == self.start:
            return False
        if cave.big:
            return True
        if not cave in self.path:
            return True

        this_count = 0
        small_visited = set()
        for visited in self.path:
            if visited.small:
                if visited == self.start:
                    continue
                if visited == cave:
                    this_count += 1
                else:
                    if visited in small_visited:
                        return False

                small_visited.add(visited)
        return this_count <= self.max_small_visits

    def search(self):
        self.path = []
        self.paths = []
        self.dfs(self.start)
        return self.paths

    def dfs(self, cave):
        self.path.append(cave)

        if cave == self.end:
            self.paths.append(self.path.copy())
            return

        for neighbour in cave.neighbours:
            if self.can_visit(neighbour):
                self.dfs(neighbour)
                self.path.pop()

def part_a(filename):
    caves = load_caves(filename)
    searcher = Searcher(caves, 0)
    paths = searcher.search()
    return len(paths)

def part_b(filename):
    caves = load_caves(filename)
    searcher = Searcher(caves, 1)
    paths = searcher.search()
    return len(paths)
