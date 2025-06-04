
import heapq

# Represent current state as a string (makes debugging easy)
# These are the valid positions for amphipods in the string
HALLWAY_POSNS = [15, 16, 18, 20, 22, 24, 25]
ROOM_POSNS = [[31, 45, 57, 69], [33, 47, 59, 71], [35, 49, 61, 73], [37, 51, 63, 75]]
ROOM_ENTRANCE_POSNS = [17, 19, 21, 23]

EMPTY = '.'
COST_MULTIPLIERS = {'A': 1, 'B': 10, 'C': 100, 'D': 1000}

def is_done(map):
    return (all([map[i] == 'A' for i in ROOM_POSNS[0]])
        and all([map[i] == 'B' for i in ROOM_POSNS[1]])
        and all([map[i] == 'C' for i in ROOM_POSNS[2]])
        and all([map[i] == 'D' for i in ROOM_POSNS[3]]))

def do_move(from_pos, to_pos, map):
    amphipod = map[from_pos]
    map = map[:from_pos] + EMPTY + map[from_pos + 1:]
    map = map[:to_pos] + amphipod + map[to_pos + 1:]
    return map

def get_target_room(amphipod):
    if amphipod == 'A':
        return 0
    if amphipod == 'B':
        return 1
    if amphipod == 'C':
        return 2
    if amphipod == 'D':
        return 3
    raise Exception(f'Unknown amphipod: {amphipod}')

def get_destination(from_pos, map):
    amphipod = map[from_pos]
    target_room_no = get_target_room(amphipod)
    target_room_posns = ROOM_POSNS[target_room_no]
    for (i, pos) in enumerate(target_room_posns[::-1]):
        if map[pos] == EMPTY:
            hallway_steps = calc_steps_to_move_from_hall_to_room(from_pos, target_room_no, map)
            if hallway_steps is None:
                return None
            steps_into_room = 4 - i
            return (pos, hallway_steps + steps_into_room)
        if map[pos] is not amphipod:
            return None
    raise Exception('Room full!')


def is_route_clear(route):
    return all([x == EMPTY for x in route])


def calc_steps_to_move_from_room_to_hall(room_no, to_pos, map):
    entry_pos = ROOM_ENTRANCE_POSNS[room_no]
    route = map[entry_pos+1:to_pos + 1] if to_pos > entry_pos else map[to_pos:entry_pos]
    return len(route) if is_route_clear(route) else None


def calc_steps_to_move_from_hall_to_room(from_pos, room_no, map):
    entry_pos = ROOM_ENTRANCE_POSNS[room_no]
    route = map[from_pos + 1:entry_pos + 1] if entry_pos > from_pos else map[entry_pos:from_pos]
    return len(route) if is_route_clear(route) else None


def track_path(from_map, to_map, cost):
    if to_map in path:
        (old_from, old_cost) = path[to_map]
        if cost > old_cost:
            return
    path[to_map] = (from_map, cost)


def enqueue_moves(map, cost):

    for src_pos in HALLWAY_POSNS:
        amphipod = map[src_pos]
        if amphipod != EMPTY:
            move = get_destination(src_pos, map)
            if move is not None:
                dest_pos, move_steps = move
                result_map = do_move(src_pos, dest_pos, map)
                result_cost = cost + (move_steps * COST_MULTIPLIERS[amphipod])
                heapq.heappush(heap, (result_cost, result_map))
                track_path(map, result_map, result_cost)

    for (room_no, room) in enumerate(ROOM_POSNS):
        for (exit_steps, src_pos) in enumerate(room, start = 1):
            amphipod = map[src_pos]
            if amphipod != EMPTY:
                for dest_pos in HALLWAY_POSNS:
                    move_steps = calc_steps_to_move_from_room_to_hall(room_no, dest_pos, map)
                    if move_steps is not None:
                        result_map = do_move(src_pos, dest_pos, map)
                        result_cost = cost + ((exit_steps + move_steps) * COST_MULTIPLIERS[amphipod])
                        heapq.heappush(heap, (result_cost, result_map))
                        track_path(map, result_map, result_cost)
                break

path = {}
visited = set()

input_lines = [
    '#############\n',
    '#...........#\n',
    '###D#A#B#C###\n',
    '  #D#C#B#A#\n',
    '  #D#B#A#C#\n',
    '  #B#A#D#C#\n',
    '  #########\n',
]
start_map = ''.join(input_lines)
start_cost = 0

heap = [(start_cost, start_map)]
heapq.heapify(heap)

while heap:
    cost, map = heapq.heappop(heap)

    if map in visited:
        continue

    visited.add(map)

    if is_done(map):
        while map:
            print(map)
            map, _ = path.get(map, (None, None))

        print("Cost:", cost)
        break

    enqueue_moves(map, cost)
