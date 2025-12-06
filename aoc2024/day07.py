import os
import re
from typing import Callable, List
import operator

EQUATION_REGEX = r"(\d+):\s([\d\s]+)"
calibration_equations = {}

with open(os.path.join(os.getcwd(), 'resources', 'day7.txt')) as file:
    for line in file:
        line = line.strip()
        if (match := re.match(EQUATION_REGEX, line)) is not None:
            total = int(match.group(1))
            numbers = list(map(int, match.group(2).split()))
            calibration_equations[total] = numbers


# Calculates all the potential solutions that could be created from the list of values and the operators that can be
# used to calculate the solution
def potential_solutions(vals: list[int], ops: List[Callable[[int, int], int]]) -> set[int]:
    sols = {vals[0]}
    for n in vals[1:]:
        new_sols = set()
        for op in ops:
            new_sols.update(op(k, n) for k in sols)
        sols = new_sols
    return sols


# Sums the value of any equation that was able to be solved given the provided operators
def sum_sols_for_equations_using_ops(ops: List[Callable[[int, int], int]]):
    return sum(sol for sol, equation in calibration_equations.items() if sol in potential_solutions(equation, ops))


# Puzzle one is finding how many of the equations can be solved using either the '+' or '*' operator
def puzzle_one():
    ops = [
        operator.add,
        operator.mul
    ]
    return sum_sols_for_equations_using_ops(ops)


# Puzzle two is finding how many of the equations can be solved using either the '+', '*', or custom '||' operator
# which given two numbers (x,y) returns xy
def puzzle_two():
    ops = [
        operator.add,
        operator.mul,
        lambda x, y: int(str(x) + str(y))
    ]
    return sum_sols_for_equations_using_ops(ops)


print(f"puzzle one solution = {puzzle_one()}")
print(f"puzzle one solution = {puzzle_two()}")