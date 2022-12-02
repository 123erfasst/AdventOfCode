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

def max(iterator: Iterator[int]) -> int:
    ret: int = None
    try:
        ret = next(iterator)
    except StopIteration:
        return None
    for val in iterator:
        if val > ret:
            ret = val
    return ret

def run():
    file: Iterator[str] = helper.load_file("input/day1.txt")

    sums: list[int] = list(add_groups(file))

    print(f"Max: {max(iter(sums))}")

    sums.sort(reverse = True)
    print(f"Top three: {sum(sums[0:3])}")
