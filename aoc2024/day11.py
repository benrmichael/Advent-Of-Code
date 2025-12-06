import os
from collections import defaultdict

with open(os.path.join(os.getcwd(), 'resources', 'day11.txt')) as file:
    physics_defying_stones = list(map(int, file.readline().strip().split()))

# Cache the know values of splitting stones
split_stones = defaultdict(lambda: defaultdict(int))

# Split a stone n times, return how many stones this action will yield
def split_stone(stone, n):
    if stone in split_stones and n in split_stones[stone]:
        return split_stones[stone][n]

    if n == 1:
        k = len(apply_rule(stone))
        split_stones[stone][n] = k
        return k
    else:
        k = sum(split_stone(s, n - 1) for s in apply_rule(stone))
        split_stones[stone][n] = k
        return k

# Apply a magical rule to a physics defying stone
def apply_rule(stone):
    if stone == 0:
        return [1]
    elif len(str(stone)) % 2 == 0:
        str_stone = str(stone)
        return [int(str_stone[:len(str_stone) // 2]), int(str_stone[len(str_stone) // 2:].lstrip('0') or '0')]
    else:
        return [stone * 2024]

# How many stones will you have after blinking 25 times?
def puzzle_one():
    return sum(split_stone(stone, 25) for stone in physics_defying_stones)

# How many stones will you have after blinking 75 times?
def puzzle_two():
    return sum(split_stone(stone, 75) for stone in physics_defying_stones)

print(f"puzzle one solution = {puzzle_one()}")
print(f"puzzle two solution = {puzzle_two()}")