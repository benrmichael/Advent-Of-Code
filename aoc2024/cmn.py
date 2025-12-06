"""
Adds two tuples together, summing their components and returning the result tuple
"""
def add_tuples(tup1, tup2):
    return tuple(map(sum, zip(tup1, tup2)))

"""
Convert a 2D tuple into a string representing it's coordinates
"""
def tuple_str(tup: tuple[int, int]) -> str:
    return f"{tup[0]}-{tup[1]}"

"""
Checks if a 2D tuple containing a point in a matrix is within some dimensions
"""
def in_bounds(point: tuple[int, int], dim: tuple[int, int]) -> bool:
    return 0 <= point[0] < dim[0] and 0 <= point[1] < dim[1]