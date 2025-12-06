import os

lab_map = []
movements = [(-1, 0), (0, 1), (1, 0), (0, -1)]

with open(os.path.join(os.getcwd(), 'resources', 'day6.txt')) as file:
    for line in file:
        if '^' in line:
            guard_start = (len(lab_map), line.find('^'))
        lab_map.append(line)

# Get the next movement after the current movement, 'i'
def next_movement(i) -> int:
    return (i + 1) % len(movements)

# Check if the tuple position 'p' is out of map boundaries
def out_of_bounds(p: tuple[int, int]) -> bool:
    return not 0 <= p[0] < len(lab_map) or not 0 <= p[1] < len(lab_map[0])

# Adds two tuples together and returns the tuple that is the sum of the values
def add_tuples(tup1: tuple[int, int], tup2: tuple[int, int]):
    return tuple(map(sum, zip(tup1, tup2)))

def previous_position(p, i) -> tuple[int, int]:
    return p[0] - movements[i][0], p[1] - movements[i][1]

# Check if the tuple position 'p' is an obstacle
def obstacle_at_position(p, m) -> bool:
    if m is None:
        m = lab_map
    return m[p[0]][p[1]] == '#'

# Puzzle one is finding all the unique locations the guard would travel to before leaving the patrol area
def puzzle_one() -> set[tuple[int, int]]:
    visited = set()
    movement = 0
    guard = guard_start

    while not out_of_bounds(guard):
        if obstacle_at_position(guard, lab_map):
            guard = previous_position(guard, movement)
            movement = next_movement(movement)
        else:
            visited.add(guard)
            guard = add_tuples(guard, movements[movement])

    return visited

# Checks to see if we walk this modified lab map, will we get stuck in a loop? The criteria for being stuck means
# we have already crossed some location in this same direction, e.g. we are starting to walk a circle...
def stuck_in_loop(modified_lab_map) -> bool:
    history = set()
    movement = 0
    guard = guard_start

    while not out_of_bounds(guard):
        if obstacle_at_position(guard, modified_lab_map):
            guard = previous_position(guard, movement)
            movement = next_movement(movement)

        travelled = frozenset({guard, movement})
        if travelled in history:
            return True

        history.add(travelled)
        guard = add_tuples(guard, movements[movement])

    return False

# Puzzle two is finding all the locations (except the guard's starting position) in which if an obstacle is placed
# the guard will get stuck in an infinite loop
def puzzle_two(walked_path) -> int:
    loops = 0

    walked_path = walked_path -  {guard_start} # guard starting position cannot have an obstacle
    for location in walked_path:
        modified_lab_map = lab_map.copy()
        old_path = modified_lab_map[location[0]]
        modified_path = old_path[:location[1]] + '#' + old_path[location[1] + 1:]
        modified_lab_map[location[0]] = modified_path

        if stuck_in_loop(modified_lab_map):
            loops += 1

    return loops


guard_path = puzzle_one()
print(f"Puzzle one solution = {len(guard_path)}")
print(f"Puzzle two solution = {puzzle_two(guard_path)}")