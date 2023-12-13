use crate::Solution;
use ahash::AHashSet;
use nom::IResult;
use rayon::prelude::*;

pub struct Day4;

impl Solution<4> for Day4 {
    type Output = u32;

    fn part1(&self, input: &str) -> Self::Output {
        input
            .par_lines()
            .map(|line| parse_card(line).expect("Card format mismatch").1)
            .map(Card::score)
            .sum()
    }

    fn part2(&self, input: &str) -> Option<Self::Output> {
        let matches = input
            .par_lines()
            .map(|line| parse_card(line).expect("Card format mismatch").1)
            .map(Card::matches)
            .collect::<Vec<_>>();
        let copies = matches.iter().enumerate().fold(
            vec![1u32; matches.len()],
            |mut copies, (line, &match_count)| {
                for m in 0..match_count {
                    copies[line + 1 + m] += copies[line];
                }
                copies
            },
        );

        Some(copies.iter().sum())
    }
}

fn parse_card(line: &str) -> IResult<&str, Card> {
    use nom::bytes::complete::tag;
    use nom::character::complete::{char, space1, u32};
    use nom::combinator::map;
    use nom::multi::separated_list1;
    use nom::sequence::separated_pair;
    use nom::sequence::{preceded, tuple};

    map(
        preceded(
            tuple((tag("Card"), space1, u32, char(':'), space1)),
            separated_pair(
                separated_list1(space1, u32),
                tuple((space1, char('|'), space1)),
                separated_list1(space1, u32),
            ),
        ),
        |(winning, have)| Card {
            winning: winning.into_iter().collect(),
            have: have.into_iter().collect(),
        },
    )(line)
}

struct Card {
    winning: AHashSet<u32>,
    have: AHashSet<u32>,
}

impl Card {
    fn matches(self) -> usize {
        self.have.intersection(&self.winning).count()
    }

    fn score(self) -> u32 {
        let matches = self.matches();
        if matches != 0 {
            1 << (matches - 1)
        } else {
            0
        }
    }
}

#[cfg(test)]
mod test {
    use super::*;
    use indoc::indoc;

    const TEST_INPUT: &str = indoc! {"
        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
        Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
        Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
        Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
        Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
    "};

    #[test]
    fn test_part1() {
        assert_eq!(Day4.part1(TEST_INPUT), 13);
    }

    #[test]
    fn test_part2() {
        assert_eq!(Day4.part2(TEST_INPUT), Some(30));
    }
}
