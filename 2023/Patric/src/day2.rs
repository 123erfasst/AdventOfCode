use crate::Solution;
use nom::branch::alt;
use nom::bytes::complete::tag;
use nom::character::complete::u32 as n_u32;
use nom::character::complete::{char, space1};
use nom::combinator::map;
use nom::multi::separated_list1;
use nom::sequence::{delimited, pair, separated_pair, tuple};
use nom::IResult;
use rayon::prelude::*;

pub struct Day2;

impl Solution<2> for Day2 {
    type Output = u32;

    fn part1(&self, input: &str) -> Self::Output {
        input
            .par_lines()
            .filter_map(|line| parse_game(line).ok())
            .map(|r| r.1)
            .filter_map(|(id, valid, _)| valid.then_some(id))
            .sum()
    }

    fn part2(&self, input: &str) -> Option<Self::Output> {
        let power = input
            .par_lines()
            .filter_map(|line| parse_game(line).ok())
            .map(|r| r.1 .2)
            .sum();
        Some(power)
    }
}

fn parse_game(line: &str) -> IResult<&str, (u32, bool, u32)> {
    map(
        tuple((
            delimited(pair(tag("Game"), space1), n_u32, pair(char(':'), space1)),
            separated_list1(
                pair(char(';'), space1),
                separated_list1(
                    pair(char(','), space1),
                    separated_pair(n_u32, space1, alt((tag("red"), tag("green"), tag("blue")))),
                ),
            ),
        )),
        |(id, sets)| {
            let mut valid = true;
            let (mut r, mut g, mut b) = (0, 0, 0);
            for set in sets {
                for (amount, color) in set {
                    match color {
                        "red" => {
                            valid &= amount <= 12;
                            r = r.max(amount);
                        }
                        "green" => {
                            valid &= amount <= 13;
                            g = g.max(amount);
                        }
                        "blue" => {
                            valid &= amount <= 14;
                            b = b.max(amount);
                        }
                        _ => unreachable!(),
                    }
                }
            }
            (id, valid, r * g * b)
        },
    )(line)
}

#[cfg(test)]
mod test {
    use super::*;
    use indoc::indoc;

    const TEST_INPUT: &str = indoc! {"
        Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
        Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
        Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
        Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    "};

    #[test]
    fn test_part1() {
        assert_eq!(Day2.part1(TEST_INPUT), 8);
    }

    #[test]
    fn test_part2() {
        assert_eq!(Day2.part2(TEST_INPUT), Some(2286));
    }
}
