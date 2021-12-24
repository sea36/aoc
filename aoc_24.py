import math


##
## Observation from looking at instructions, only 'z' register propagates from one input digit to the next
## so we can explore the space one digit at a time, and only explore possible 'z' values for the next digit
##

def alu_eval(prog, inputs, init_registers=(0, 0, 0, 0)):
    w, x, y, z = init_registers

    registers = {'w': w, 'x': x, 'y': y, 'z': z}

    input_iter = iter(inputs)
    for (cmd, a, b) in prog:
        if not isinstance(b, int):
            b = registers[b]

        if cmd == 'inp':
            registers[a] = next(input_iter)
        elif cmd == 'add':
            registers[a] = registers[a] + b
        elif cmd == 'mul':
            registers[a] = registers[a] * b
        elif cmd == 'div':
            registers[a] = math.floor(registers[a] / b)
        elif cmd == 'mod':
            registers[a] = registers[a] % b
        elif cmd == 'eql':
            registers[a] = 1 if registers[a] == b else 0
        else:
            raise Exception(cmd)

    return (registers['w'], registers['x'], registers['y'], registers['z'])


def parse_instruction(line):
    (cmd, *args) = line.strip().split(' ')
    if cmd == 'inp':
        return (cmd, args[0], 0)
    args = [int(arg) if arg.lstrip('-').isnumeric() else arg for arg in args]
    return (cmd,) + tuple(args)

def parse_prog(lines):
    progs = []
    prog = []
    for line in lines:
        if 'inp' in line and prog:
            progs.append(prog)
            prog = []
        prog.append(parse_instruction(line))

    return progs + [prog]


def load_prog(filename):
    with open(filename, 'r') as file:
        return parse_prog(file.readlines())


digit_progs = load_prog('data/input24.txt')

results = {0: ((), ())}
for digit in range(14):

    prog = digit_progs[digit]
    max_z = 26 ** (14 - digit)

    print("Digit:", digit, "search space: ", len(results))

    next_results = {}
    for (z0, (max_digits, min_digits)) in results.items():
        for i in range(1, 10):
            (w, x, y, z) = alu_eval(prog, [i], (0, 0, 0, z0))

            ## we're searching for z = 0, and z gets divided by either 1 or 26 each step
            ## so if z > 26 ^ remaining_steps, we can discard it
            ## this optimisation takes us from ~30 minutes -> ~40 seconds
            if z > max_z:
                continue

            new_max_digits = max_digits + (i,)
            new_min_digits = min_digits + (i,)

            if z not in next_results:
                next_results[z] = (new_max_digits, new_min_digits)
            else:
                prev_solution = next_results[z]
                next_results[z] = (max(new_max_digits, prev_solution[0]), min(new_min_digits, prev_solution[1]))

    results = next_results

model_no = results[0]
print("max", "".join([str(x) for x in model_no[0]]))
print("min", "".join([str(x) for x in model_no[1]]))
