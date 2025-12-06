import os

with open(os.path.join(os.getcwd(), 'resources', 'day12.txt')) as file:
    garden = {(x, y): plant for y, line in enumerate(file.read().splitlines()) for x, plant in enumerate(line)}

directions = [(1, 0), (0, 1), (-1, 0), (0, -1)]
fenced = set()

# Fence off an area - move around in each direction we can go if there is the same plant there and add it to the
# overall fenced region
def fence_area(plant, x, y, r):
    r.add((x, y))
    fenced.add((x, y))
    for dx, dy in directions:
        if (x+dx, y+dy) not in fenced and garden.get((x+dx, y+dy)) == plant:
            fence_area(plant, x+dx, y+dy, r)
    return r


puzzle_one = 0
puzzle_two = 0
for (x, y), plant in garden.items():
    if (x, y) in fenced:
        continue
    region = fence_area(plant, x, y, set())
    area = len(region)
    perimeter = set((x+dx, y+dy, dx, dy) for x,y in region for dx, dy in directions if (x+dx, y+dy) not in region)
    puzzle_one += area * len(perimeter)

    # for puzzle two only - remove any sides that are along the same line. do so by adding one for each iteration of
    # the perimeter, e.g. count the whole side as one, then go through all the sides in the perimeter and remove any
    # that are on the same path
    valid_sides = 0
    while perimeter:
        valid_sides += 1
        side_x, side_y, side_dx, side_dy = perimeter.pop()
        moves = {(side_dy, side_dx), (-side_dy, -side_dx)}
        #print(f"side_x={side_x}, side_y={side_y}, side_dx={side_dx}, side_dy={side_dy}")
        for dx, dy in moves:
            n_x, n_y = side_x+dx, side_y+dy
            #print(f"dx={dx}, n_x={n_x}, dy={dy}, n_y={n_y}")
            while (n_x, n_y, side_dx, side_dy) in perimeter:
                perimeter.remove((n_x, n_y, side_dx, side_dy))
                n_x += dx
                n_y += dy
    puzzle_two += area * valid_sides

print(f"puzzle one solution={puzzle_one}")
print(f"puzzle two solution={puzzle_two}")