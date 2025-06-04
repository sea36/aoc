
def load_data(filename):
    file = open(filename, "r")
    lines = [line.strip() for line in file.readlines()]
    split = [line.split(" | ") for  line in lines]
    return [{"patterns": x[0].split(" "), "output": x[1].split(" ")} for x in split]

data = load_data("data/input8.txt")

unique_sizes = {2, 3, 4, 7}

count = 0
for entry in data:
    for digit in entry["output"]:
        if len(digit) in unique_sizes:
            count += 1

print(count)

digit_segments = ["abcefg", "cf", "acdeg", "acdfg", "bcdf", "abdfg", "abdefg", "acf", "abcdefg", "abcdfg"]
length_digits = {2: [1], 3: [7], 4: [4], 5: [2, 3, 5], 6:[0, 6, 9], 7: [8]}

digit_masks = [
    0b1110111,
    0b0010010,
    0b1011101,
    0b1011011,
    0b0111010,
    0b1101011,
    0b1010010,
    0b1111111,
    0b1111011
]

segment_counts = {4: ["e"], 6: ["b"], 7: ["d", "g"], 8: ["a", "c"], 9: ["f"]}

def map_segments(patterns):

    counts = {"a": 0, "b": 0, "c": 0, "d": 0, "e": 0, "f": 0, "g": 0}
    for pattern in patterns:
        for segment in pattern:
            counts[segment] += 1

    # First constraint: popcount
    solution = {}
    for segment in ["a", "b", "c", "d", "e", "f", "g"]:
        count = counts[segment]
        possible_solutions = segment_counts[count]
        solution[segment] = list(possible_solutions)

    patterns.sort(key=lambda x: len(x))

    for i in range(0, 10):
        for pattern in patterns:
            possible_digits = length_digits[len(pattern)]

            really_possible = []

            for digit in possible_digits:
                possible = True
                for segment in pattern:
                    if len(solution[segment]) == 1 and solution[segment][0] not in digit_segments[digit]:
                        possible = False
                if possible:
                    really_possible.append(digit)

            if len(really_possible) == 1:
                digit = really_possible[0]

                ds = set()
                for s in digit_segments[digit]:
                    ds.add(s)

                for segment in pattern:
                    ps = set(solution[segment])
                    solution[segment] = list(ps.intersection(ds))

        return solution

result = 0

for entry in data:
    segment_mapping = map_segments(entry["patterns"])

    output = ""
    for digit in entry["output"]:
        real_segments =  []
        for segment in digit:
            real_segments.append(segment_mapping[segment][0])
        real_segments.sort()

        token = ""
        for segment in real_segments:
            token += segment

        output += str(digit_segments.index(token))
    print(output)
    result += int(output)

print(result)




