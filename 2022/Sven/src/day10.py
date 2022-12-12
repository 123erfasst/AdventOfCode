from typing import Optional

from cpl_core.console import Console
from cpl_core.utils import String
from cpl_query.enumerable import Enumerable
from cpl_query.extension import List
from cpl_core.pipes import *

from aoc.aoc import get_input

# global vars
day = 10
aoc_input = get_input(2022, day)
aoc_input1 = """noop
addx 3
addx -5
"""


def get_test_data():
    return """addx 15
addx -11
addx 6
addx -3
addx 5
addx -1
addx -8
addx 13
addx 4
noop
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx -35
addx 1
addx 24
addx -19
addx 1
addx 16
addx -11
noop
noop
addx 21
addx -15
noop
noop
addx -3
addx 9
addx 1
addx -3
addx 8
addx 1
addx 5
noop
noop
noop
noop
noop
addx -36
noop
addx 1
addx 7
noop
noop
noop
addx 2
addx 6
noop
noop
noop
noop
noop
addx 1
noop
noop
addx 7
addx 1
noop
addx -13
addx 13
addx 7
noop
addx 1
addx -33
noop
noop
noop
addx 2
noop
noop
noop
addx 8
noop
addx -1
addx 2
addx 1
noop
addx 17
addx -9
addx 1
addx 1
addx -3
addx 11
noop
noop
addx 1
noop
addx 1
noop
noop
addx -13
addx -19
addx 1
addx 3
addx 26
addx -30
addx 12
addx -1
addx 3
addx 1
noop
noop
noop
addx -9
addx 18
addx 1
addx 2
noop
noop
addx 9
noop
noop
noop
addx -1
addx 2
addx -37
addx 1
addx 3
noop
addx 15
addx -21
addx 22
addx -6
addx 1
noop
addx 2
addx 1
noop
addx -10
noop
noop
addx 20
addx 1
addx 2
addx 2
addx -6
addx -11
noop
noop
noop
"""


class CPU:
    x = 1
    cycles = 1
    signal_strength = 0

    position = 0

    @classmethod
    def cycle(cls, value: int = None):
        Console.write('█' if cls.position >= cls.x - 1 and cls.position <= cls.x + 1 else ' ')

        cls.position += 1
        if cls.position == 40:
            cls.position = 0
            Console.write_line()

        if (cls.cycles - 20) % 40 == 0:
            cls.signal_strength += cls.x * cls.cycles

        cls.cycles += 1

        if value is not None:
            cls.x += value


def main():
    ops = List(tuple)
    # aoc_input = get_test_data()
    Console.write_line()
    for line in aoc_input.splitlines():
        s_line = line.split()
        cmd = s_line[0]
        value = None
        if len(s_line) > 1:
            value = int(s_line[1])

        match cmd:
            case 'noop':
                CPU.cycle(None)
            case 'addx':
                CPU.cycle(None)
                CPU.cycle(value)

    Console.write_line()
    Console.write_line(CPU.cycles, CPU.x, CPU.signal_strength)


if __name__ == '__main__':
    Console.write_line(f'Advent of code day {day}')
    main()
    Console.write_line()
