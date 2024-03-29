#![feature(iter_array_chunks, array_windows)]
#![feature(assert_matches)]
#![deny(clippy::all)]
#![warn(clippy::pedantic)]

use mimalloc::MiMalloc;
use std::fmt::Display;

#[global_allocator]
static GLOBAL: MiMalloc = MiMalloc;

pub trait Solution<const DAY: u8> {
    type Output: Display;

    fn part1(&self, input: &str) -> Self::Output;
    fn part2(&self, input: &str) -> Option<Self::Output> {
        let _ = input;
        None
    }
}

pub struct Runner<'a> {
    input: &'a str,
}

impl Runner<'_> {
    #[must_use]
    pub fn new(input: &'_ str) -> Runner<'_> {
        Runner { input }
    }

    pub fn run<const DAY: u8, S: Solution<DAY>>(&'_ self, solution: &S) {
        let input = &self.input.replace("\r\n", "\n");
        println!("--------");
        println!("Day {DAY:02}");
        println!("--------");

        let result = solution.part1(input);
        println!("Part 1:\n{result}");

        let result = solution.part2(input);
        if let Some(result) = result {
            println!("Part 2:\n{result}");
        } else {
            println!("Part 2:\nNot solved");
        }
    }
}

pub mod day1;
pub mod day10;
pub mod day11;
pub mod day12;
pub mod day13;
pub mod day14;
pub mod day15;
pub mod day16;
pub mod day17;
pub mod day18;
pub mod day2;
pub mod day3;
pub mod day4;
pub mod day5;
pub mod day6;
pub mod day7;
pub mod day8;
pub mod day9;
pub mod util;
