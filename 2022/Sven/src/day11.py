import textwrap
from dataclasses import dataclass
from functools import reduce
from math import lcm
from typing import Optional

from cpl_core.console import Console
from cpl_core.utils import String
from cpl_query.enumerable import Enumerable
from cpl_query.extension import List
from cpl_core.pipes import *

from aoc.aoc import get_input

# global vars
day = 11
aoc_input = get_input(2022, day)
aoc_input1 = """Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
"""


class Monkey:

    def __init__(self, m: List[str]):
        self.items = List(int)
        self.operation = ''
        self.test = ''
        self.test_true_action = None
        self.test_false_action = None
        self.inspects = 0

        for x in m:
            x = str(x).replace(' ', '')
            if ':' not in x:
                continue

            atr, value = x.split(':')
            match atr:
                case 'Startingitems':
                    for v in value.split(','):
                        self.items.append(int(v))
                case 'Operation':
                    self.operation = value
                case 'Test':
                    self.test = value
                    self.divisor = int(self.test.split('divisibleby')[1])
                case 'Iftrue':
                    self.test_true_action = value
                case 'Iffalse':
                    self.test_false_action = value

        self.initial_items = self.items.copy()

    def __repr__(self):
        return f'<Monkey {self.inspects} {self.items}>'

    def do_work(self, monkeys: List['Monkey'], modulus, part2=False):
        remove = []
        for old in self.items:
            self.inspects += 1
            if part2:
                new = int(eval(self.operation.split('=')[1]))
            else:
                new = int(eval(self.operation.split('=')[1]) / 3)
            new %= modulus

            divisible = new % int(self.test.split('divisibleby')[1]) == 0

            throw_to = monkeys.element_at(int((self.test_true_action if divisible else self.test_false_action).split('throwtomonkey')[1]))

            throw_to.items.append(new)
            remove.append(old)

        for r in remove:
            self.items.remove(r)


def main(part2=False):
    monkeys = List(str, aoc_input.splitlines()) \
        .select(lambda x: x if not str(x).startswith('Monkey') else None).where(lambda x: x is not None) \
        .split(lambda x: '') \
        .select(lambda monkey: Monkey(monkey))

    mod = lcm(*monkeys.select(lambda x: x.divisor))
    for i in range(0, 10000 if part2 else 20):
        for m in monkeys:
            m.do_work(monkeys, mod, part2)

    Console.write_line(reduce((lambda x, y: x * y), monkeys.order_by_descending(lambda x: x.inspects).take(2).select(lambda x: x.inspects)))


if __name__ == '__main__':
    Console.write_line(f'Advent of code day {day}')
    main()
    main(True)
    Console.write_line()
