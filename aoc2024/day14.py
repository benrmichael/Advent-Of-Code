import os
import re

robo_regex = r"p=(\d+),(\d+)\sv=(-?\d+),(-?\d+)"
robots = []
width = 101
height = 103

with open(os.path.join(os.getcwd(), 'resources', 'day14.txt')) as file:
    for line in file:
        r = re.match(robo_regex, line.strip())
        robots.append(((int(r.group(1)), (int(r.group(2)))), (int(r.group(3)), (int(r.group(4))))))


def calc_safety_factor(quads):
    safety_factor = 1
    for quad in quads:
        safety_factor *= quad
    return safety_factor


def determine_quadrant(pos):
    if pos[0] == width // 2 or pos[1] == height // 2:
        return None
    x = pos[0] < width // 2
    y = pos[1] < height // 2
    return 0 if y and not x else 1 if x and y else 2 if not y and x else 3


def move(t):
    quads = [0] * 4
    for robot in robots:
        moved = (robot[0][0] + t * robot[1][0]) % width, (robot[0][1] + t * robot[1][1]) % height
        quad = determine_quadrant(moved)
        if quad is not None:
            quads[quad] += 1
    return quads


# Predict the motion of the robots in your list within a space which is 101 tiles wide and 103 tiles tall.
# What will the safety factor be after exactly 100 seconds have elapsed?
def puzzle_one():
    quads = move(100)
    return calc_safety_factor(quads)


# During the bathroom break, someone notices that these robots seem awfully similar to ones built and used at the
# North Pole. If they're the same type of robots, they should have a hard-coded Easter egg: very rarely, most of the
# robots should arrange themselves into a picture of a Christmas tree. What is the fewest number of seconds that must
# elapse for the robots to display the Easter egg?
def puzzle_two():
    min_noise = -1
    min_t = 0
    for t in range(10000):
        quads = move(t)
        noise = calc_safety_factor(quads)
        if noise < min_noise or min_noise == -1:
            min_noise = noise
            min_t = t
    return min_t


print(f"puzzle one solution={puzzle_one()}")
print(f"puzzle two solution={puzzle_two()}")
