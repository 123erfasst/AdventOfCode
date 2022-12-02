from typing import Optional

from cpl_core.console import Console
from cpl_core.utils import String
from cpl_query.enumerable import Enumerable
from cpl_query.extension import List
from cpl_core.pipes import *

from aoc.aoc import get_input
from day1 import aoc_input

# global vars
day = 2
aoc_input = get_input(2022, day)
rps = {'A': 'R', 'B': 'P', 'C': 'S', 'X': 'R', 'Y': 'P', 'Z': 'S'}
res = {"X": 1, "Y": 2, "Z": 3}


def get_score(opponent, me) -> int:
    if opponent == 'R':
        if me == 'R':
            return 3
        elif me == 'P':
            return 6
        elif me == 'S':
            return 0
    elif opponent == 'P':
        if me == 'R':
            return 0
        elif me == 'P':
            return 3
        elif me == 'S':
            return 6
    elif opponent == 'S':
        if me == 'R':
            return 6
        elif me == 'P':
            return 0
        elif me == 'S':
            return 3


def get_expected_score(opponent, self):
    if self == 'Y':
        to_me = {"A": "X", "B": "Y", "C": "Z"}
        return to_me[opponent]
    else:
        if opponent == 'A':
            if self == 'X':
                return 'Z'
            elif self == 'Z':
                return 'Y'
        elif opponent == 'B':
            if self == 'X':
                return 'X'
            elif self == 'Z':
                return 'Z'
        elif opponent == 'C':
            if self == 'X':
                return 'Y'
            elif self == 'Z':
                return 'X'


def part1(data) -> int:
    score = 0
    for x in data:
        opponent, me = x.split()

        score += res[me]
        score += get_score(rps[opponent], rps[me])

    return score


def part2(data):
    score = 0
    for x in data:
        opponent, me = x.split()

        e = get_expected_score(opponent, me)
        score += res[e]
        score += get_score(rps[opponent], rps[e])

    return score


if __name__ == '__main__':
    """
        Das ist so fckng dreckige Lösung...
        Warum bin ich um 6:30 aufgestanden...
        Ich hab kein bock mehr.
    """
    Console.write_line(f'Advent of code day {day}')
    Console.write_line(part1(aoc_input.splitlines()), part2(aoc_input.splitlines()))
    Console.write_line()
