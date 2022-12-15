import time
from dataclasses import dataclass
from typing import Optional

from cpl_core.console import Console
from cpl_core.utils import String
from cpl_query.enumerable import Enumerable
from cpl_query.extension import List
from cpl_core.pipes import *

from aoc.aoc import get_input

# global vars
day = 14
aoc_input = get_input(2022, day)
test_input = """498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9
"""


@dataclass
class Position:
    x: int = 0
    y: int = 0


def build_rocks(grid: List[List[str]], paths: List[List[Position]], index_map: List[int]):
    for path in paths:
        last_pos = None
        for pos in path:
            if last_pos is None:
                last_pos = pos
                continue

            x_bias = 1 if last_pos.x - pos.x > 0 else -1
            y_bias = 1 if last_pos.y - pos.y > 0 else -1
            vertical = List(int).extend(range(pos.y, last_pos.y + y_bias, y_bias))
            horizontal = List(int).extend(range(index_map.index_of(pos.x), index_map.index_of(last_pos.x) + x_bias, x_bias))

            for y in vertical:
                for x in horizontal:
                    grid[y][x] = '#'

            last_pos = pos


def draw(grid: List[List[Position]], pos: Position = None):
    Console.clear()
    draw_grid = grid.select(lambda x: x.copy()).copy()
    if pos is not None:
        draw_grid[pos.y][pos.x] = 'x'

    Console.write_line()
    i = 0
    for i, row in enumerate(draw_grid):
        Console.write_line(f'{i:03d}', ''.join(row))

    time.sleep(0.01)


def place_tile(grid: List[List[Position]], x: int, y: int):
    grid[y][x] = 'o'

    if grid[y - 1][x] == '.' and grid[y - 1][x + 1] != '#':
        grid[y - 1][x + 1] = '.'

    elif grid[y - 1][x + 1] == 'o':
        grid[y - 1][x + 1] = '.'

    if grid[y - 1][x] != '#':
        grid[y - 1][x] = '.'


def in_boundary(grid: List[List[Position]], x: int, y: int) -> bool:
    return 0 <= y < grid.count() and 0 <= x < grid.first().count()


def is_blocked(grid: List[List[Position]], x: int, y: int) -> bool:
    if not in_boundary(grid, x, y):
        return True

    return grid[y][x] == 'o' or grid[y][x] == '#'


def simulate_sand(grid: List[List[Position]], index_map: List[int]):
    start = Position(index_map.index_of(500), 0)
    x = start.x
    y = start.y
    count = 0

    can_continue = True
    while can_continue:
        # draw(grid, Position(x, y))

        # if grid[y + 1][x] == '.':
        #     y += 1
        #     continue

        if not is_blocked(grid, x, y + 1):
            y += 1

        elif not in_boundary(grid, x, y + 1):
            break

        elif not is_blocked(grid, x - 1, y + 1):
            x -= 1
            y += 1

        elif not in_boundary(grid, x - 1, y + 1):
            break

        elif not is_blocked(grid, x + 1, y + 1):
            x += 1
            y += 1

        elif not in_boundary(grid, x + 1, y + 1):
            break

        else:
            count += 1
            if x == start.x and y == start.y:
                break

            grid[y][x] = 'o'
            x = start.x
            y = start.y

    draw(grid)
    Console.write_line(count)


def main(text: str, part2=False):
    if part2:
        dx = 500
        text += f'{min_pos.x - dx},{max_pos.y + 2} -> {max_pos.x + dx},{max_pos.y + 2}'

    paths: List[List[Position]] = List(Position, text.splitlines()) \
        .select(lambda x: List(str, x.split(' -> ')).select(lambda y: Position(int(y.split(',')[0]), int(y.split(',')[1]))))

    paths_x = paths.select_many(lambda x: x)
    min_pos.x = paths_x.min(lambda x: x.x)
    min_pos.y = paths_x.min(lambda x: x.y)
    max_pos.x = paths_x.max(lambda x: x.x)
    max_pos.y = paths_x.max(lambda x: x.y)

    index_map = List.range(min_pos.x, paths_x.max(lambda x: x.x) + 1)
    grid = List.range(0, max_pos.y + (3 if part2 else 1)).select(
        lambda y: index_map.select(lambda x: '.'))

    grid[0][index_map.index_of(500)] = '+'

    build_rocks(grid, paths, index_map)
    # draw(grid)
    simulate_sand(grid, index_map)


if __name__ == '__main__':
    Console.write_line(f'Advent of code day {day}')
    max_pos = Position(0, 0)
    min_pos = Position(0, 0)
    # main(test_input)
    # main(test_input, True)
    main(aoc_input)
    main(aoc_input, True)
    Console.write_line()
