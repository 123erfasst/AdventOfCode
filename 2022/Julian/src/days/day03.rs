use crate::helper::solution::{Solution, SolutionPair};

pub fn solve() -> SolutionPair {
    let input = include_str!("./input_day03.txt");

    (solve_part_1(input), solve_part_2(input))
}

fn solve_part_1(input: &'static str) -> Option<Solution> {
    let bags: u32 = input
        .lines()
        .map(|line| {
            let (first, second) = line.split_at(line.len() / 2);

            let result = first
                .chars()
                .find(|char| second.contains(&char.to_string()));

            match result {
                Some(val) => priority(val),
                None => 0,
            }
        })
        .sum();

    Some(Solution::U32(bags))
}

fn solve_part_2(input: &'static str) -> Option<Solution> {
    let bags: u32 = input
        .lines()
        .array_chunks::<3>()
        .map(|[first, second, third]| {
            let result = first.chars().find(|char| {
                second.contains(&char.to_string()) && third.contains(&char.to_string())
            });

            match result {
                Some(val) => priority(val),
                None => 0,
            }
        })
        .sum();

    Some(Solution::U32(bags))
}

fn priority(item: char) -> u32 {
    if !item.is_ascii_alphabetic() {
        return 0;
    }
    match item {
        'a'..='z' => (item as u8 - b'a' + 1) as u32,
        'A'..='Z' => (item as u8 - b'A' + 27) as u32,
        _ => unreachable!(),
    }
}
