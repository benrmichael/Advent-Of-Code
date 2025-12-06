import os
from collections import defaultdict

with open(os.path.join(os.getcwd(), 'resources', 'day20.txt')) as file:
    racetrack = {(x, y): o for y, line in enumerate(file.read().splitlines()) for x, o in enumerate(line.strip())}

start = list(racetrack.keys())[list(racetrack.values()).index("S")]
end = list(racetrack.keys())[list(racetrack.values()).index("E")]
racetrack[end] = "."
tracks = {(x, y) for (x, y), t in racetrack.items() if t == "."}

path = [start]
lookup = {start: 0}
x, y = start
index = 1
while tracks:
    for dx, dy in [(1, 0), (0, 1), (-1, 0), (0, -1)]:
        track = (x+dx, y+dy)
        if track in tracks:
            tracks.remove(track)
            path.append(track)
            lookup[track] = index
            x, y = track
            index += 1
            break


def puzzle_one():
    possible_cheats = defaultdict(int)
    taken = set()
    for i, (x, y) in enumerate(path):
        for cx, cy in [(2, 0), (0, 2), (-2, 0), (0, -2)]:
            state = (x+cx, y+cy)
            if state not in racetrack or racetrack[state] != "." or (x, y, x+cx, y+cy) in taken:
                continue
            if (cheat_index := lookup[state]) > i:
                taken.add((x, y, x+cx, y+cy))
                possible_cheats[cheat_index - i - 2] += 1
    del possible_cheats[0]
    return sum([n for s, n in possible_cheats.items() if s >= 100])


def puzzle_two():
    possible_cheats = defaultdict(int)
    taken = set()
    for i, (x, y) in enumerate(path):
        time_left = 20
        visited = set()
        q = [(x, y)]
        while time_left >= 0 and q:
            l = len(q)
            for _ in range(l):
                state = q.pop(0)
                if state in visited:
                    continue
                visited.add(state)
                cs = (x, y, *state)
                if racetrack[state] == "." and cs not in taken and (ci := lookup[state]) > i:
                    taken.add(cs)
                    possible_cheats[ci-i-(20-time_left)] += 1
                qx, qy = state
                for dqx, dqy in [(1, 0), (0, 1), (-1, 0), (0, -1)]:
                    if (qx+dqx, qy+dqy) in racetrack and (qx+dqx, qy+dqy) not in visited:
                        q.append((qx+dqx, qy+dqy))
            time_left -= 1
    del possible_cheats[0]
    return sum([n for s, n in possible_cheats.items() if s >= 100])


print(f"puzzle one solution={puzzle_one()}")
print(f"puzzle two solution={puzzle_two()}")