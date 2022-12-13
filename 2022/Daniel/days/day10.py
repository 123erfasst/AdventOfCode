from dataclasses import dataclass
from typing import Iterator
from enum import Enum, unique

from . import helper

@unique
class Operation(Enum):
    none = 0
    addx = 1

@dataclass
class Clock:
    operation: Operation
    duration: int = 1
    value: int = 0

    def do_clock(self, x: int) -> int:
        self.duration -= 1

        match self.operation:
            case Operation.none:
                return x
            case Operation.addx:
                if self.duration == 0:
                    return x + self.value
        
        return x


def parse_input(file: Iterator[str]) -> Iterator[Clock]:
    for line in file:
        if line == "noop":
            yield Clock(Operation.none)
        if line.startswith("addx"):
            yield Clock(Operation.addx, 2, int(line.split(' ')[1]))

def do_all_clocks(input: Iterator[Clock]) -> Iterator[int]:
    x: int = 1

    for item in input:
        while item.duration > 0:
            yield x
            x = item.do_clock(x)




def run():
    # Round 1
    print("Round 1:")

    file: Iterator[str] = helper.load_file("input/day10.txt")

    input: Iterator[tuple[Operation, int]] = parse_input(file)

    result: Iterator[int] = do_all_clocks(input)

    count_at = [20, 60, 100, 140, 180, 220]
    strength: int = 0

    counter: int = 0
    for item in result:
        counter += 1
        if any(item == counter for item in count_at):
            strength += counter * item
            print(f"{counter}: {item}")

    print(f"Strength: {strength}")


    # Round 2
    print("Round 2:")

    file: Iterator[str] = helper.load_file("input/day10.txt")

    input: Iterator[tuple[Operation, int]] = parse_input(file)

    result: Iterator[int] = do_all_clocks(input)

    counter: int = 0
    for item in result:
        if item - 1 <= counter <= item + 1:
            print('█', end='')
        else:
            print(' ', end='')
        
        counter += 1
        
        if counter == 40:
            counter = 0
            print("")