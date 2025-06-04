
def calc_dimensions(lines):
    xmax = 0
    ymax = 0
    for line in lines:
        if ',' in line:
            xy = line.split(',')
            xmax = max(xmax, int(xy[0]))
            ymax = max(ymax, int(xy[1]))
    return [xmax, ymax]


def load_polymer(filename):
    file = open(filename, "r")
    try:
        lines = [line.strip() for line in file.readlines()]
        template = lines[0]
        rules = parse_rules(lines)
        return Polymer(template, rules)
    finally:
        file.close()

def parse_rules(lines):
    rules = {}
    for line in lines:
        if '->' in line:
            parts = line.split('->')
            element_pair = parts[0].strip()
            insert = parts[1].strip()
            result = [
                str(element_pair[0] + insert),
                str(insert + element_pair[1]),
            ]
            rules[element_pair] = result
    return rules

def count_pairs(template):
    pair_counts = {}
    for i in range(0, len(template) - 1):
        element_pair = template[i:i+2]
        pair_counts[element_pair] = pair_counts.get(element_pair, 0) + 1
    return pair_counts

class Polymer:

    def __init__(self, template, rules):
        self.template = template
        self.rules = rules
        self.pair_counts = count_pairs(template)

    def generate(self):
        new_pair_counts = {}
        for (pair, count) in self.pair_counts.items():
            for new_pair in self.rules.get(pair, [pair]):
                new_pair_counts[new_pair] = new_pair_counts.get(new_pair, 0) + count
        self.pair_counts = new_pair_counts

    def count_elements(self):
        counts = {}

        for (pair, count) in self.pair_counts.items():
            self.do_count(pair[0], counts, count)
            self.do_count(pair[1], counts, count)

        # increment terminators - everything else was double counted
        self.do_count(self.template[0], counts, 1)
        self.do_count(self.template[-1], counts, 1)

        return {element:int(count/2) for (element,count) in counts.items()}

    def do_count(self, element, counts, count):
        counts[element] = counts.get(element, 0) + count


def polymerise(filename, step_count):
    polymer = load_polymer(filename)

    for i in range(0, step_count):
        polymer.generate()

    element_counts = polymer.count_elements()
    max_element = max(element_counts, key=element_counts.get)
    min_element = min(element_counts, key=element_counts.get)

    return element_counts[max_element] - element_counts[min_element]
