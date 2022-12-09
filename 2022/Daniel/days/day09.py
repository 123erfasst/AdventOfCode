from typing import Iterator
from enum import Enum, unique

from . import helper

@unique
class Direction(Enum):
    up = 0
    down = 1
    left = 2
    right = 3

def parse_input(file: Iterator[str]) -> Iterator[tuple[Direction, int]]:
    for line in file:
        splits = line.split(' ')
        number: int = int(splits[1])
        match splits[0]:
            case 'U':
                yield (Direction.up, number)
            case 'D':
                yield (Direction.down, number)
            case 'L':
                yield (Direction.left, number)
            case 'R':
                yield (Direction.right, number)

def follow_point(h_pos: tuple[int, int], t_pos: tuple[int, int]) -> tuple[int, int]:
    x_diff = h_pos[0] - t_pos[0]
    y_diff = h_pos[1] - t_pos[1]

    x_bias = 1 if x_diff > 0 else -1
    y_bias = 1 if y_diff > 0 else -1

    if x_diff == 0:
        x_bias = 0
    if y_diff == 0:
        y_bias = 0

    if abs(x_diff) <= 1 and abs(y_diff) <= 1:
        return t_pos
    elif (abs(x_diff) == 2 and abs(y_diff) == 0) or (abs(y_diff) == 2 and abs(x_diff) == 0):
        return (t_pos[0] + x_bias, t_pos[1] + y_bias)

    return (t_pos[0] + x_bias, t_pos[1] + y_bias)

def do_step(direction: Direction, points: list[tuple[int, int]]) -> list[tuple[tuple[int, int]]]:
    h_pos = points[0]
    match direction:
        case Direction.up:
            h_pos = (h_pos[0], h_pos[1] + 1)
        case Direction.down:
            h_pos = (h_pos[0], h_pos[1] - 1)
        case Direction.left:
            h_pos = (h_pos[0] - 1, h_pos[1])
        case Direction.right:
            h_pos = (h_pos[0] + 1, h_pos[1])

    result: list[tuple[int, int]] = [h_pos]

    for item in points[1:]:
        result.append(follow_point(result[len(result) - 1], item))

    return result

def do_all_steps(input: Iterator[tuple[Direction, int]], count: int) -> Iterator[list[tuple[int, int]]]:
    current_points: list[tuple[int, int]] = []

    for i in range(count):
        current_points.append((0, 0))

    for item in input:
        for i in range(item[1]):
            current_points = do_step(item[0], current_points)
            yield current_points

def run():
    # Round 1
    print("Round 1:")

    file: Iterator[str] = helper.load_file("input/day09.txt")

    input: Iterator[tuple[Direction, int]] = parse_input(file)

    steps: Iterator[list[tuple[int, int]]] = do_all_steps(input, 2)

    result: set = set([(0, 0)])

    for item in steps:
        result.add(item[1])

    print(f"Number of T positions: {len(result)}")

    
    # Round 2
    print("Round 2:")

    file: Iterator[str] = helper.load_file("input/day09.txt")

    input: Iterator[tuple[Direction, int]] = parse_input(file)

    steps: Iterator[tuple[int, int]] = do_all_steps(input, 10)

    result: set = set([(0, 0)])

    for item in steps:
        result.add(item[9])
        print(item)

    print(f"Number of <9> positions: {len(result)}")

