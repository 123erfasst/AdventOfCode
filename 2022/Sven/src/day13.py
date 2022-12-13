import json
from functools import cmp_to_key
from math import prod
from typing import Union

from cpl_core.console import Console
from cpl_query.extension import List

from aoc.aoc import get_input

# global vars
day = 13
aoc_input = get_input(2022, day)
test_input = """[1,1,3,1,1]
[1,1,5,1,1]

[[1],[2,3,4]]
[[1],4]

[9]
[[8,7,6]]

[[4,4],4,4]
[[4,4],4,4,4]

[7,7,7,7]
[7,7,7]

[]
[3]

[[[]]]
[[]]

[1,[2,[3,[4,[5,6,7]]]],8,9]
[1,[2,[3,[4,[5,6,0]]]],8,9]
"""


def compare(left: Union[int, list], right: Union[int, list]):
    match left, right:
        case int(), list():
            return compare([left], right)
        case list(), int():
            return compare(left, [right])
        case int(), int():
            return left - right
        case list(), list():
            for i, j in zip(left, right):
                if (r := compare(i, j)) != 0:
                    return r
            return compare(len(left), len(right))


def main(lines=aoc_input, part2=False):
    lists: List[List[int]] = List(str, lines.splitlines()) \
        .select(lambda x: List(object, json.loads(x)) if x != '' else None) \
        .split(lambda x: None) \
        .select(lambda x: x.where(lambda y: y is not None))

    if not part2:
        lists: List[List[int]] = List(str, lines.splitlines()) \
            .select(lambda x: List(object, json.loads(x)) if x != '' else None) \
            .split(lambda x: None) \
            .select(lambda x: x.where(lambda y: y is not None))
        compared = lists \
            .where(lambda pair: compare(pair.element_at(0).to_list(), pair.element_at(1).to_list()) < 0) \
            .select(lambda x: lists.index_of(x) + 1)

        Console.write_line(compared.count(), compared.sum(lambda x: x))
        return

    two_six = List(object, [List(object, [List(object, [2])]), List(object, [List(object, [6])])])
    compared = lists.extend(two_six) \
        .select_many(lambda x: x.select(lambda y: y.to_list() if isinstance(x, List) else y).to_list() if isinstance(x, List) else x) \
        .order_by(cmp_to_key(compare))

    Console.write_line(prod(two_six.select_many(lambda x: x.select(lambda y: compared.index_of(y.to_list()) + 1))))


if __name__ == '__main__':
    Console.write_line(f'Advent of code day {day}')
    main()
    Console.write_line()
    main(part2=True)
    Console.write_line()
