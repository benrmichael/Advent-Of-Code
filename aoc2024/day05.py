import os
import re
from collections import defaultdict

rule_regex = r"(\d+)\|(\d+)"
rules = defaultdict(set)
pages = []
ordered = []
unordered = []

with open(os.path.join(os.getcwd(), 'resources', 'day5.txt')) as file:
    for line in file:
        if line.isspace():
            continue
        if (match := re.match(rule_regex, line)) is not None:
            rules[int(match.group(1))].add(int(match.group(2)))
        else:
            pages.append(list(map(int, line.split(","))))

# Checks if the element is currently in order, where 'i' is the index of the element in the page 'pg'
# If the element has no rules, it is by default in order
def in_order(i, pg):
    if pg[i] not in rules:
        return True

    return all(False if pg[k] in rules[pg[i]] else True for k in range(i - 1, -1, -1))

# Parse the pages and sort them into a set of the ordered and unordered ones
def pre_parse():
    for p in pages:
        if all(in_order(i, p) for i in range(len(p))):
            ordered.append(p)
        else:
            unordered.append(p)

# Puzzle one asks to find all the pages in order and then sum their middle elements
def puzzle_one(pgs=None):
    if pgs is None:
        pgs = ordered
    return sum(p[(len(p) - 1) // 2] for p in pgs)

# Puzzle two asks to put the unordered pages into order and then sum their middle elements
def puzzle_two():
    def put_in_order(index, page):
        while index >= 1 and not in_order(index, page):
            page[index], page[index -1 ] = page[index -1], page[index]
            index -= 1

    for page_not_in_order in unordered:
        for i in range(len(page_not_in_order)):
            put_in_order(i, page_not_in_order)

    return puzzle_one(unordered)


pre_parse()
print(f"Puzzle one solution = {puzzle_one()}")
print(f"Puzzle two solution = {puzzle_two()}")
