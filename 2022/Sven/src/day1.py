from typing import Optional

from cpl_core.console import Console
from cpl_core.utils import String
from cpl_query.enumerable import Enumerable
from cpl_query.extension import List
from cpl_core.pipes import *

from aoc.aoc import get_input

# global vars
day = 1
aoc_input = get_input(2022, day)
elfs = List(int)


def part1() -> int:
    elf = 0
    for cal in aoc_input.splitlines():
        if cal == '':
            elfs.append(elf)
            elf = 0
            continue

        elf += int(cal)

    return elfs.max()


if __name__ == '__main__':
    Console.write_line(f'Advent of code day {day}')
    Console.write_line(f'Part 1: {part1()}')
    Console.write_line(f'Part 2: {elfs.order_by_descending().take(3).sum()}')
    Console.write_line()
