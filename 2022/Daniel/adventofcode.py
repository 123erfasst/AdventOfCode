import argparse

import days.day01 as day01
import days.day02 as day02
import days.day03 as day03
import days.day04 as day04
import days.day05 as day05
import days.day06 as day06
import days.day07 as day07
import days.day08 as day08
import days.day09 as day09
import days.day10 as day10
import days.day11 as day11
import days.day12 as day12
import days.day13 as day13

def get_arguments(numberOfDays: int):
    parser = argparse.ArgumentParser()
    parser.add_argument("day", type=int,
                        choices=[i for i in range(1, numberOfDays + 1)],
                        help="Day of the Advent of Code")

    args = parser.parse_args()

    return args

def main() -> None:
    days = [
        day01.run,
        day02.run,
        day03.run,
        day04.run,
        day05.run,
        day06.run,
        day07.run,
        day08.run,
        day09.run,
        day10.run,
        day11.run,
        day12.run,
        day13.run
    ]

    args = get_arguments(len(days))

    days[args.day - 1]()


if __name__ == "__main__":
    main()
