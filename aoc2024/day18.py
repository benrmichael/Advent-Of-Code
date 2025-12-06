import os

steps = [(1, 0), (-1, 0), (0, 1), (0, -1)]

with open(os.path.join(os.getcwd(), 'resources', 'day18.txt')) as file:
    falling_bytes = [(int(line.split(',')[0]), int(line.split(',')[1])) for line in file]

def populate_grid(n_bytes, m, n):
    grid = [['.'] * n for _ in range(m)]
    for i in range(n_bytes):
        x, y = falling_bytes[i]
        grid[y][x] = '#'
    return grid

def can_step(x, y, g):
    lx, ly = len(g[0]), len(g)
    if 0 <= x < lx and 0 <= y < ly and g[y][x] != '#':
        return True
    return False

def bfs(g):
    end = len(g[0]) - 1, len(g) - 1
    q = [(0, 0)]
    visited = set()
    s = 0

    while q:
        l = len(q)
        for _ in range(l):
            x, y = q.pop(0)
            if (x, y) == end:
                return s
            if (x, y) in visited:
                continue
            visited.add((x, y))
            for dx, dy in steps:
                if can_step(x + dx, y + dy, g):
                    q.append((x + dx, y + dy))
        s += 1

    return None

def puzzle_one():
    grid = populate_grid(1024, 71, 71)
    return bfs(grid)

def puzzle_two():
    L, R = 1024, len(falling_bytes)

    fc = None
    while L <= R:
        M = (L + R) // 2
        if not bfs(populate_grid(M, 71, 71)):
            fc = min(fc, M) if fc else M
            R = M - 1
        else:
            L += 1
    return falling_bytes[fc-1]


print(f"puzzle one solution={puzzle_one()}")
print(f"puzzle two solution={puzzle_two()}")