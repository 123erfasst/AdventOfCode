use crate::helper::solution::{Solution, SolutionPair};

pub fn solve() -> SolutionPair {
    let input = include_str!("./input_day01.txt");

    (solve_part_1(input), solve_part_2(input))
}

fn solve_part_1(input: &'static str) -> Option<Solution> {
    input
        .split("\r\n\r\n")
        .map(|group| {
            group
                .lines()
                .map(|line| match line.parse::<u32>() {
                    Ok(val) => val,
                    Err(_) => 0,
                })
                .sum::<u32>()
        })
        .max()
        .map(Solution::U32)
}

fn solve_part_2(input: &'static str) -> Option<Solution> {
    let mut x: Vec<u32> = input
        .split("\r\n\r\n")
        .map(|group| {
            group
                .lines()
                .map(|line| match line.parse::<u32>() {
                    Ok(val) => val,
                    Err(_) => 0,
                })
                .sum::<u32>()
        })
        .collect();
    x.sort();
    Some(Solution::U32(x.iter().rev().take(3).sum()))
}
