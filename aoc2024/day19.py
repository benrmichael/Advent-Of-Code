import os
from functools import cache

with open(os.path.join(os.getcwd(), 'resources', 'day19.txt')) as file:
    lines = file.readlines()
    patterns = set(lines[0].strip().split(", "))
    designs = [line.strip() for line in lines[2:]]


@cache
def can_make(d):
    if not d:
        return 1
    return sum(can_make(d[len(p):]) for p in patterns if d.startswith(p))


p1, p2 = 0, 0
for design in designs:
    n = can_make(design)
    if n > 0:
        p1 += 1
        p2 += n

print(f"puzzle one solution={p1}")
print(f"puzzle two solution={p2}")