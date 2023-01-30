use std::collections::HashSet;

use crate::helper::solution::{Solution, SolutionPair};

pub fn solve() -> SolutionPair {
    let input = include_str!("./input_day06.txt");

    (solve_part_1(input), solve_part_2(input))
}

fn solve_part_1(input: &'static str) -> Option<Solution> {
    input
        .chars()
        .collect::<Vec<char>>()
        .array_windows::<4>()
        .enumerate()
        .find(|(_, substr)| HashSet::from(**substr).len() == 4)
        .map(|(index, _)| Solution::Usize(index + 4))
}

fn solve_part_2(input: &'static str) -> Option<Solution> {
    input
        .chars()
        .collect::<Vec<char>>()
        .array_windows::<14>()
        .enumerate()
        .find(|(_, substr)| HashSet::from(**substr).len() == 14)
        .map(|(index, _)| Solution::Usize(index + 14))
}
