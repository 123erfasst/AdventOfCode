from __future__ import annotations

import math
import re
from dataclasses import dataclass
from typing import Union

from cpl_core.console import Console
from cpl_core.utils import String
from cpl_query.enumerable import Enumerable
from cpl_query.extension import List
from cpl_core.pipes import *

from aoc.aoc import get_input

# global vars
day = 15
aoc_input = get_input(2022, day)
test_input = """Sensor at x=2, y=18: closest beacon is at x=-2, y=15
Sensor at x=9, y=16: closest beacon is at x=10, y=16
Sensor at x=13, y=2: closest beacon is at x=15, y=3
Sensor at x=12, y=14: closest beacon is at x=10, y=16
Sensor at x=10, y=20: closest beacon is at x=10, y=16
Sensor at x=14, y=17: closest beacon is at x=10, y=16
Sensor at x=8, y=7: closest beacon is at x=2, y=10
Sensor at x=2, y=0: closest beacon is at x=2, y=10
Sensor at x=0, y=11: closest beacon is at x=2, y=10
Sensor at x=20, y=14: closest beacon is at x=25, y=17
Sensor at x=17, y=20: closest beacon is at x=21, y=22
Sensor at x=16, y=7: closest beacon is at x=15, y=3
Sensor at x=14, y=3: closest beacon is at x=15, y=3
Sensor at x=20, y=1: closest beacon is at x=15, y=3
"""


@dataclass
class Position:
    x: int
    y: int

    def manhattan_distance(self):
        return abs(self.x) + abs(self.y)

    def __sub__(self, other: Position):
        x = self.x - other.x
        y = self.y - other.y
        return Position(x, y)


def find_blocked_in_row(index: int, elements: List(tuple[Position, Position, Position])):
    result = set()
    for sensor, beacon, diff in elements:
        dist = diff.manhattan_distance()
        offset = abs(index - sensor.y)
        if offset > dist:
            continue

        dx = dist - offset
        t = list(range(sensor.x - dx, sensor.x + dx + 1))
        if beacon.y == index and beacon.x in t:
            t.remove(beacon.x)

        result.update(set(t))

    return result


def parse(text: str) -> List[tuple[Position, Position, Position]]:
    elements = List(tuple)
    for line in text.splitlines():
        found = re.findall(r'=-?\d+', line)
        sensor = Position(int(found[0][1:]), int(found[1][1:]))
        beacon = Position(int(found[2][1:]), int(found[3][1:]))
        diff = sensor - beacon
        elements.add((sensor, beacon, diff))

    return elements


def part1(text: str, index: int):
    elements = parse(text)
    result = find_blocked_in_row(index, elements)
    Console.write_line(len(result))


if __name__ == '__main__':
    Console.write_line(f'Advent of code day {day}')
    part1(test_input, 10)
    part1(aoc_input, 2_000_000)
    # part2(test_input, 20)
    # part2(aoc_input, 4_000_000)
    Console.write_line()
