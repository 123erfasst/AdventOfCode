use crate::Solution;
use ahash::{AHashMap, AHasher};
use std::hash::{Hash, Hasher};
use std::str::FromStr;

pub struct Day14;

impl Solution<14> for Day14 {
    type Output = usize;

    fn part1(&self, input: &str) -> Self::Output {
        let mut dish = Dish::from_str(input).expect("Format");
        dish.tilt();
        dish.total_load()
    }

    fn part2(&self, input: &str) -> Option<Self::Output> {
        let mut dish = Dish::from_str(input).expect("Format");
        dish.cycle_until_target(1_000_000_000);
        Some(dish.total_load())
    }
}

#[derive(Debug, Eq, PartialEq, Hash)]
struct Dish {
    grid: Vec<Vec<Cell>>,
    rows: usize,
    cols: usize,
}

impl Dish {
    fn total_load(&self) -> usize {
        self.grid
            .iter()
            .enumerate()
            .map(|(r, row)| {
                row.iter().filter(|&&cell| cell == Cell::Round).count() * (self.rows - r)
            })
            .sum()
    }

    #[allow(clippy::match_on_vec_items)]
    fn tilt(&mut self) {
        for col in 0..self.cols {
            let mut empty = 0;
            for row in 0..self.rows {
                match self.grid[row][col] {
                    Cell::Round => {
                        self.grid[row][col] = Cell::Empty;
                        self.grid[empty][col] = Cell::Round;
                        empty += 1;
                    }
                    Cell::Cube => {
                        empty = row + 1;
                    }
                    Cell::Empty => (),
                }
            }
        }
    }

    fn rotate(&mut self) {
        let size = self.rows;
        let mut rotated = vec![vec![Cell::Empty; size]; self.cols];

        for (r, row) in self.grid.iter().enumerate() {
            for (c, cell) in row.iter().enumerate() {
                rotated[c][size - r - 1] = *cell;
            }
        }

        self.grid = rotated;
    }

    #[inline]
    fn cycle(&mut self) {
        for _ in 0..4 {
            self.tilt();
            self.rotate();
        }
    }

    fn cycle_until_target(&mut self, target: usize) {
        let mut steps = 0;
        let mut seen = AHashMap::new();

        while steps < target {
            self.cycle();
            steps += 1;
            let first_occurrence = *seen.entry(self.get_hash()).or_insert(steps);

            if first_occurrence < steps {
                let cycle_length = steps - first_occurrence;
                let full_cycles = (target - steps) / cycle_length;
                steps += cycle_length * full_cycles;
            }
        }
    }

    #[inline]
    fn get_hash(&self) -> u64 {
        let mut hasher = AHasher::default();
        self.hash(&mut hasher);
        hasher.finish()
    }
}

impl FromStr for Dish {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        use Cell::{Cube, Empty, Round};
        let grid: Vec<Vec<Cell>> = s
            .lines()
            .map(|line| {
                line.chars()
                    .map(|c| match c {
                        'O' => Ok(Round),
                        '#' => Ok(Cube),
                        '.' => Ok(Empty),
                        c => Err(format!("Unknown cell {c}").to_string()),
                    })
                    .collect::<Result<_, Self::Err>>()
            })
            .collect::<Result<_, Self::Err>>()?;

        Ok(Self {
            rows: grid.len(),
            cols: grid[0].len(),
            grid,
        })
    }
}

#[derive(Debug, Eq, PartialEq, Copy, Clone, Hash)]
enum Cell {
    Round,
    Cube,
    Empty,
}

#[cfg(test)]
mod test {
    use super::*;
    use indoc::indoc;

    const TEST_INPUT: &str = indoc! {"
        O....#....
        O.OO#....#
        .....##...
        OO.#O....O
        .O.....O#.
        O.#..O.#.#
        ..O..#O..O
        .......O..
        #....###..
        #OO..#....
    "};

    #[test]
    fn test_part1() {
        assert_eq!(Day14.part1(TEST_INPUT), 136);
    }

    #[test]
    fn test_part2() {
        assert_eq!(Day14.part2(TEST_INPUT), Some(64));
    }
}
