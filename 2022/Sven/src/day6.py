from typing import Optional

from cpl_core.console import Console
from cpl_core.utils import String
from cpl_query.enumerable import Enumerable
from cpl_query.extension import List
from cpl_core.pipes import *

from aoc.aoc import get_input

# global vars
day = 6
aoc_input = get_input(2022, day)
# aoc_input = "mjqjpqmgbljsphdztnvjfqwrcgsmlb"


class Marker:

    def __init__(self, part2=False):
        self._chars = List(str)
        self._max_count = 14 if part2 else 4

    @property
    def chars(self) -> List[str]:
        return self._chars

    @property
    def is_valid(self) -> bool:
        return self._max_count == self._chars.count() == self._chars.distinct().count()

    def add_char(self, c: str):
        if self._chars.count() == self._max_count:
            self._chars.remove_at(0)

        self._chars.add(c)


def part1():
    marker = Marker()
    for i, c in enumerate(aoc_input):
        marker.add_char(c)
        if not marker.is_valid:
            continue

        Console.write_line(f'Found marker: {"".join(marker.chars)} at {i + 1}')
        break


def part2():
    marker = Marker(True)
    for i, c in enumerate(aoc_input):
        marker.add_char(c)
        if not marker.is_valid:
            continue

        Console.write_line(f'Found marker: {"".join(marker.chars)} at {i + 1}')
        break


if __name__ == '__main__':
    Console.write_line(f'Advent of code day {day}')
    part1()
    part2()
    Console.write_line()
