from typing import Iterator
from enum import Enum, unique

from . import helper

@unique
class RockPaperScissors(Enum):
    none = 0
    rock = 1
    paper = 2
    scissors = 3

class WinLoseDraw(Enum):
    win = 6
    lose = 0
    draw = 3

def parse_file_direct(file: Iterator[str]) -> Iterator[tuple[RockPaperScissors, RockPaperScissors]]:
    wrapper: dict = {
        "A": RockPaperScissors.rock,
        "B": RockPaperScissors.paper,
        "C": RockPaperScissors.scissors,
        "X": RockPaperScissors.rock,
        "Y": RockPaperScissors.paper,
        "Z": RockPaperScissors.scissors
    }
    for line in file:
        items: list[str] = line.split()
        yield (wrapper[items[0]] , wrapper[items[1]])

def get_points_direct(match: tuple[RockPaperScissors, RockPaperScissors]) -> int:
    wrapper: dict = {
        (RockPaperScissors.rock, RockPaperScissors.rock) : WinLoseDraw.draw,
        (RockPaperScissors.paper, RockPaperScissors.paper) : WinLoseDraw.draw,
        (RockPaperScissors.scissors, RockPaperScissors.scissors) : WinLoseDraw.draw,

        (RockPaperScissors.rock, RockPaperScissors.paper) : WinLoseDraw.win,
        (RockPaperScissors.paper, RockPaperScissors.scissors) : WinLoseDraw.win,
        (RockPaperScissors.scissors, RockPaperScissors.rock) : WinLoseDraw.win,

        (RockPaperScissors.paper, RockPaperScissors.rock) : WinLoseDraw.lose,
        (RockPaperScissors.rock, RockPaperScissors.scissors) : WinLoseDraw.lose,
        (RockPaperScissors.scissors, RockPaperScissors.paper) : WinLoseDraw.lose,
    }
    
    return match[1].value + wrapper[match].value

def parse_file_result(file: Iterator[str]) -> Iterator[tuple[RockPaperScissors, WinLoseDraw]]:
    wrapper: dict = {
        "A": RockPaperScissors.rock,
        "B": RockPaperScissors.paper,
        "C": RockPaperScissors.scissors,
        "X": WinLoseDraw.lose,
        "Y": WinLoseDraw.draw,
        "Z": WinLoseDraw.win
    }
    for line in file:
        items: list[str] = line.split()
        yield (wrapper[items[0]] , wrapper[items[1]])

def get_points_result(match: tuple[RockPaperScissors, WinLoseDraw]) -> int:
    wrapper: dict = {
        (RockPaperScissors.rock, WinLoseDraw.draw) : RockPaperScissors.rock,
        (RockPaperScissors.paper, WinLoseDraw.draw) : RockPaperScissors.paper,
        (RockPaperScissors.scissors, WinLoseDraw.draw) : RockPaperScissors.scissors,

        (RockPaperScissors.rock, WinLoseDraw.win) : RockPaperScissors.paper,
        (RockPaperScissors.paper, WinLoseDraw.win) : RockPaperScissors.scissors,
        (RockPaperScissors.scissors, WinLoseDraw.win) : RockPaperScissors.rock,

        (RockPaperScissors.paper, WinLoseDraw.lose) : RockPaperScissors.rock,
        (RockPaperScissors.rock, WinLoseDraw.lose) : RockPaperScissors.scissors,
        (RockPaperScissors.scissors, WinLoseDraw.lose) : RockPaperScissors.paper,
    }
    
    return match[1].value + wrapper[match].value

def run():
    # Round 1

    file: Iterator[str] = helper.load_file("input/day2.txt")

    matches: Iterator[tuple[RockPaperScissors, RockPaperScissors]] = parse_file_direct(file)

    sum: int = 0
    for match in matches:
        sum += get_points_direct(match)

    print(f"Points Round 1: {sum}")

    # Round 2

    file: Iterator[str] = helper.load_file("input/day2.txt")

    matches: Iterator[tuple[RockPaperScissors, WinLoseDraw]] = parse_file_result(file)
    
    sum: int = 0
    for match in matches:
        sum += get_points_result(match)

    print(f"Points Round 2: {sum}")
