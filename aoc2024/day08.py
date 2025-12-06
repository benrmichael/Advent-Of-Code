import os
import cmn
from collections import defaultdict

with open(os.path.join(os.getcwd(), 'resources', 'day8.txt')) as file:
    lines = [line.strip() for line in file if not line.isspace()]

antennas = defaultdict(list)
for i, row in enumerate(lines):
    for j, char in enumerate(row):
        if char != '.':
            antennas[char].append((i, j))

dim = (len(lines), len(lines[0]))

def cartesian_distance(p1, p2):
    return p1[0] - p2[0], p1[1] - p2[1]


def generate_antinodes(p1: tuple[int, int], p2: tuple[int, int], part2=False):
    dist1, dist2 = cartesian_distance(p1, p2), cartesian_distance(p2, p1)
    anode1, anode2 = cmn.add_tuples(p1, dist1), cmn.add_tuples(p2, dist2)

    if not part2:
        return [cmn.tuple_str(p) for p in [anode1, anode2] if cmn.in_bounds(p, dim)]

    antinodes = {cmn.tuple_str(p1), cmn.tuple_str(p2)}
    while cmn.in_bounds(anode1, dim):
        antinodes.add(cmn.tuple_str(anode1))
        anode1 = cmn.add_tuples(anode1, dist1)
    while cmn.in_bounds(anode2, dim):
        antinodes.add(cmn.tuple_str(anode2))
        anode2 = cmn.add_tuples(anode2, dist2)

    return list(antinodes)

def puzzle_one(part2=False) -> int:
    locations = set()
    for antenna, nodes in antennas.items():
        for index in range(len(nodes)):
            for node in nodes[index + 1:]:
                antinodes = generate_antinodes(nodes[index], node, part2)
                locations.update(antinodes)

    return len(locations)

def puzzle_two() -> int:
    return puzzle_one(True)


print(f"puzzle one solution = {puzzle_one()}")
print(f"puzzle two solution = {puzzle_two()}")