from typing import Iterator

from . import helper

def make_matrix(file: Iterator[str]) -> list[str]:
    result: list[str] = []
    for line in file:
        result.append(line)

    return result

def is_visible(matrix: list[str], row: int, column: int) -> bool:
    visible: int = 4
    for up in range(row - 1, -1, -1):
        if matrix[up][column] >= matrix[row][column]:
            visible -= 1
            break
    
    for down in range(row + 1, len(matrix), 1):
        if matrix[down][column] >= matrix[row][column]:
            visible -= 1
            break
    
    for left in range(column - 1, -1, -1):
        if matrix[row][left] >= matrix[row][column]:
            visible -= 1
            break
    
    for right in range(column + 1, len(matrix[0]), 1):
        if matrix[row][right] >= matrix[row][column]:
            visible -= 1
            break

    return visible != 0

def number_of_open_trees(matrix: list[str]) -> int:
    count: int = len(matrix) * 2 + len(matrix[0]) * 2 - 4
    for row in range(1, len(matrix) - 1):
        for column in range(1, len(matrix[0]) - 1):
            if is_visible(matrix, row, column):
                count += 1

    return count

def visible_tree_score(matrix: list[str], row: int, column: int) -> int:
    result: int = 1

    count: int = 0
    for up in range(row - 1, -1, -1):
        count += 1
        if matrix[up][column] >= matrix[row][column]:
            break

    result *= count
    count = 0
    
    for down in range(row + 1, len(matrix), 1):
        count += 1
        if matrix[down][column] >= matrix[row][column]:
            break

    result *= count
    count = 0
    
    for left in range(column - 1, -1, -1):
        count += 1
        if matrix[row][left] >= matrix[row][column]:
            break

    result *= count
    count = 0
    
    for right in range(column + 1, len(matrix[0]), 1):
        count += 1
        if matrix[row][right] >= matrix[row][column]:
            break

    result *= count
    return result

def visible_trees(matrix: list[str]) -> Iterator[int]:
    for row in range(1, len(matrix) - 1):
        for column in range(1, len(matrix[0]) - 1):
            yield visible_tree_score(matrix, row, column)



def run():
    # Round 1
    print("Round 1:")

    file: Iterator[str] = helper.load_file("input/day08.txt")

    matrix: list[str] = make_matrix(file)

    result: int = number_of_open_trees(matrix)

    print(f"Visible: {result}")
    
    # Round 2
    print("Round 2:")

    result: Iterator(int) = visible_trees(matrix)

    print(f"Best Score: {helper.max(result)}")
