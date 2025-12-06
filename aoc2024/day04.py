import os

with open(os.path.join(os.getcwd(), 'resources', 'day4.txt'), 'r') as file:
    lines = [line.strip().lower() for line in file]
    m = len(lines)
    n = len(lines[0])

# Puzzle one is finding all occurrences of the word 'xmas' in the crossword puzzle input (m x n char matrix) where
# xmas can be omnidirectional
def puzzle_one():
    xmas = "xmas"
    moves = [(1, 0), (-1, 0), (0, 1), (0, -1), (1, 1), (1, -1), (-1, 1), (-1, -1)]

    def search(r, c, index, movement):
        r = r + movement[0]
        c = c + movement[1]
        if not 0 <= r < m or not 0 <= c < n or not lines[r][c] == xmas[index]:
            return 0

        if lines[r][c] == xmas[-1]:
            return 1

        return search(r, c, index + 1, movement)

    return sum(
        sum(search(row, col, 1, mov) for mov in moves) if lines[row][col] == 'x' else 0
        for row in range(m) for col in range(n)
    )

# Puzzle two is finding all x-mas in the word. This means that mas is formed diagonally, and can be written forwards
# or backwards
def puzzle_two():
    mas = 'mas'

    def is_mas(r, c):
        if any(not 0 <= (r + k) < m or not 0 <= (c + k) < n for k in range(-1, 2)):
            return False

        d1 = lines[r-1][c-1] + 'a' + lines[r+1][c+1]
        d2 = lines[r-1][c+1] + 'a' + lines[r+1][c-1]

        if (d1 == mas or d1[::-1] == mas) and (d2 == mas or d2[::-1] == mas):
            return True

        return False

    return sum(is_mas(row, col) if lines[row][col] == 'a' else False for row in range(m) for col in range(n))


print(f"Puzzle one solution = {puzzle_one()}")
print(f"Puzzle two solution = {puzzle_two()}")


