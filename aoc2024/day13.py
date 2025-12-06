import os
import re

# Hope I can get a pink giraffe from this machine
class ClawMachine:
    def __init__(self, button_a, button_b, prize):
        self.button_a = button_a
        self.button_b = button_b
        self.prize = prize

    def adjust_prize(self, factor):
        self.prize = self.prize[0] + factor, self.prize[1] + factor

prize_reg = r"Prize: X=(\d+), Y=(\d+)"
button_reg = r"Button [A|B]: X\+(\d+), Y\+(\d+)"

claw_machines = []
with open(os.path.join(os.getcwd(), 'resources', 'day13.txt')) as file:
    lines = file.readlines()

    for i in range(0, len(lines), 4):
        b_a = tuple(map(int, re.match(button_reg, lines[i]).groups()))
        b_b = tuple(map(int, re.match(button_reg, lines[i + 1]).groups()))
        p = tuple(map(int, re.match(prize_reg, lines[i + 2]).groups()))
        claw_machines.append(ClawMachine(b_a, b_b, p))

# Cramer more like lamer! (jk)
def tokens_to_win(machine, limit=None):
    a, c = machine.button_a
    b, d = machine.button_b
    x, y = machine.prize

    determinant = a * d - b * c
    sol_x = (d * x - b * y) / determinant
    sol_y = (a * y - c * x) / determinant

    if not sol_x.is_integer() or not sol_y.is_integer():
        return 0

    if limit and (sol_x > limit or sol_y > limit):
        return 0

    return int(sol_x) * 3 + int(sol_y)

# What is the fewest tokens you would have to spend to win all possible prizes?
def puzzle_one():
    return sum(tokens_to_win(claw_machine, 100) for claw_machine in claw_machines)

# Due to a unit conversion error in your measurements, the position of every prize is actually 10000000000000 higher
# on both the X and Y axis!What is the fewest tokens you would have to spend to win all possible prizes?
def puzzle_two():
    for claw_machine in claw_machines:
        claw_machine.adjust_prize(10000000000000)
    return sum(tokens_to_win(claw_machine) for claw_machine in claw_machines)


print(f"puzzle one solution={puzzle_one()}")
print(f"puzzle two solution={puzzle_two()}")
