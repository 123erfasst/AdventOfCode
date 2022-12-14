from __future__ import annotations
from typing import Iterator
from functools import cmp_to_key

from . import helper

def parse_list_recursive(line: str, position: int = 1) -> tuple[list, int]:
    result = []
    i: int = position
    start_pos = position
    while i < len(line):
        if line[i] == ']':
            return result, i
        elif line[i] == '[':
            sub_list, i = parse_list_recursive(line, i + 1)
            result.append(sub_list)
        elif line[i] == ',':
            start_pos = i + 1
        elif line[i].isdigit():
            next_pos = line.find(',', i)
            if ']' in line[start_pos:next_pos]:
                next_pos = line.find(']', i)
            result.append(int(line[start_pos:next_pos]))


        i += 1

def parse_groups(file: Iterator[str]) -> Iterator[tuple[list[int | list], list[int | list]]]:
    current_group = []

    for line in file:
        if line == '':
            yield tuple(current_group)
            current_group = []
        elif line.startswith('['):
            current_group.append(parse_list_recursive(line)[0])

    yield tuple(current_group)

def compare_order_recursive(left: list[int | list], right: list[int | list]) -> bool:
    for i in range(len(left)):
        if len(right) <= i:
            return False

        l_item = left[i]
        r_item = right[i]

        if type(l_item) is list and type(r_item) is int:
            r_item = [r_item]
        elif type(r_item) is list and type(l_item) is int:
            l_item = [l_item]

        if type(l_item) is list and type(r_item) is list:
            result = compare_order_recursive(l_item, r_item)
            if result != None:
                return result

            if len(l_item) < len(r_item):
                return True
            elif len(l_item) > len(r_item):
                return False
        elif l_item < r_item:
            return True
        elif l_item > r_item:
            return False

def compare(left, right):
    result = compare_order_recursive(left, right)
    if result == True or result == None:
        return -1
    else:
        return 1



def run():
    # Round 1
    print("Round 1:")

    file: Iterator[str] = helper.load_file("input/day13.txt")

    groups: Iterator[tuple] = parse_groups(file)

    result: int = 0

    for i, item in enumerate(groups):
        comp = compare_order_recursive(item[0], item[1])
        if comp == True or comp == None:
            result += i + 1
        #print(f"#{i}: {compare}")
        #print(f"{item[0]}\n{item[1]}")

    print(f"Result: {result}")

    # Round 2
    print("Round 1:")

    file: Iterator[str] = helper.load_file("input/day13.txt")

    groups: Iterator[tuple] = parse_groups(file)

    packet_list = [[[2]], [[6]]]

    for item in groups:
        packet_list.append(item[0])
        packet_list.append(item[1])

    packet_list = sorted(packet_list, key=cmp_to_key(compare))

    result: int = 1

    for i, item in enumerate(packet_list):
        if item == [[2]] or item == [[6]]:
            result *= i + 1
        print(item)

    print(f"Result: {result}")
