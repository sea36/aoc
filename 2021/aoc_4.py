
def load_game(filename):
    file = open(filename, "r")
    try:
        lines = file.readlines()
        boards = get_boards(lines)
        drawn_numbers = get_drawn_numbers(lines)
        return Game(boards, drawn_numbers)
    finally:
        file.close()

class Game:
    def __init__(self, boards, drawn_numbers):
        self.boards = boards
        self.drawn_numbers = drawn_numbers

class Board:

    def __init__(self, lines):
        self.numbers =  [parse_line(line) for line in lines]
        self.row_counts = [0] * 5
        self.col_counts = [0] * 5

    def call(self, draw):
        for row in range(0, 5):
            for col in range(0,5):
                if self.numbers[col][row] == draw:
                    self.numbers[col][row] += 1000
                    self.row_counts[row] += 1
                    self.col_counts[col] += 1

    def is_winner(self):
        return any(x >= 5 for x in self.row_counts) or any(x >= 5 for x in self.col_counts)

    def score(self):
        score = 0
        for row in range(0, 5):
            for col in range(0,5):
                if self.numbers[col][row] < 1000:
                    score += self.numbers[col][row]
        return score

    def reset(self):
        self.row_counts = [0] * 5
        self.col_counts = [0] * 5
        for row in range(0, 5):
            for col in range(0,5):
                if self.numbers[col][row] >= 1000:
                    self.numbers[col][row] -= 1000

def get_drawn_numbers(lines):
    return [int(x) for x in lines[0].strip().split(",")]

def parse_line(line):
    return [int(line[0:2]), int(line[3:5]), int(line[6:8]), int(line[9:11]), int(line[12:14])]

def get_boards(lines):
    boards = []
    for i in range(2, len(lines), 6):
        boards.append(Board(lines[i:i+5]))
    return boards

def part_a(filename):
    game = load_game(filename)
    for draw in game.drawn_numbers:
        for board in game.boards:
            board.call(draw)
            if board.is_winner():
                return board.score() * draw
    return None

def part_b(filename):
    game = load_game(filename)
    boards = game.boards.copy()
    for draw in game.drawn_numbers:
        # copy to iterate, as we remove during iteration
        for board in list(boards):
            board.call(draw)
            if board.is_winner():
                if (len(boards) == 1):
                    return board.score() * draw
                else:
                    boards.remove(board)
