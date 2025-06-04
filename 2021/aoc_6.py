
def load_state(filename):
    file = open(filename, 'r')
    try:
        state = file.readline()
        state_counts = [0] * 9
        for cycle_stage in state.split(','):
            state_counts[int(cycle_stage)] += 1
        return state_counts
    finally:
        file.close()

def advance_generation(state_counts):
    breeding_fish = state_counts[0]
    for i in range(0, 8):
        state_counts[i] = state_counts[i + 1]
    state_counts[6] += breeding_fish
    state_counts[8] = breeding_fish

def simulate(filename, generations):
    state_counts = load_state(filename)
    for i in range(0, generations):
        advance_generation(state_counts)
    return sum(state_counts)






