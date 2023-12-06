use crate::Solution;
use nom::bytes::complete::tag;
use nom::character::complete::{line_ending, space1, u64 as n_u64};
use nom::multi::separated_list1;
use nom::sequence::{preceded, separated_pair, tuple};
use nom::IResult;

pub struct Day6;

impl Solution<6> for Day6 {
    type Output = u64;

    fn part1(&self, input: &str) -> Self::Output {
        let (times, distances) = parse_input(input).expect("Format mismatch").1;
        times
            .into_iter()
            .zip(distances)
            .map(|(time, distance)| calculate_options(time, distance))
            .product()
    }

    fn part2(&self, input: &str) -> Option<Self::Output> {
        let (times, distances) = parse_input(input).expect("Format mismatch").1;
        let time = times
            .into_iter()
            .map(|t| t.to_string())
            .collect::<String>()
            .parse::<u64>()
            .unwrap();
        let distance = distances
            .into_iter()
            .map(|d| d.to_string())
            .collect::<String>()
            .parse::<u64>()
            .unwrap();
        Some(calculate_options(time, distance))
    }
}

#[allow(
    clippy::cast_precision_loss,
    clippy::cast_sign_loss,
    clippy::cast_possible_truncation
)]
#[must_use]
pub fn calculate_options(time: u64, distance: u64) -> u64 {
    let root = f64::sqrt((time.pow(2) - 4 * distance) as f64);
    let mut a = (time as f64 + root) / 2.0;
    let mut b = (time as f64 - root) / 2.0;
    // Perfect squares would result in a tie with the record
    if a.fract() == 0.0 {
        a -= 1.0;
    }
    if b.fract() == 0.0 {
        b += 1.0;
    }

    f64::floor(a) as u64 - f64::ceil(b) as u64 + 1
}

fn parse_input(input: &str) -> IResult<&str, (Vec<u64>, Vec<u64>)> {
    separated_pair(
        preceded(
            tuple((tag("Time:"), space1)),
            separated_list1(space1, n_u64),
        ),
        line_ending,
        preceded(
            tuple((tag("Distance:"), space1)),
            separated_list1(space1, n_u64),
        ),
    )(input)
}

#[cfg(test)]
mod test {
    use super::*;
    use indoc::indoc;

    const TEST_INPUT: &str = indoc! {"
        Time:      7  15   30
        Distance:  9  40  200
    "};

    #[test]
    fn test_part1() {
        assert_eq!(Day6.part1(TEST_INPUT), 288);
    }

    #[test]
    fn test_part2() {
        assert_eq!(Day6.part2(TEST_INPUT), Some(71503));
    }
}
