use crate::helper::solution::{Solution, SolutionPair};

struct Range(u32, u32);

impl Range {
    fn fully_contain(&self, other: &Range) -> bool {
        self.0 <= other.0 && self.1 >= other.1
    }

    fn overlap(&self, other: &Range) -> bool {
        self.0 <= other.1 && self.1 >= other.0
    }
}

pub fn solve() -> SolutionPair {
    let input = include_str!("./input_day04.txt");

    (solve_part_1(input), solve_part_2(input))
}

fn solve_part_1(input: &'static str) -> Option<Solution> {
    let res: Vec<(Range, Range)> = parse_lines(input)
        .into_iter()
        .filter(|(first, second)| first.fully_contain(second) || second.fully_contain(first))
        .collect();

    Some(Solution::Usize(res.len()))
}

fn solve_part_2(input: &'static str) -> Option<Solution> {
    let res: Vec<(Range, Range)> = parse_lines(input)
        .into_iter()
        .filter(|(first, second)| first.overlap(second) || second.overlap(first))
        .collect();

    Some(Solution::Usize(res.len()))
}

fn parse_lines(input: &'static str) -> Vec<(Range, Range)> {
    input
        .lines()
        .filter_map(|line| {
            line.split_once(",")
                .and_then(|(first_section, second_section)| {
                    let first = first_section
                        .split_once("-")
                        .map(|(x, y)| Range(x.parse::<u32>().unwrap(), y.parse::<u32>().unwrap()));
                    let second = second_section
                        .split_once("-")
                        .map(|(x, y)| Range(x.parse::<u32>().unwrap(), y.parse::<u32>().unwrap()));

                    first.zip(second)
                })
        })
        .collect()
}
