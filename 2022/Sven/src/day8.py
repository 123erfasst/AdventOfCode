from typing import Optional

from cpl_core.console import Console
from cpl_core.utils import String
from cpl_query.enumerable import Enumerable
from cpl_query.extension import List
from cpl_core.pipes import *

from aoc.aoc import get_input

# global vars
day = 8
aoc_input = get_input(2022, day)
aoc_input1 = """30373
25512
65332
33549
35390"""


def parse(lines: str) -> List[List[int]]:
    grid = List(List)
    for line in lines.splitlines():
        trees = List(int)
        for tree in line:
            trees.add(int(tree))

        grid.add(trees)

    return grid


def main(grid: List[List[int]]) -> tuple[int, int]:
    # consideration for refactoring part1: select bools then select many count
    visible = List(int)
    scenic_scores = List(int)
    for i, trees in enumerate(grid):
        if i == 0 or i == grid.count() - 1:
            visible.extend(trees)
            continue

        for n, t in enumerate(trees):
            if n == 0 or n == trees.count() - 1:
                visible.add(t)
                continue

            up = all([t > grid[x][n] for x in range(i - 1, -1, -1)])
            down = all([t > grid[x][n] for x in range(i + 1, len(grid))])
            left = all([t > x for x in trees[:n]])
            right = all([t > x for x in trees[n + 1:]])

            for l in range(n + 1, grid.first().count()):
                if trees[l] >= t:
                    break

            for r in range(n - 1, -1, -1):
                if trees[r] >= t:
                    break

            for d in range(i + 1, grid.count()):
                if grid[d][n] >= t:
                    break

            for u in range(i - 1, -1, -1):
                if grid[u][n] >= t:
                    break

            # im not proud of this shit
            score = (l - n) * (n - r) * (d - i) * (i - u)
            scenic_scores.add(score)

            if up or down or left or right:
                visible.add(t)

    return visible.count(), scenic_scores.max()


if __name__ == '__main__':
    Console.write_line(f'Advent of code day {day}')
    tree_map = parse(aoc_input)
    p1 = main(tree_map)
    Console.write_line(p1)
    Console.write_line()
