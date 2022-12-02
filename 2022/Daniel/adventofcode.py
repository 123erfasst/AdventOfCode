import argparse

import days.day1 as day1
import days.day2 as day2

def get_arguments(numberOfDays: int):
    parser = argparse.ArgumentParser()
    parser.add_argument("day", type=int,
                        choices=[i for i in range(1, numberOfDays + 1)],
                        help="Day of the Advent of Code")

    args = parser.parse_args()

    return args

def main() -> None:
    days = [
        day1.run,
        day2.run
    ]

    args = get_arguments(len(days))

    days[args.day - 1]()


if __name__ == "__main__":
    main()
