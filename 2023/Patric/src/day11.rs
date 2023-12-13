use crate::util::point::Point2D;
use crate::Solution;
use ahash::AHashSet;
use itertools::Itertools;
use std::str::FromStr;

pub struct Day11;

impl Solution<11> for Day11 {
    type Output = usize;

    fn part1(&self, input: &str) -> Self::Output {
        let universe = Universe::from_str(input).expect("Format");
        sum_distances(&universe.expand(2))
    }

    fn part2(&self, input: &str) -> Option<Self::Output> {
        let universe = Universe::from_str(input).expect("Format");
        Some(sum_distances(&universe.expand(1_000_000)))
    }
}

type Point = Point2D<usize>;

struct Universe {
    galaxies: Vec<Point>,
    col_gaps: Vec<usize>,
    row_gaps: Vec<usize>,
}

impl Universe {
    #[must_use]
    fn expand(&self, factor: usize) -> Vec<Point> {
        self.galaxies
            .iter()
            .map(|g| {
                Point::new(
                    g.x + self.col_gaps[g.x] * (factor - 1),
                    g.y + self.row_gaps[g.y] * (factor - 1),
                )
            })
            .collect()
    }
}

fn sum_distances(points: &[Point]) -> usize {
    points
        .iter()
        .tuple_combinations()
        .map(|(a, &b)| a.manhattan_distance(b))
        .sum()
}

impl FromStr for Universe {
    type Err = ();

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let (galaxies, galaxy_cols, galaxy_rows) = s
            .lines()
            .enumerate()
            .flat_map(|(row, line)| {
                line.chars()
                    .enumerate()
                    .map(move |(col, char)| (row, col, char))
            })
            .filter_map(|(row, col, char)| (char == '#').then_some(Point::new(col, row)))
            .fold(
                (Vec::new(), AHashSet::new(), AHashSet::new()),
                |(mut galaxies, mut galaxy_cols, mut galaxy_rows), galaxy| {
                    galaxy_cols.insert(galaxy.x);
                    galaxy_rows.insert(galaxy.y);
                    galaxies.push(galaxy);
                    (galaxies, galaxy_cols, galaxy_rows)
                },
            );

        let height = galaxies.iter().map(|g| g.y).max().unwrap() + 1;
        let width = galaxies.iter().map(|g| g.x).max().unwrap() + 1;

        let row_gaps = precompute_gaps(height, &galaxy_rows);
        let col_gaps = precompute_gaps(width, &galaxy_cols);

        Ok(Self {
            galaxies,
            col_gaps,
            row_gaps,
        })
    }
}

fn precompute_gaps(size: usize, galaxies: &AHashSet<usize>) -> Vec<usize> {
    let mut gaps = vec![];
    let mut empty_space = 0;
    for idx in 0..size {
        if !galaxies.contains(&idx) {
            empty_space += 1;
        }

        gaps.push(empty_space);
    }
    gaps
}

#[cfg(test)]
mod test {
    use super::*;
    use indoc::indoc;

    const TEST_INPUT: &str = indoc! {"
        ...#......
        .......#..
        #.........
        ..........
        ......#...
        .#........
        .........#
        ..........
        .......#..
        #...#.....
    "};

    #[test]
    fn test_part1() {
        assert_eq!(Day11.part1(TEST_INPUT), 374);
    }

    #[test]
    fn test_part2() {
        let universe = Universe::from_str(TEST_INPUT).expect("Format");
        assert_eq!(sum_distances(&universe.expand(10)), 1030);
        assert_eq!(sum_distances(&universe.expand(100)), 8410);
    }
}
