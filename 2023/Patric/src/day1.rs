use crate::Solution;
use rayon::prelude::*;

pub struct Day1;

impl Solution<1> for Day1 {
    type Output = u32;

    fn part1(&self, input: &str) -> Self::Output {
        input.lines().map(calibration_digits).sum()
    }

    fn part2(&self, input: &str) -> Option<Self::Output> {
        Some(input.par_lines().map(full_calibration_digits).sum())
    }
}

fn calibration_digits(line: &str) -> u32 {
    let mut digits = line.chars().filter_map(|c| c.to_digit(10));
    let leading = digits.next().unwrap();
    let end = digits.last().unwrap_or(leading);
    leading * 10 + end
}

#[allow(clippy::cast_possible_truncation)]
fn full_calibration_digits(line: &str) -> u32 {
    let mut digits = line.char_indices().filter_map(|(i, c)| {
        if let Some(digit) = c.to_digit(10) {
            Some(digit)
        } else {
            NUMBERS.iter().enumerate().find_map(|(num_idx, num_text)| {
                line[i..]
                    .starts_with(num_text)
                    .then_some(num_idx as u32 + 1)
            })
        }
    });
    let leading = digits.next().unwrap();
    let end = digits.last().unwrap_or(leading);
    leading * 10 + end
}

const NUMBERS: &[&str] = &[
    "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
];

#[cfg(test)]
mod test {
    use super::*;
    use indoc::indoc;

    const FIRST_TEST_INPUT: &str = indoc! {"
        1abc2
        pqr3stu8vwx
        a1b2c3d4e5f
        treb7uchet
    "};

    #[test]
    fn test_part1() {
        assert_eq!(Day1.part1(FIRST_TEST_INPUT), 142);
    }

    const SECOND_TEST_INPUT: &str = indoc! {"
        two1nine
        eightwothree
        abcone2threexyz
        xtwone3four
        4nineeightseven2
        zoneight234
        7pqrstsixteen
    "};

    #[test]
    fn test_part2() {
        assert_eq!(Day1.part2(SECOND_TEST_INPUT), Some(281));
    }
}
