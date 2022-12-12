from __future__ import annotations
from dataclasses import dataclass, field
import math
from typing import Iterator

from . import helper


@dataclass
class Monkey:
    number: int
    items: list[int]
    operation: callable
    operation_second: int
    divisible_by: int
    test_true: int
    test_false: int
    monkey_true: Monkey = None
    monkey_false: Monkey = None
    inspect_counter: int = 0

    def do_turn(self, lcm: int, divide: bool = True):
        while len(self.items) > 0:
            self.inspect_counter += 1
            item = self.items.pop(0)
            new = self.operation(item, self.operation_second if self.operation_second > 0 else item)
            result = (int(new / 3) if divide else new) % lcm

            if result % self.divisible_by == 0:
                self.monkey_true.items.append(result)
            else:
                self.monkey_false.items.append(result)

def parse_operation(operation: list[str]) -> tuple[callable, int]:
    operator: callable = None
    match operation[1]:
        case '*':
            operator = lambda x, y: x * y
        case '+':
            operator = lambda x, y: x + y
    
    if (operation[2] == "old"):
        return (operator, -1)
    else:
        return (operator, int(operation[2]))


def parse_monkeys(file: Iterator[str]) -> Iterator[Monkey]:
    counter: int = 0

    items: list[int] = []
    operation: callable = None
    operation_second: int = -1
    test: int = -1
    test_true: int = -1
    test_false: int = -1

    for line in file:
        line = line.strip()
        if line.startswith("Monkey"):
            continue
        elif line.startswith("Starting items:"):
            temp = line[16:].split(', ')
            items = [int(item) for item in temp]
        elif line.startswith("Operation:"):
            temp = line[17:].split(' ')
            operation, operation_second = parse_operation(temp)
        elif line.startswith("Test:"):
            test = int(''.join(line[19:]))
        elif line.startswith("If true:"):
            test_true = int(''.join(line[25:]))
        elif line.startswith("If false:"):
            test_false = int(''.join(line[26:]))
            yield Monkey(counter, items, operation, operation_second, test, test_true, test_false)
            items = []
            operation = None
            operation_second = -1
            test = -1
            test_true = -1
            test_false = -1

            counter += 1

def parse_all_monkeys(file: Iterator[str]) -> list[Monkey]:
    result: list[Monkey] = []

    for item in parse_monkeys(file):
        result.append(item)

    for item in result:
        item.monkey_true = result[item.test_true]
        item.monkey_false = result[item.test_false]

    return result


def do_round(monkeys: list[Monkey], lcm: int, divide: bool = True):
    for monkey in monkeys:
        monkey.do_turn(lcm, divide)

def run():
    # Round 1
    print("Round 1:")

    file: Iterator[str] = helper.load_file("input/day11.txt")

    monkeys: list[Monkey] = parse_all_monkeys(file)

    lcm: int = math.lcm(*[item.divisible_by for item in monkeys])

    for i in range(1, 21):
        #print(f"#{i}:")
        do_round(monkeys, lcm)
        #for item in monkeys:
        #    print(item.items)

    for monkey in monkeys:
        print(f"Monkey {monkey.number} inspected {monkey.inspect_counter} times.")
    
    monkeys.sort(key=lambda x: x.inspect_counter, reverse = True)
    print(f"Monkey business: {monkeys[0].inspect_counter * monkeys[1].inspect_counter}")
    
    # Round 2
    print("Round 2:")

    file: Iterator[str] = helper.load_file("input/day11.txt")

    monkeys: list[Monkey] = parse_all_monkeys(file)

    lcm: int = math.lcm(*[item.divisible_by for item in monkeys])

    for i in range(1, 10001):
        #print(f"#{i}:")
        do_round(monkeys, lcm, False)
        #for item in monkeys:
        #    print(item.items)

    for monkey in monkeys:
        print(f"Monkey {monkey.number} inspected {monkey.inspect_counter} times.")
    
    monkeys.sort(key=lambda x: x.inspect_counter, reverse = True)
    print(f"Monkey business: {monkeys[0].inspect_counter * monkeys[1].inspect_counter}")