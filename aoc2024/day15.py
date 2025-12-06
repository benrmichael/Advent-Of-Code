import os

WALL = "#"
directions = { "v": (0, 1), ">": (1, 0), "^": (0, -1), "<": (-1, 0) }

with open(os.path.join(os.getcwd(), 'resources', 'day15.txt')) as file:
    lines = file.readlines()
    warehouse_map = [list(line.strip()) for line in lines if not line.isspace() and "#" in line]
    moves = "".join(line.strip() for line in lines if not line.isspace() and not "#" in line)

dimensions = (len(warehouse_map), len(warehouse_map[0]))

# find where the robot starts in the maze
def find_start(warehouse):
    for (x, y), item in warehouse.items():
        if item == "@":
            return x, y
    return None

# explore the warehouse
def explore_warehouse(warehouse, box, can_move_box):
    pos = find_start(warehouse)
    for move in moves:
        x, y = pos
        dx, dy = directions[move]
        nx, ny = x + dx, y + dy

        if warehouse[(nx, ny)] == WALL:
            continue
        elif warehouse[(nx, ny)] in box:
            if can_move_box(warehouse, nx, ny, dx, dy, box):
                pos = (nx, ny)
                warehouse[(x, y)] = '.'
                warehouse[(nx, ny)] = '@'
        else:
            pos = (nx, ny)
            warehouse[(x, y)] = '.'
            warehouse[(nx, ny)] = '@'

def p1_move_box(wh, x, y, dx, dy, b):
    ix, iy = x, y
    while (item := wh[(ix + dx, iy + dy)]) != WALL:
        if item not in b:
            mx, my = ix + dx, iy + dy
            while (mx, my) != (x, y):
                wh[(mx, my)], wh[(mx - dx, my - dy)] = wh[(mx - dx, my - dy)], wh[(mx, my)]
                mx, my = mx - dx, my - dy
            return True
        ix, iy = ix + dx, iy + dy
    return False

# Predict the motion of the robot and boxes in the warehouse. After the robot is finished moving, what is the sum of
# all boxes' GPS coordinates?
def puzzle_one():
    box = {"O"}
    warehouse = {(x, y): item for y, line in enumerate(warehouse_map) for x, item in enumerate(line)}
    explore_warehouse(warehouse, box, p1_move_box)
    return sum(x + 100 * y for (x, y), item in warehouse.items() if item in box)

def find_neighbor_box(wh, x, y):
    return (x+1, y) if wh[(x, y)] == '[' else (x-1, y)

def p2_move_box(wh, x, y, dx, dy, b):
    if directions["<"] == (dx, dy) or directions[">"] == (dx, dy):
        return p1_move_box(wh, x, y, dx, dy, b)

    steps = 1
    boxes = { (x, y), find_neighbor_box(wh, x, y) }
    history = []
    while True:
        history.append(frozenset(boxes))
        items = set((i+dx, j+dy) for i, j in boxes)
        if any(wh[i, j] == WALL for i, j in items):
            return False
        if all(wh[i, j] == '.' for i, j in items):
            break
        updated_boxes = set()
        for i, j in items:
            if wh[(i, j)] in b:
                updated_boxes.add((i, j))
                updated_boxes.add(find_neighbor_box(wh, i, j))
        boxes = updated_boxes
        steps += 1

    for bxs in history[::-1]:
        for (bx, by) in bxs:
            wh[(bx+dx, by+dy)], wh[(bx, by)] = wh[(bx, by)], '.'
    return True

# Predict the motion of the robot and boxes in this new, scaled-up warehouse. What is the sum of all boxes' final GPS
# coordinates?
def puzzle_two():
    def expand_line(s):
        mapping = { "O": "[]", WALL: "##", '.': "..", "@": "@." }
        return ''.join(mapping.get(c, "@.") for c in s)

    box = {"[", "]"}
    warehouse = {(x, y): i for y, l in enumerate([expand_line(l) for l in warehouse_map]) for x, i in enumerate(l)}
    explore_warehouse(warehouse, box, p2_move_box)
    return sum(x + 100 * y for (x, y), item in warehouse.items() if item == '[')


print(f'puzzle one solution={puzzle_one()}')
print(f'puzzle two solution={puzzle_two()}')