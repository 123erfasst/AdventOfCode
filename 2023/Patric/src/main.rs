use aoc_2023::*;
use clap::Parser;

#[derive(Parser, Debug)]
#[clap(long_about = None)]
struct Args {
    #[arg(default_value_t = 1)]
    day: u8,
}

fn main() {
    let day = Args::parse().day;

    match day {
        1 => Runner::new(include_str!("../inputs/day01.txt")).run(&day1::Day1),
        2 => Runner::new(include_str!("../inputs/day02.txt")).run(&day2::Day2),
        3 => Runner::new(include_str!("../inputs/day03.txt")).run(&day3::Day3),
        4 => Runner::new(include_str!("../inputs/day04.txt")).run(&day4::Day4),
        5 => Runner::new(include_str!("../inputs/day05.txt")).run(&day5::Day5),
        _ => panic!("Could not find day {day}"),
    }
}
