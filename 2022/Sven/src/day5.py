from typing import Optional

from cpl_core.console import Console
from cpl_core.utils import String
from cpl_query.enumerable import Enumerable
from cpl_query.extension import List
from cpl_core.pipes import *

from aoc.aoc import get_input

# global vars
day = 5
aoc_input = get_input(2022, day)
aoc_input1 = """    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2"""


def get_stacks() -> List[List[str]]:
    stacks = List(List)

    for line in aoc_input.splitlines():
        if line.startswith('move'):
            continue

        if stacks.count() == 0:
            for i in range(int(len(line) / 4 + 1)):
                stacks.append(List(str))

        for i in range(1, len(line), 4):
            x = line[i]
            if x.isnumeric() or x == ' ':
                continue

            stacks[int((i - 1) / 4)].append(x)

    return stacks.select(lambda s: s.reverse())


def get_moves() -> List[tuple]:
    moves = List(tuple)
    for line in aoc_input.splitlines():
        if not line.startswith('move'):
            continue

        move_str = line.split()
        moves.append((int(move_str[1]), int(move_str[3]), int(move_str[5])))

    return moves


def make_move(stacks: List[List[str]], moves: List[tuple], part_two=None):
    if moves.first_or_default() is None:
        return

    move = moves.first()

    if part_two is None:
        for i in range(move[0]):
            e = stacks[move[1] - 1].last()
            stacks[move[2] - 1].append(e)
            stacks[move[1] - 1].pop()
    else:
        elements = stacks[move[1] - 1].take_last(move[0])
        stacks[move[2] - 1].extend(elements)
        stacks[move[1] - 1] = stacks[move[1] - 1].skip_last(move[0])

    moves.remove(move)
    make_move(stacks, moves, part_two=part_two)


def part1():
    stacks = get_stacks()
    make_move(stacks, get_moves())
    Console.write_line('Part 1: ', ''.join(stacks.select(lambda s: s.last_or_default()).where(lambda x: x is not None).to_list()))


def part2():
    stacks = get_stacks()
    make_move(stacks, get_moves(), part_two=True)
    Console.write_line('Part 2: ', ''.join(stacks.select(lambda s: s.last_or_default()).where(lambda x: x is not None).to_list()))


if __name__ == '__main__':
    Console.write_line(f'Advent of code day {day}')
    part1()
    part2()
    Console.write_line()
