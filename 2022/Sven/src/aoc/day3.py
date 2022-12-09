import string
from typing import Optional

from cpl_core.console import Console
from cpl_core.utils import String
from cpl_query.enumerable import Enumerable
from cpl_query.extension import List
from cpl_core.pipes import *

from aoc.aoc import get_input

# global vars
day = 3
aoc_input = get_input(2022, day)
# aoc_input = """vJrwpWtwJgWrhcsFMMfFFhFp
# jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
# PmmdzqPrVvPwwTWBwg
# wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
# ttgJtRGJQctTZtZT
# CrZsJsPPZsGzwwsLwLmpwMDw
# """

priorities = [*string.ascii_lowercase, *string.ascii_uppercase]


def part1() -> int:
    duplicates = {}
    for rucksack in aoc_input.splitlines():

        c1, c2 = rucksack[:len(rucksack) // 2], rucksack[len(rucksack) // 2:]

        r_duplicates = List(str, c1).select(lambda x: x if x in c2 else '').where(lambda x: x != '')
        if r_duplicates.first() not in duplicates:
            duplicates[r_duplicates.first()] = 0

        duplicates[r_duplicates.first()] += r_duplicates.select(lambda x: priorities.index(x) + 1).distinct().sum()

    return sum(duplicates.values())


def part2() -> int:
    groups = List(List)
    index = 0
    for rucksack in aoc_input.splitlines():
        if index == 0:
            groups.append(List(str))

        index += 1
        if index == 3:
            index = 0

        groups.last().append(rucksack)

    return groups.select(lambda g: List(str, ''.join(g)).distinct().where(lambda x: x in g[0] and x in g[1] and x in g[2]).single()).select(lambda x: priorities.index(x) + 1).sum()


if __name__ == '__main__':
    Console.write_line(f'Advent of code day {day}')
    Console.write_line(part1(), part2())
    Console.write_line()
