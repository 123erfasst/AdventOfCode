from typing import Iterator

from . import helper

def add_groups(list: Iterator[str]) -> Iterator[int]:
    sum: int = 0
    for item in list:
        if not item:
            yield sum
            sum = 0
        else:
            sum += int(item)

def run():
    file: Iterator[str] = helper.load_file("input/day01.txt")

    sums: list[int] = list(add_groups(file))

    print(f"Max: {helper.max(iter(sums))}")

    sums.sort(reverse = True)
    print(f"Top three: {sum(sums[0:3])}")
