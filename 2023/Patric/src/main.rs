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
        _ => panic!("Could not find day {day}"),
    }
}
