from dataclasses import dataclass
from typing import Optional

from cpl_core.console import Console
from cpl_core.utils import String
from cpl_query.enumerable import Enumerable
from cpl_query.extension import List
from cpl_core.pipes import *

from aoc.aoc import get_input

# global vars
day = 9
aoc_input = get_input(2022, day)
aoc_input1 = """R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20"""


@dataclass
class Position:
    x: int = 0
    y: int = 0

    def __repr__(self):
        return f'({self.x}, {self.y})'

    @property
    def as_tuple(self) -> tuple[int, int]:
        return self.x, self.y


def get_new_tail(head: Position, tail: Position) -> Position:
    diff = Position(head.x - tail.x, head.y - tail.y)
    bias = Position(0 if diff.x == 0 else int(diff.x / abs(diff.x)), 0 if diff.y == 0 else int(diff.y / abs(diff.y)))

    if abs(diff.x) <= 1 and abs(diff.y) <= 1:
        return tail

    return Position(tail.x + bias.x, tail.y + bias.y)


def part1():
    head = Position(0, 0)
    tail = Position(0, 0)
    start = Position(0, 0)

    steps = {tail.as_tuple}

    for line in aoc_input.splitlines():
        step, width = line.split()
        for i in range(0, int(width)):
            match step:
                case 'R':
                    head.x += 1

                case 'L':
                    head.x -= 1

                case 'U':
                    head.y += 1

                case 'D':
                    head.y -= 1

            tail = get_new_tail(head, tail)
            steps.add(tail.as_tuple)

    Console.write_line(start, head, tail)
    Console.write_line(len(steps), steps)


def part2():
    head = Position(0, 0)
    tails = List(int).range(0, 9).select(lambda x: Position(0, 0))

    steps = {tails.last().as_tuple}

    for line in aoc_input.splitlines():
        step, width = line.split()
        for i in range(0, int(width)):
            match step:
                case 'R':
                    head.x += 1

                case 'L':
                    head.x -= 1

                case 'U':
                    head.y += 1

                case 'D':
                    head.y -= 1

            last_t = head
            new_tails = List(Position)
            for t in tails.to_list():
                new_t = get_new_tail(last_t, t)
                new_tails.add(new_t)
                last_t = new_t

            tails = new_tails
            steps.add(tails.last().as_tuple)

    Console.write_line(head, tails.last())
    Console.write_line(len(steps), steps)


if __name__ == '__main__':
    Console.write_line(f'Advent of code day {day}')
    part1()
    part2()
    Console.write_line()
