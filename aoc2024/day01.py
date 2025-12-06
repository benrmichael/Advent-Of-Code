import os

text_input = os.getcwd() + '\\resources\\day1.txt'

def parse_input() -> ([int], [int]):
    a = []
    b = []

    with open(text_input, 'r') as file:
        for line in file:
            ints = list(map(int, line.split()))
            a.append(ints[0])
            b.append(ints[1])

    return a, b

# Puzzle one consists of finding the difference between the smallest elements in each list
def puzzle_one(a: [int], b: [int]) -> int:
    a.sort(), b.sort()
    return sum([abs(x - y) for x, y in zip(a, b)])

# Puzzle two consists of calculating how many times a number in list one appears in list two, then multiplying the
# number by its count and summing that value for all numbers in list one
def puzzle_two(a: [int], b: [int]) -> int:
    return sum(num * b.count(num) for num in a)

# Run solutions
list_one, list_two = parse_input()
puzzle_one_answer = puzzle_one(list_one, list_two)
puzzle_two_answer = puzzle_two(list_one, list_two)

print(f"Puzzle one answer = {puzzle_one_answer}")
print(f"Puzzle two answer = {puzzle_two_answer}")