from typing import Iterator
import re

from . import helper

def parse_groups(items: Iterator[str]) -> Iterator[tuple[range, range]]:
    for item in items:
        pair = re.split(',|-', item)
        yield (range(int(pair[0]), int(pair[1]) + 1), range(int(pair[2]), int(pair[3]) + 1))

def find_overlap(pairs: Iterator[tuple[range, range]]) -> Iterator[bool]:
    for item in pairs:
        yield is_overlapping(item)

def is_overlapping(item: tuple[range, range]) -> bool:
    bot_1: int = item[0].start
    top_1: int = item[0].stop - 1
    bot_2: int = item[1].start
    top_2: int = item[1].stop - 1

    return bot_1 <= bot_2 <= top_2 <= top_1 or bot_2 <= bot_1 <= top_1 <= top_2

def find_intersections(pairs: Iterator[tuple[range, range]]) -> Iterator[bool]:
    for item in pairs:
        yield is_intersecting(item)

def is_intersecting(item: tuple[range, range]) -> bool:
    bot_1: int = item[0].start
    top_1: int = item[0].stop - 1
    bot_2: int = item[1].start
    top_2: int = item[1].stop - 1

    return is_overlapping(item) or bot_1 <= bot_2 <= top_1 <= top_2 or bot_2 <= bot_1 <= top_2 <= top_1


def run():
    # Round 1

    file: Iterator[str] = helper.load_file("input/day4.txt")

    pairs: Iterator[tuple[range, range]] = parse_groups(file)

    overlapping: Iterator[bool] = find_overlap(pairs)

    print(f"Number of overlaps: {helper.count_true(overlapping)}")

    # Round 2

    file: Iterator[str] = helper.load_file("input/day4.txt")

    pairs: Iterator[tuple[range, range]] = parse_groups(file)

    intersecting: Iterator[bool] = find_intersections(pairs)

    print(f"Number of Intersections: {helper.count_true(intersecting)}")