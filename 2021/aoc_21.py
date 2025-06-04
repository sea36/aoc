
DIRAC_ROLLS = [
    (3, 1),
    (4, 3),
    (5, 6),
    (6, 7),
    (7, 6),
    (8, 3),
    (9, 1),
]


class DeterministicDie:

    def __init__(self, values):
        self.values = values
        self.rolls = 0

    def next_roll(self):
        index = self.rolls % len(self.values)
        self.rolls += 1
        return self.values[index]

    def roll_count(self):
        return self.rolls

class Pawn:

    def __init__(self, start):
        self.position = start
        self.score = 0

    def move(self, rolls):
        self.position += sum(rolls)
        while self.position > 10:
            self.position -= 10
        self.score += self.position

def part_a(p1_start, p2_start):

    die = DeterministicDie(range(1,101))

    player1 = Pawn(p1_start)
    player2 = Pawn(p2_start)

    while True:

        player1.move([die.next_roll(), die.next_roll(), die.next_roll()])
        if player1.score >= 1000:
            break

        player2.move([die.next_roll(), die.next_roll(), die.next_roll()])
        if player2.score >= 1000:
            break

    rolls = die.rolls
    losing_score = min(player1.score, player2.score)

    return rolls * losing_score


def find_most_wins(p1_start, p2_start):

    # ((p1_pos, p1_score), (p2_pos, p2_score)) : count
    universes = { ((p1_start, 0), (p2_start, 0)): 1 }

    p1_wins = 0
    p2_wins = 0

    player_index = 0

    def new_pos(old, move):
        pos = old + move
        while pos > 10:
            pos -= 10
        return pos

    while len(universes) > 0:

        next_universes = {}

        for state in universes:
            state_count = universes.get(state)
            (p1, p2) = state

            for (move, count) in DIRAC_ROLLS:
                if player_index == 0:
                    pos = new_pos(p1[0], move)
                    next_p1 = (pos, p1[1] + pos)
                    next_p2 = p2
                else:
                    pos = new_pos(p2[0], move)
                    next_p1 = p1
                    next_p2 = (pos, p2[1] + pos)

                new_count = state_count * count
                if next_p1[1] >= 21:
                    p1_wins += new_count
                elif next_p2[1] >= 21:
                    p2_wins += new_count
                else:
                    new_state = (next_p1, next_p2)
                    next_universes[new_state] = next_universes.get(new_state, 0) + new_count

        universes = next_universes
        player_index = (player_index + 1) % 2

    return max(p1_wins, p2_wins)
