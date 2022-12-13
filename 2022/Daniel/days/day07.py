from __future__ import annotations
from typing import Iterator
from dataclasses import dataclass, field

from . import helper

@dataclass
class dir:
    name: str
    parent: dir
    files: list[tuple[str, int]] = field(default_factory=list)
    dirs: list[dir] = field(default_factory=list)

    def add_dir(self: dir, dir: dir):
        self.dirs.append(dir)

    def add_file(self: dir, file: tuple[str, int]):
        self.files.append(file)

    def get_size(self: dir) -> int:
        sum: int = self.get_file_size()

        for dir in self.dirs:
            sum += dir.get_size()

        return sum
    
    def get_file_size(self: dir) -> int:
        sum: int = 0

        for file in self.files:
            sum += file[1]

        return sum

def parse_line(line: str, pwd: dir) -> dir:
    if line.startswith('$ cd ..'):
        return pwd.parent
    elif line.startswith('$ cd'):
        for item in pwd.dirs:
            if item.name == line[5:]:
                return item
        raise Exception('Parsing Error')
    elif line.startswith('$ ls'):
        return pwd
    
    if line.startswith("dir"):
        new_dir: dir = dir(line[4:], pwd)
        pwd.add_dir(new_dir)
    else:
        file: list[str] = line.split(' ')
        pwd.add_file((file[1], int(file[0])))

    return pwd

def generate_tree(file: Iterator[str]) -> dir:
    root: dir = dir("/", None)
    pwd: dir = root
    next(file)
    for line in file:
        pwd = parse_line(line, pwd)

    return root

def walk_file_size_small_sum(pwd: dir) -> int:
    sum: int = 0
    
    file_size: int = pwd.get_size()
    if file_size <= 100000:
        sum += file_size

    for dir in pwd.dirs:
        sum += walk_file_size_small_sum(dir)

    return sum

def all_dirs(pwd: dir) -> list[dir]:
    result: list[dir] = [ pwd ]

    for dir in pwd.dirs:
        result.extend(all_dirs(dir))

    return result


def run():
    # Round 1
    print("Round 1:")

    file: Iterator[str] = helper.load_file("input/day07.txt")

    root: dir = generate_tree(file)

    file_size_small: int = walk_file_size_small_sum(root)

    print(f"Size: {file_size_small}")

    # Round 2
    print("Round 2:")

    dirs = all_dirs(root)

    needed_space: int = 30000000 - (70000000 - dirs[0].get_size())

    result = filter(lambda x: x.get_size() > needed_space, dirs)

    ordered_result = sorted(result, key = lambda file: file.get_size())

    print(f"Smallest: {ordered_result[0].get_size()}")
