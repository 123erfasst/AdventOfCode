from typing import Iterator
import re

from . import helper

def parse_file_matrix(file: Iterator[str]) -> list[list[str]]:
    matrix: list[list[str]] = []
    for line in file:
        if not line:
            break

        if len(matrix) == 0:
            for i in range(int(len(line) / 4 + 1)):
                matrix.append([])

        for column in range(1, len(line), 4):
            if not line[column] == ' ':
                matrix[int((column - 1) / 4)].insert(0, line[column])

    for column in matrix:
        column.pop(0)
    return matrix

def parse_file_moves(file: Iterator[str]) -> Iterator[tuple[int, int, int]]:
    for line in file:
        pair = re.split('from|to', line.replace('move', ''))
        yield (int(pair[0]), int(pair[1]), int(pair[2]))

def do_all_moves(matrix: list[list[str]], moves: Iterator[tuple[int, int, int]]):
    for move in moves:
        for times in range(move[0]):
            do_move(matrix, (move[1], move[2]))


def do_move(matrix: list[list[str]], move: tuple[int, int]):
    matrix[move[1] - 1].append(matrix[move[0] - 1].pop())

def do_all_moves_at_once(matrix: list[list[str]], moves: Iterator[tuple[int, int, int]]):
    for move in moves:
        do_move_at_once(matrix, move)

def do_move_at_once(matrix: list[list[str]], move: tuple[int, int, int]):
    from_column: list(str) = matrix[move[1] - 1]

    moving: list[str] = from_column[move[0] * - 1:]
    matrix[move[1] - 1] = from_column[:len(from_column) - move[0]]
    matrix[move[2] - 1].extend(moving)


def run():
    # Round 1
    print("Round 1:")

    file: Iterator[str] = helper.load_file("input/day05.txt")

    matrix: list[list[str]] = parse_file_matrix(file)

    print("Before:")

    for item in matrix:
        print(item)

    moves: Iterator[tuple[int, int, int]] = parse_file_moves(file)

    do_all_moves(matrix, moves)

    print("\nAfter:")

    for item in matrix:
        print(item)

    # Round 2
    print("\n\nRound 2:")

    file: Iterator[str] = helper.load_file("input/day05.txt")

    matrix: list[list[str]] = parse_file_matrix(file)

    print("Before:")

    for item in matrix:
        print(item)

    moves: Iterator[tuple[int, int, int]] = parse_file_moves(file)

    do_all_moves_at_once(matrix, moves)

    print("\nAfter:")

    for item in matrix:
        print(item)
