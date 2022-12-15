from __future__ import annotations
from dataclasses import dataclass, field
import re
from typing import Iterator
from enum import Enum, unique
import math

from . import helper

@unique
class Block(Enum):
    sensor = 0
    beacon = 1
    nothing = 2

@dataclass
class Vector2D:
    x: int
    y: int

    @classmethod
    def from_tuple(cls: Vector2D, point: tuple[int, int]) -> Vector2D:
        return Vector2D(point[0], point[1])

    def __sub__(self: Vector2D, other: Vector2D) -> Vector2D:
        x = self.x - other.x
        y = self.y - other.y
        return Vector2D(x, y)

    def __add__(self: Vector2D, other: Vector2D) -> Vector2D:
        x = self.x + other.x
        y = self.y + other.y
        return Vector2D(x, y)

    def manhattan_distance(self: Vector2D):
        return abs(self.x) + abs(self.y)

    def __hash__(self: Vector2D) -> int:
        return hash((self.x, self.y))

    def __eq__(self: Vector2D, other: Vector2D) -> bool:
        if isinstance(other, self.__class__):
            return self.x == other.x and self.y == other.x
        else:
            return False


@dataclass
class Graph:
    sensors: list[tuple[Vector2D, Vector2D, Vector2D]] = field(default_factory=list)
    min: Vector2D = None
    max: Vector2D = None

    def update_max_min(self: Graph, vector: Vector2D):
        if self.min is None or self.max is None:
            self.min = Vector2D(vector.x, vector.y)
            self.max = Vector2D(vector.x, vector.y)

        if self.min.x > vector.x:
            self.min.x = vector.x
        if self.min.y > vector.y:
            self.min.y = vector.y

        if self.max.x < vector.x:
            self.max.x = vector.x
        if self.max.y < vector.y:
            self.max.y = vector.y

    def append(self: Graph, sensor: Vector2D, beacon: Vector2D):
        self.sensors.append((sensor, beacon, beacon - sensor))
        self.update_max_min(sensor)
        self.update_max_min(beacon)

    def draw(self: Graph):
        grid: list[list[str]] = []
        y_offset = self.min.y * -1
        x_offset = self.min.x * -1

        for y in range(self.min.y + y_offset, self.max.y + y_offset + 1):
            grid.append([])
            for x in range(self.min.x + x_offset, self.max.x + x_offset + 1):
                grid[y].append('.')

        for sensor, beacon, diff in self.sensors:
            grid[sensor.y][sensor.x] = 'S'
            grid[beacon.y][beacon.x] = 'B'

        for y in range(len(grid)):
            for x in range(len(grid[0])):
                print(grid[y][x], end='')
            print('')

    @staticmethod
    def is_relevant(sensor: Vector2D, diff: Vector2D, target_y: int):
        y_offset = abs(target_y - sensor.y)
        return y_offset <= diff.manhattan_distance()

    def find_blocked_in_row(self: Graph, row: int, remove_beacons: bool = True) -> set[Vector2D]:
        result: list[Vector2D] = []
        for sensor, beacon, diff in self.sensors:
            if not Graph.is_relevant(sensor, diff, row):
                continue
            y_bias = 1 if sensor.y < row else -1
            number_of_points = abs(sensor.y + (diff.manhattan_distance() * y_bias) - row) * 2 + 1
            radius = int(number_of_points / 2)

            for x in range(sensor.x - radius, sensor.x + radius + 1):
                result.append(Vector2D(x, row))

        if remove_beacons:
            for sensor, beacon, diff in self.sensors:
                if beacon.y == row:
                    result.remove(Vector2D(beacon.x, row))

        return set(result)

    def get_row_blocked(self: Graph, row: int) -> dict[int: int]:
        result: list = []
        for sensor, beacon, diff in self.sensors:
            offset = abs(row - sensor.y)
            dist = diff.manhattan_distance()

            if offset <= dist:
                result.append((sensor.x - (dist - offset), sensor.x + (dist - offset)))
        
        return result

    def get_free_by_row(self: Graph, p_min: Vector2D, p_max: Vector2D) -> Iterator[int, list]:
        for y in range(p_min.y, p_max.y + 1):
            row_intervals = self.get_row_blocked(y)
            row_intervals = merge_intervals([(p_min.x, p_min.y)] + row_intervals + [(p_max.x, p_max.y)])

            if len(row_intervals) > 1:
                yield y, row_intervals

def parse_input(file: Iterator[str]) -> Graph:
    graph = Graph()
    for line in file:
        found = re.findall(r'=-?[0-9]+', line)
        sensor = Vector2D(int(found[0][1:]), int(found[1][1:]))
        beacon = Vector2D(int(found[2][1:]), int(found[3][1:]))
        graph.append(sensor, beacon)

    return graph

def is_touching(a: tuple[int, int], b: tuple[int, int]):
    if b[0] >= a[0] and b[0] <= a[1] + 1:
        return True
    else:
        return False

def merge_intervals(intervals: list[tuple[int, int]]):
    intervals.sort()
    result = [intervals[0]]
    for i in range(1, len(intervals)):
        last = result.pop()
        #current = intervals[i]

        if is_touching(last, intervals[i]):
            new_element = last[0], max(last[1], intervals[i][1])
            result.append(new_element)
        else:
            result.append(last)
            result.append(intervals[i])

    return result


def run():
    # Round 1
    print("Round 1:")

    file: Iterator[str] = helper.load_file("input/day15.txt")

    graph = parse_input(file)

    result = graph.find_blocked_in_row(2000000)
    print(f"Number of blocked spaces: {len(result)}")
    
    # Round 2
    print("Round 2:")

    p_min = Vector2D(0, 0)
    #p_max = Vector2D(20, 20)
    p_max = Vector2D(4000000, 4000000)

    results = graph.get_free_by_row(p_min, p_max)

    for item in results:
        vector = Vector2D(item[1][0][1] + 1, item[0])
        print(vector)
        score = vector.x * 4000000 + vector.y
        print(f"Score: {score}")
