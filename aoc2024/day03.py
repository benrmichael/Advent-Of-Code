import os
import re

with open(os.getcwd() + '\\resources\\day3.txt', 'r') as file:
    mem = ''.join([line for line in file])

mul_instruction = r"mul\((\d{1,3}),(\d{1,3})\)"
dont_instruction = "don't()"
do_instruction = "do()"


# Puzzle one is finding each of the mul operations buried in each line of corrupted memory, then summing the result
# of all those operations
def puzzle_one(str = mem):
    return sum(int(m.group(1)) * int(m.group(2)) for m in re.finditer(mul_instruction, str))


# Puzzle two is much like the first, except now there are 'do()' and 'don't()' instructions that either enable or
# disable mul operations
def puzzle_two():
    clean = mem
    while dont_instruction in clean:
        next_dont = clean.find(dont_instruction)
        next_do = clean[next_dont + len(dont_instruction):].find(do_instruction)
        end = next_do if next_do == -1 else next_dont + len(dont_instruction) + next_do + len(do_instruction)
        clean = clean[:next_dont] + clean[end:]

    return puzzle_one(clean)


print(f"Puzzle one solution = {puzzle_one()}")
print(f"Puzzle two solution = {puzzle_two()}")
