from typing import Iterator

from . import helper

def find_4_different(file: Iterator[str]) -> int:
    for line in file:
        for i in range(3, len(line)):
            chars: str = line[i-3:i+1]
            unique: set[str] = set(chars)
            if len(unique) == 4:
                return i

def find_14_different(file: Iterator[str]) -> int:
    for line in file:
        for i in range(13, len(line)):
            chars: str = line[i-13:i+1]
            unique: set[str] = set(chars)
            if len(unique) == 14:
                return i

def run():
    # Round 1
    print("Round 1:")

    file: Iterator[str] = helper.load_file("input/day06.txt")

    result: int = find_4_different(file)

    print(f"Block End: {result + 1}")

    # Round 2
    print("Round 2:")

    file: Iterator[str] = helper.load_file("input/day06.txt")

    result: int = find_14_different(file)

    print(f"Message Start: {result + 1}")
