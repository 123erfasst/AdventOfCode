from __future__ import annotations
from dataclasses import dataclass, field
from typing import Iterator
from enum import Enum, unique

from . import helper

@dataclass
class Vector2D:
    x: int
    y: int

    @classmethod
    def from_tuple(cls: Vector2D, point: tuple[int, int]) -> Vector2D:
        return Vector2D(point[0], point[1])

@unique
class Block(Enum):
    air = 0
    void = 1
    rock = 2
    spawner = 3
    sand = 4

    def char(self) -> str:
        match self:
            case Block.air:
                return '.'
            case Block.void:
                return '_'
            case Block.rock:
                return '#'
            case Block.spawner:
                return '+'
            case Block.sand:
                return 'o'

@dataclass
class Grid:
    grid: list[list[Block]]

    def __getitem__(self, key) -> list[Block]:
        return self.grid[key]

    def __str__(self: Grid) -> str:
        result = ""
        for y in range(len(self.grid[0])):
            for x in range(len(self.grid)):
                result += self.grid[x][y].char()
            result += "\n"

        return result

    @classmethod
    def from_list(self: Grid, collection: list[list[Vector2D]], minimum: Vector2D, maximum: Vector2D):
        grid = []
        x_offset = minimum.x - 1
        y_offset = 0

        x_max = maximum.x - minimum.x + 3
        y_max = maximum.y + 2

        for x in range(0, x_max):
            grid.append([])
            for y in range(0, y_max):
                if x == 0 or x == x_max - 1 or y == y_max - 1:
                    grid[x].append(Block.void)
                else:
                    grid[x].append(Block.air)

        grid[500 - x_offset][0 - y_offset] = Block.spawner

        for item in collection:
            last = item[0]
            for vector in item[1:]:
                draw_line(grid, last, vector, x_offset, y_offset)
                last = vector

        return Grid(grid)

def draw_line(grid: list[list[Block]], start: Vector2D, end: Vector2D, x_offset: int, y_offset: int):
    if start.x - end.x != 0:
        step = 1 if start.x - end.x < 0 else -1
        for x in range(start.x, end.x + step, step):
            grid[x - x_offset][start.y - y_offset] = Block.rock
    elif start.y - end.y != 0:
        step = 1 if start.y - end.y < 0 else -1
        for y in range(start.y, end.y + step, step):
            grid[start.x - x_offset][y - y_offset] = Block.rock

def parse_input(file: Iterator[str]) -> Iterator[list[Vector2D]]:
    for line in file:
        yield list(Vector2D.from_tuple(tuple(int(x.strip()) for x in item.split(','))) for item in line.split('->'))

def find_min_max(items: Iterator[list[Vector2D]]) -> tuple[Vector2D, Vector2D]:
    minimum = Vector2D(-1, -1)
    maximum = Vector2D(-1, -1)

    for item in items:
        for point in item:
            if minimum.x == -1 or minimum.x > point.x:
                minimum.x = point.x
            if minimum.y == -1 or minimum.y > point.y:
                minimum.y = point.y
            if maximum.x == -1 or maximum.x < point.x:
                maximum.x = point.x
            if maximum.y == -1 or maximum.y < point.y:
                maximum.y = point.y

    return minimum, maximum

def simulate_sand(grid: Grid) -> int:
    sand_start_pos = Vector2D(list(item[0] for item in grid.grid).index(Block.spawner), 0)
    sand_pos = sand_start_pos
    counter = 0

    while True:
        if grid[sand_pos.x][sand_pos.y + 1] == Block.air:
            sand_pos = Vector2D(sand_pos.x, sand_pos.y + 1)
        elif grid[sand_pos.x][sand_pos.y + 1] == Block.void:
            break
        elif grid[sand_pos.x - 1][sand_pos.y + 1] == Block.air:
            sand_pos = Vector2D(sand_pos.x - 1, sand_pos.y + 1)
        elif grid[sand_pos.x - 1][sand_pos.y + 1] == Block.void:
            break
        elif grid[sand_pos.x + 1][sand_pos.y + 1] == Block.air:
            sand_pos = Vector2D(sand_pos.x + 1, sand_pos.y + 1)
        elif grid[sand_pos.x + 1][sand_pos.y + 1] == Block.void:
            break
        else:
            counter += 1
            if sand_pos == sand_start_pos:
                break
            grid[sand_pos.x][sand_pos.y] = Block.sand
            sand_pos = sand_start_pos

    return counter


    


def run():
    # Round 1
    print("Round 1:")

    file: Iterator[str] = helper.load_file("input/day14.txt")

    parsed: list[list[Vector2D]] = list(parse_input(file))

    minimum, maximum = find_min_max(iter(parsed))

    grid = Grid.from_list(parsed, minimum, maximum)
    #print(grid)
    counter = simulate_sand(grid)
    #print(grid)
    print(f"Number of sand: {counter}")

    
    # Round 2
    print("Round 2:")

    parsed.append([Vector2D(0, maximum.y + 2), Vector2D(1000, maximum.y + 2)])

    grid = Grid.from_list(parsed, Vector2D(0, maximum.y + 2), Vector2D(1000, maximum.y + 2))
    counter = simulate_sand(grid)
    print(f"Number of sand: {counter}")
