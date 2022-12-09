from typing import Iterator

def load_file(filename: str) -> Iterator[str]:
    with open(filename, "r") as txt_file:
        for line in txt_file:
            yield line.replace('\n', '')

def sum(iter: Iterator[int]) -> int:
    sum: int = 0
    for item in iter:
        sum += item

    return sum

def count_true(iter: Iterator[bool]) -> int:
    counter: int = 0
    for item in iter:
        counter += int(item)

    return counter

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
