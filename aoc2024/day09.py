import os
from itertools import chain
from itertools import count

DOT = "."

class MemChunk:
    def __init__(self, val: str, n: int, free_space=False):
        self.free_space = free_space
        self.val = val
        self.n = n
    def __repr__(self):
        return self.__str__()
    def __str__(self):
        return f"Chunk(free_space={self.free_space}, val={self.val},n={self.n})"
    @staticmethod
    def free_chunk(n):
        return MemChunk(DOT, n, True)
    def chunk(self) -> list[str]:
        return [str(self.val)] * self.n
    def free(self) -> bool:
        return self.free_space
    def size(self) -> int:
        return self.n


file_id = count(0)
with open(os.path.join(os.getcwd(), 'resources', 'day9.txt')) as file:
    disk_map = list(map(int, file.readline().strip()))
mem_chunks = [
    MemChunk(str(next(file_id)), disk_map[k]) if k % 2 == 0 else MemChunk.free_chunk(disk_map[k])
    for k in range(len(disk_map))
]

# Find the check sum for a list of files - assumes the sorting is done already and that files just contains a list
# of valid (non-free) file blocks
def check_sum(files: list[str]) -> int:
    return sum(n * int(f) for n, f in enumerate(files) if f != DOT)

# Puzzle one is moving each free file from the end to the next free spot on the left
def puzzle_one():
    file_blocks = list(chain.from_iterable(mem.chunk() for mem in mem_chunks))
    l, r = 0, (len(file_blocks) - 1)
    while l <= r:
        if file_blocks[l] == DOT:
            file_blocks[l] = file_blocks[r]
            file_blocks[r] = DOT
            r -= 1
        else:
            l += 1

    return check_sum(file_blocks)

def puzzle_two():
    r = len(mem_chunks) - 1
    while r >= 0:
        if mem_chunks[r].free():
            r -= 1
            continue
        l = 0
        while l < r:
            if mem_chunks[l].free() and mem_chunks[l].size() >= mem_chunks[r].size():
                rem = mem_chunks[l].size() - mem_chunks[r].size()
                mem_chunks[l] = mem_chunks[r]
                mem_chunks[r] = MemChunk.free_chunk(mem_chunks[r].size())
                if rem != 0:
                    mem_chunks.insert(l + 1, MemChunk.free_chunk(rem))
                break
            l += 1
        r -= 1

    return check_sum(list(chain.from_iterable(mem.chunk() for mem in mem_chunks)))

print(f"puzzle one solution = {puzzle_one()}")
print(f"puzzle two solution = {puzzle_two()}")