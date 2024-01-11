use crate::Solution;
use nom::IResult;
use rayon::prelude::*;

pub struct Day13;

impl Solution<13> for Day13 {
    type Output = usize;

    fn part1(&self, input: &str) -> Self::Output {
        input
            .split("\n\n")
            .collect::<Vec<_>>()
            .par_iter()
            .filter_map(|input| Pattern::parse(input).ok())
            .filter_map(|(_, pattern)| pattern.reflections(0))
            .sum()
    }

    fn part2(&self, input: &str) -> Option<Self::Output> {
        Some(
            input
                .split("\n\n")
                .collect::<Vec<_>>()
                .par_iter()
                .filter_map(|input| Pattern::parse(input).ok())
                .filter_map(|(_, pattern)| pattern.reflections(1))
                .sum(),
        )
    }
}

#[derive(Debug)]
struct Pattern {
    rows: Vec<u32>,
    cols: Vec<u32>,
}

impl Pattern {
    fn reflections(&self, smudges: u32) -> Option<usize> {
        if let Some(x) = Pattern::reflect(&self.rows, smudges) {
            Some(x * 100)
        } else {
            Pattern::reflect(&self.cols, smudges)
        }
    }

    fn reflect(axis: &[u32], smudges: u32) -> Option<usize> {
        let size = axis.len();
        (1..size).find(|&i| {
            (0..i.min(size - i)).fold(0, |smudge_count, j| {
                smudge_count + (axis[i - j - 1] ^ axis[i + j]).count_ones()
            }) == smudges
        })
    }

    fn parse(input: &str) -> IResult<&str, Pattern> {
        use nom::branch::alt;
        use nom::character::complete::{char, line_ending};
        use nom::combinator::map;
        use nom::multi::{many1, separated_list1};

        map(
            separated_list1(
                line_ending::<&str, nom::error::Error<&str>>,
                many1(alt((char('.'), char('#')))),
            ),
            |grid| {
                let width = grid[0].len();

                let rows = grid
                    .iter()
                    .map(|row| {
                        (0..width)
                            .map(|col| row[col])
                            .fold(0, |num, cell| num << 1 | u32::from(cell == '#'))
                    })
                    .collect::<Vec<_>>();
                let cols = (0..width)
                    .map(|col| {
                        grid.iter()
                            .map(|row| row[col])
                            .fold(0, |num, cell| num << 1 | u32::from(cell == '#'))
                    })
                    .collect::<Vec<_>>();

                Pattern { rows, cols }
            },
        )(input)
    }
}

#[cfg(test)]
mod test {
    use super::*;
    use indoc::indoc;

    const TEST_INPUT: &str = indoc! {"
        #.##..##.
        ..#.##.#.
        ##......#
        ##......#
        ..#.##.#.
        ..##..##.
        #.#.##.#.

        #...##..#
        #....#..#
        ..##..###
        #####.##.
        #####.##.
        ..##..###
        #....#..#
    "};

    #[test]
    fn test_part1() {
        assert_eq!(Day13.part1(TEST_INPUT), 405);
    }

    #[test]
    fn test_part2() {
        assert_eq!(Day13.part2(TEST_INPUT), Some(400));
    }
}
