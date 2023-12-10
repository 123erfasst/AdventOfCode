use crate::Solution;
use itertools::Itertools;
use nom::character::complete::{i64 as n_i64, line_ending, space1};
use nom::multi::separated_list1;
use nom::IResult;
use std::iter::successors;

pub struct Day9;

impl Solution<9> for Day9 {
    type Output = i64;

    fn part1(&self, input: &str) -> Self::Output {
        let data = parse_input(input).expect("Format mismtach").1;
        data.into_iter().map(extrapolate_forward).sum()
    }

    fn part2(&self, input: &str) -> Option<Self::Output> {
        let data = parse_input(input).expect("Format mismtach").1;
        Some(data.into_iter().map(extrapolate_backward).sum())
    }
}

fn extrapolate_forward(values: Vec<i64>) -> i64 {
    successor_differences(values)
        .map(|v| *v.last().expect("Must not be empty"))
        .sum()
}

fn extrapolate_backward(values: Vec<i64>) -> i64 {
    let starts = successor_differences(values)
        .map(|v| *v.first().expect("Must not be empty"))
        .collect::<Vec<_>>();

    starts.iter().rev().fold(0, |acc, value| value - acc)
}

fn successor_differences(values: Vec<i64>) -> impl Iterator<Item = Vec<i64>> {
    successors(Some(values), |values| {
        if values.iter().all(|&value| value == 0) {
            None
        } else {
            Some(
                values
                    .iter()
                    .tuple_windows()
                    .map(|(a, b)| b - a)
                    .collect::<Vec<_>>(),
            )
        }
    })
}

fn parse_input(input: &str) -> IResult<&str, Vec<Vec<i64>>> {
    separated_list1(line_ending, separated_list1(space1, n_i64))(input)
}

#[cfg(test)]
mod test {
    use super::*;
    use indoc::indoc;

    const PARTIAL_INPUT: &str = indoc! {"
        10 13 16 21 30 45
    "};

    const TEST_INPUT: &str = indoc! {"
        0 3 6 9 12 15
        1 3 6 10 15 21
        10 13 16 21 30 45
    "};

    #[test]
    fn test_part1() {
        assert_eq!(Day9.part1(PARTIAL_INPUT), 68);
        assert_eq!(Day9.part1(TEST_INPUT), 114);
    }

    #[test]
    fn test_part2() {
        assert_eq!(Day9.part2(PARTIAL_INPUT), Some(5));
        assert_eq!(Day9.part2(TEST_INPUT), Some(2));
    }
}
