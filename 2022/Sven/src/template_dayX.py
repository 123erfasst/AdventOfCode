from typing import Optional

from cpl_core.console import Console
from cpl_core.utils import String
from cpl_query.enumerable import Enumerable
from cpl_query.extension import List
from cpl_core.pipes import *

from aoc.aoc import get_input

# global vars
day = 0
aoc_input = get_input(2022, day)


def main():
    Console.write_line('hello world!')


if __name__ == '__main__':
    Console.write_line(f'Advent of code day {day}')
    main()
    Console.write_line()
