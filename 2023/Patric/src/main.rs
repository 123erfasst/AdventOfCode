use aoc_2023::*;
use clap::Parser;

#[derive(Parser, Debug)]
#[clap(long_about = None)]
struct Args {
    day: Option<u8>,
}

fn main() {
    let day = Args::parse().day;

    let days = [
        || Runner::new(include_str!("../inputs/day01.txt")).run(&day1::Day1),
        || Runner::new(include_str!("../inputs/day02.txt")).run(&day2::Day2),
        || Runner::new(include_str!("../inputs/day03.txt")).run(&day3::Day3),
        || Runner::new(include_str!("../inputs/day04.txt")).run(&day4::Day4),
        || Runner::new(include_str!("../inputs/day05.txt")).run(&day5::Day5),
        || Runner::new(include_str!("../inputs/day06.txt")).run(&day6::Day6),
        || Runner::new(include_str!("../inputs/day07.txt")).run(&day7::Day7),
        || Runner::new(include_str!("../inputs/day08.txt")).run(&day8::Day8),
        || Runner::new(include_str!("../inputs/day09.txt")).run(&day9::Day9),
        || Runner::new(include_str!("../inputs/day10.txt")).run(&day10::Day10),
        || Runner::new(include_str!("../inputs/day11.txt")).run(&day11::Day11),
        || Runner::new(include_str!("../inputs/day12.txt")).run(&day12::Day12),
        || Runner::new(include_str!("../inputs/day13.txt")).run(&day13::Day13),
        || Runner::new(include_str!("../inputs/day14.txt")).run(&day14::Day14),
        || Runner::new(include_str!("../inputs/day15.txt")).run(&day15::Day15),
        || Runner::new(include_str!("../inputs/day16.txt")).run(&day16::Day16),
        || Runner::new(include_str!("../inputs/day17.txt")).run(&day17::Day17),
        || Runner::new(include_str!("../inputs/day18.txt")).run(&day18::Day18),
    ];

    match day {
        Some(day) if day <= days.len() as u8 => days[day as usize - 1](),
        Some(day) => panic!("No solution for day {}", day),
        None => days.iter().for_each(|d| d()),
    }
}
