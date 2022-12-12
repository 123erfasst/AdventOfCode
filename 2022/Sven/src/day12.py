import collections
import string
from dataclasses import dataclass
from typing import Union

from cpl_core.console import Console
from cpl_query.extension import List

from aoc.aoc import get_input

# global vars
day = 12
aoc_input = get_input(2022, day)
aoc_input1 = """Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi"""


@dataclass
class Position:
    x: int = 0
    y: int = 0


def get_position(grid: List[List[str]], char: str) -> Position:
    y = grid.index_of(grid.where(lambda row: char in row).single())
    x = grid.element_at(y).index_of(char)
    return Position(x, y)


def get_path(grid: List[List[Union[str, int]]], start: Position, reverse=False):
    width = grid.first().count()
    height = grid.count()
    directions = [(0, 1), (0, -1), (1, 0), (-1, 0)]

    paths = collections.defaultdict(lambda: 10e15)
    paths[(start.y, start.x)] = 0
    should_search = [(start.x, start.y, 0)]
    while len(should_search) > 0:
        x, y, steps = should_search[0]
        should_search = should_search[1:]
        from_height = ord(grid[y][x])

        for (dx, dy) in directions:
            nx = x + dx
            ny = y + dy
            if nx < 0 or nx >= width or ny < 0 or ny >= height:
                continue

            to_height = ord(grid[ny][nx])

            if not reverse and not from_height - to_height >= -1:
                continue

            if reverse and not from_height - to_height <= 1:
                continue

            if paths[(ny, nx)] > steps + 1:
                paths[(ny, nx)] = steps + 1
                should_search.append((nx, ny, steps + 1))

    return paths


def main():
    grid = List(str, aoc_input.splitlines()).select(lambda x: List(int, x))
    Console.write_line(grid)
    start = get_position(grid, 'S')
    target = get_position(grid, 'E')
    grid: List[List[Union[str, int]]] = grid.select(lambda x: x.select(lambda y: 'a' if y == 'S' else 'z' if y == 'E' else y))
    Console.write_line(get_path(grid, start)[target.y, target.x])
    Console.write_line(min([dist for ((y, x), dist) in get_path(grid, target, True).items() if grid[y][x] == 'a']))


if __name__ == '__main__':
    Console.write_line(f'Advent of code day {day}')
    main()
    Console.write_line()
