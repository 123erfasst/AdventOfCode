use crate::util::point::Point2D;
use crate::Solution;
use std::iter::successors;
use std::ops::Index;
use std::str::FromStr;

pub struct Day10;

impl Solution<10> for Day10 {
    type Output = u64;

    fn part1(&self, input: &str) -> Self::Output {
        let maze = Maze::from_str(input).expect("Format");
        maze.iterate_loop().count() as u64 / 2
    }

    fn part2(&self, input: &str) -> Option<Self::Output> {
        let maze = Maze::from_str(input).expect("Format");
        let (steps, area, _) =
            maze.iterate_loop()
                .fold((0, 0, maze.start_pos), |(steps, area, corner), pos| {
                    let char = maze[pos];
                    if char == '-' || char == '|' {
                        (steps + 1, area, corner)
                    } else {
                        (steps + 1, area + Point2D::determinant(corner, pos), pos)
                    }
                });

        Some(area.unsigned_abs() / 2 - steps / 2 + 1)
    }
}

pub const UP: Point2D<i64> = Point2D::new(-1, 0);
pub const DOWN: Point2D<i64> = Point2D::new(1, 0);
pub const LEFT: Point2D<i64> = Point2D::new(0, -1);
pub const RIGHT: Point2D<i64> = Point2D::new(0, 1);

struct Maze {
    grid: Vec<Vec<char>>,
    start_pos: Point2D<i64>,
}

impl Maze {
    fn start_direction(&self) -> Point2D<i64> {
        if matches!(self[self.start_pos + UP], '|' | '7' | 'F') {
            UP
        } else {
            DOWN
        }
    }

    fn iterate_loop(&self) -> impl Iterator<Item = Point2D<i64>> + '_ {
        successors(
            Some((
                self.start_pos,
                self.start_direction(),
                self.start_pos + self.start_direction(),
            )),
            |&(corner, direction, position)| {
                if self[position] == '-' || self[position] == '|' {
                    return Some((corner, direction, position + direction));
                }

                let next_direction = match self[position] {
                    '7' if direction == UP => LEFT,
                    'F' if direction == UP => RIGHT,
                    'J' if direction == DOWN => LEFT,
                    'L' if direction == DOWN => RIGHT,
                    'J' | 'L' => UP,
                    '7' | 'F' => DOWN,
                    _ => return None,
                };

                Some((position, next_direction, position + next_direction))
            },
        )
        .map(|(_, _, pos)| pos)
    }
}

impl Index<Point2D<i64>> for Maze {
    type Output = char;

    #[allow(clippy::cast_sign_loss, clippy::cast_possible_truncation)]
    fn index(&self, index: Point2D<i64>) -> &Self::Output {
        &self.grid[index.x as usize][index.y as usize]
    }
}

impl FromStr for Maze {
    type Err = String;

    #[allow(clippy::cast_possible_wrap)]
    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut start_pos = Point2D::default();
        let graph = s
            .lines()
            .enumerate()
            .map(|(row, line)| {
                line.chars()
                    .enumerate()
                    .map(|(col, char)| {
                        if char == 'S' {
                            start_pos = Point2D::new(row as i64, col as i64);
                        }
                        char
                    })
                    .collect::<Vec<_>>()
            })
            .collect::<Vec<_>>();

        Ok(Self {
            grid: graph,
            start_pos,
        })
    }
}

#[cfg(test)]
mod test {
    use super::*;
    use indoc::indoc;

    const SIMPLE_INPUT: &str = indoc! {"
        .....
        .S-7.
        .|.|.
        .L-J.
        .....
    "};

    const COMPLEX_INPUT: &str = indoc! {"
        ..F7.
        .FJ|.
        SJ.L7
        |F--J
        LJ...
    "};

    #[test]
    fn test_part1() {
        assert_eq!(Day10.part1(SIMPLE_INPUT), 4);
        assert_eq!(Day10.part1(COMPLEX_INPUT), 8);
    }

    const P2_COMPLEX_INPUT: &str = indoc! {"
        ...........
        .S-------7.
        .|F-----7|.
        .||.....||.
        .||.....||.
        .|L-7.F-J|.
        .|..|.|..|.
        .L--J.L--J.
        ...........
    "};

    #[test]
    fn test_part2() {
        assert_eq!(Day10.part2(SIMPLE_INPUT), Some(1));
        assert_eq!(Day10.part2(P2_COMPLEX_INPUT), Some(4));
    }
}
