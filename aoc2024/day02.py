import os

with open(os.getcwd() + '\\resources\\day2.txt', 'r') as file:
    reports = [list(map(int, line.split())) for line in file]

# Checks if a report is safe. Criteria for safety is:
#  1) The list is in either non-descending or descending order
#  2) Each element is different from its neighbors by at least one and at most 3
def report_safe(report):
    return all(
        (report[i - 1] < report[i] if (report[0] < report[1]) else report[i - 1] > report[i]) and
        1 <= abs(report[i - 1] - report[i]) <= 3
        for i in range(1, len(report))
    )

# Puzzle one is checking to see how many reports are safe in the list of reports
def puzzle_one():
    return sum(report_safe(report) for report in reports)

# Puzzle two is checking to see how many reports are safe in the list of reports taking into account
# the 'Problem Dampener' that allows for the removal of one bad level
def puzzle_two():
    return sum(
        any(report_safe(report) + report_safe(report[:i] + report[i + 1:]) for i in range(len(report)))
        for report in reports
    )

print(f"Puzzle one solution = {puzzle_one()}")
print(f"Puzzle two solution = {puzzle_two()}")