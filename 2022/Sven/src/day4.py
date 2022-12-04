from typing import Optional

from cpl_core.console import Console
from cpl_core.utils import String
from cpl_query.enumerable import Enumerable
from cpl_query.extension import List
from cpl_core.pipes import *

from aoc.aoc import get_input

# global vars
day = 4
aoc_input = get_input(2022, day)
# aoc_input = """2-4,6-8
# 2-3,4-5
# 5-7,7-9
# 2-8,3-7
# 6-6,4-6
# 2-6,4-8
# """


def part1() -> List[List]:
    groups = List(List)
    overlaps = 0
    for pairs in aoc_input.splitlines():
        pair1, pair2 = pairs.split(',')
        g1 = List(int).extend(range(int(pair1.split('-')[0]), int(pair1.split('-')[1]) + 1))
        g2 = List(int).extend(range(int(pair2.split('-')[0]), int(pair2.split('-')[1]) + 1))

        if set(g1).issubset(g2):
            overlaps += 1

        if set(g2).issubset(g1) or set(g1).issubset(g2):
            overlaps += 1

        groups.add(g1)
        groups.add(g2)

    Console.write_line(groups.count(), overlaps)
    return groups


def part2(groups: List[List]):
    pairs = List(List)

    pair = List(List)
    for i, group in enumerate(groups):
        pair.add(group)
        if i % 2 != 0:
            pairs.add(pair)
            pair = List(List)

    overlaps = 0
    for pair in pairs:
        pair: List = pair
        if pair[0].any(lambda x: x in pair[1]) or pair[1].any(lambda x: x in pair[0]):
            overlaps += 1

    Console.write_line(overlaps)


if __name__ == '__main__':
    Console.write_line(f'Advent of code day {day}')
    groups = part1()
    part2(groups)
    Console.write_line()
