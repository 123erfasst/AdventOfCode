#![feature(iter_array_chunks, array_windows)]
#![feature(assert_matches)]
#![deny(clippy::all)]
#![warn(clippy::pedantic)]

use std::fmt::Display;

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
pub mod day2;
pub mod day3;
