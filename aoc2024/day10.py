import os

import cmn

with open(os.path.join(os.getcwd(), 'resources', 'day10.txt')) as file:
    topographic_map = [list(map(int, list(line.strip()))) for line in file]

directions = [(-1, 0), (0, 1), (1, 0), (0, -1)]
dim = (len(topographic_map), len(topographic_map[0]))

def pos_val(pos):
    return topographic_map[pos[0]][pos[1]]
def add(p1, p2):
    return cmn.add_tuples(p1, p2)

# Go for a hike, starting at the specified trailhead, and find all the possible trails (paths of ascending elevation
# that end in a 9). Unique specifies if the number of unique paths for a trail should be counted or not.
def hike(trailhead, unique):
    q = [trailhead]
    height = 0
    visited = set()

    while q and height < 9:
        s = len(q)
        for _ in range(s):
            node = q.pop(0)
            visited.add(node)
            q.extend(n for n in (add(node, d) for d in directions if cmn.in_bounds(add(node, d), dim))
                     if n not in visited and pos_val(n) == height + 1 and (unique or n not in q))
        height += 1

    return len(q)

# What is the sum of the scores of all trailheads on your topographic map?
def puzzle_one():
    return sum(
        hike((row, col), False)
        for col in range(dim[1]) for row in range(dim[0]) if pos_val((row, col)) == 0
    )

# What is the sum of the ratings of all trailheads?
def puzzle_two():
    return sum(
        hike((row, col), True)
        for col in range(dim[1]) for row in range(dim[0]) if pos_val((row, col)) == 0
    )

print(f"puzzle one solution = {puzzle_one()}")
print(f"puzzle two solution = {puzzle_two()}")