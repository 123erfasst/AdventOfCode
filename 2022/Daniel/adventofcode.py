import argparse

import days.day1 as day1

def get_arguments():
    parser = argparse.ArgumentParser()
    parser.add_argument("day", type=int,
                        choices=[i for i in range(1, 25)],
                        help="Day of the Advent of Code")

    args = parser.parse_args()

    return args

def main() -> None:
    args = get_arguments()

    match args.day:
        case 1:
            day1.run()
            return


    print(args)


if __name__ == "__main__":
    main()
