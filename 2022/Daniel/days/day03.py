from typing import Iterator

from . import helper

def split_rucksack(list: Iterator[str]) -> Iterator[tuple[str, str]]:
    for line in list:
        yield (line[:len(line)//2], line[len(line)//2:])

def find_matching(packages: Iterator[tuple[str, str]]) -> Iterator[str]:
    for item in packages:
        yield "".join(set(item[0]).intersection(item[1]))

def get_priority(chars: Iterator[str]) -> Iterator[int]:
    for item in chars:
        for char in item:
            yield get_priority_of_char(char)

def get_priority_of_char(char: str) -> int:
    ascii: int = ord(char)
    if (ascii >= 97 and ascii <= 123):
        return ascii - 96
    elif (ascii >= 65 and ascii <= 91):
        return ascii - 38
    return -100000000

def group_lines(list: Iterator[str]) -> Iterator[tuple[str, str, str]]:
    first: str = ""
    second: str = ""
    counter: int = 0

    for line in list:
        if (counter == 0):
            first = line
        elif (counter == 1):
            second = line
        else:
            yield (first, second, line)
            first = ""
            second = ""
            counter = -1
        counter += 1

def find_matching_3(packages: Iterator[tuple[str, str]]) -> Iterator[str]:
    for item in packages:
        yield "".join(set(item[0]).intersection(item[1]).intersection(item[2]))


def run():
    # Round 1

    file: Iterator[str] = helper.load_file("input/day03.txt")

    packages: Iterator[tuple[str, str]] = split_rucksack(file)

    matches: Iterator[str] = find_matching(packages)

    prios: Iterator[int] = get_priority(matches)

    print(f"Summe (Runde 1): {helper.sum(prios)}")

    # Round 2

    file: Iterator[str] = helper.load_file("input/day03.txt")

    groups: Iterator[tuple[str, str, str]] = group_lines(file)

    matches: Iterator[str] = find_matching_3(groups)

    prios: Iterator[int] = get_priority(matches)

    print(f"Summe (Runde 2): {helper.sum(prios)}")


