import os
from collections import defaultdict, deque
from math import inf

with open(os.path.join(os.getcwd(), 'resources', 'day16.txt')) as file:
    reindeer_maze = {(x, y): o for y, line in enumerate(file.read().splitlines()) for x, o in enumerate(line.strip())}

facing = (1, 0)
start = list(reindeer_maze.keys())[list(reindeer_maze.values()).index("S")]
end = list(reindeer_maze.keys())[list(reindeer_maze.values()).index("E")]
best_seats = defaultdict(set)
visited = defaultdict(lambda:inf)
dq = deque()
dq.append((*start, *facing, 0, {start}))

min_cost = inf
while dq:
    x, y, dx, dy, cost, path = dq.popleft()

    if cost > visited[(x, y, dx, dy)] or cost > min_cost:
        continue
    visited[(x, y, dx, dy)] = cost

    if (x, y) == end:
        if cost <= min_cost:
            min_cost = cost
            best_seats[cost] |= path
        continue

    if reindeer_maze[(x+dx, y+dy)] != '#':
        dq.append((x+dx, y+dy, dx, dy, cost+1, path.copy() | {(x+dx, y+dy)}))

    for rx, ry in [(-dy, dx), (dy, -dx)]:
        dq.append((x, y, rx, ry, cost+1000, path.copy()))

print(f"puzzle one solution={min_cost}")
print(f"puzzle two solution={len(best_seats[min_cost])}")
