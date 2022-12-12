from typing import Iterator
from enum import Enum, unique

from . import helper

@unique
class RPS(Enum):
    none = 0
    rock = 1
    paper = 2
    scissors = 3

class WLD(Enum):
    win = 6
    lose = 0
    draw = 3

def parse_file_direct(file: Iterator[str]) -> Iterator[tuple[RPS, RPS]]:
    wrapper: dict = {
        "A": RPS.rock,
        "B": RPS.paper,
        "C": RPS.scissors,
        "X": RPS.rock,
        "Y": RPS.paper,
        "Z": RPS.scissors
    }
    for line in file:
        items: list[str] = line.split()
        yield (wrapper[items[0]] , wrapper[items[1]])

def get_points_direct(match: tuple[RPS, RPS]) -> int:
    wrapper: dict = {
        (RPS.rock,     RPS.rock) : WLD.draw,
        (RPS.paper,    RPS.paper) : WLD.draw,
        (RPS.scissors, RPS.scissors) : WLD.draw,

        (RPS.rock,     RPS.paper) : WLD.win,
        (RPS.paper,    RPS.scissors) : WLD.win,
        (RPS.scissors, RPS.rock) : WLD.win,

        (RPS.paper,    RPS.rock) : WLD.lose,
        (RPS.rock,     RPS.scissors) : WLD.lose,
        (RPS.scissors, RPS.paper) : WLD.lose,
    }
    
    return match[1].value + wrapper[match].value

def parse_file_result(file: Iterator[str]) -> Iterator[tuple[RPS, WLD]]:
    wrapper: dict = {
        "A": RPS.rock,
        "B": RPS.paper,
        "C": RPS.scissors,
        "X": WLD.lose,
        "Y": WLD.draw,
        "Z": WLD.win
    }
    for line in file:
        items: list[str] = line.split()
        yield (wrapper[items[0]] , wrapper[items[1]])

def get_points_result(match: tuple[RPS, WLD]) -> int:
    wrapper: dict = {
        (RPS.rock,     WLD.draw) : RPS.rock,
        (RPS.paper,    WLD.draw) : RPS.paper,
        (RPS.scissors, WLD.draw) : RPS.scissors,

        (RPS.rock,     WLD.win) : RPS.paper,
        (RPS.paper,    WLD.win) : RPS.scissors,
        (RPS.scissors, WLD.win) : RPS.rock,

        (RPS.paper,    WLD.lose) : RPS.rock,
        (RPS.rock,     WLD.lose) : RPS.scissors,
        (RPS.scissors, WLD.lose) : RPS.paper,
    }
    
    return match[1].value + wrapper[match].value

def run():
    # Round 1

    file: Iterator[str] = helper.load_file("input/day02.txt")

    matches: Iterator[tuple[RPS, RPS]] = parse_file_direct(file)

    sum: int = 0
    for match in matches:
        sum += get_points_direct(match)

    print(f"Points Round 1: {sum}")

    # Round 2

    file: Iterator[str] = helper.load_file("input/day02.txt")

    matches: Iterator[tuple[RPS, WLD]] = parse_file_result(file)
    
    sum: int = 0
    for match in matches:
        sum += get_points_result(match)

    print(f"Points Round 2: {sum}")
