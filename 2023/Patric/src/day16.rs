use crate::util::direction::Direction;
use crate::util::point::Point2D;
use crate::Solution;
use nom::branch::alt;
use nom::character::complete;
use nom::character::complete::line_ending;
use nom::combinator::{map, value};
use nom::multi::{many1, separated_list1};
use nom::IResult;
use rayon::prelude::*;
use std::ops::Index;

pub struct Day16;

impl Solution<16> for Day16 {
    type Output = usize;

    fn part1(&self, input: &str) -> Self::Output {
        let grid = Grid::parse(input).expect("Format").1;
        grid.iterate(Point::new(0, 0), Direction::Right)
    }

    fn part2(&self, input: &str) -> Option<Self::Output> {
        let grid = Grid::parse(input).expect("Format").1;
        let top = (0..grid.width).map(|x| (Point::new(x, 0), Direction::Down));
        let bottom = (0..grid.width).map(|x| (Point::new(x, grid.height - 1), Direction::Up));
        let left = (0..grid.height).map(|y| (Point::new(0, y), Direction::Right));
        let right = (0..grid.height).map(|y| (Point::new(grid.width - 1, y), Direction::Left));

        top.chain(bottom)
            .chain(left)
            .chain(right)
            .collect::<Vec<_>>()
            .into_par_iter()
            .map(|(start, dir)| grid.iterate(start, dir))
            .max()
    }
}

type Point = Point2D<usize>;

struct Grid {
    cells: Vec<Vec<Cell>>,
    width: usize,
    height: usize,
}

impl Grid {
    fn parse(input: &str) -> IResult<&str, Self> {
        map(separated_list1(line_ending, many1(Cell::parse)), |cells| {
            Self {
                width: cells[0].len(),
                height: cells.len(),
                cells,
            }
        })(input)
    }

    fn iterate(&self, start: Point, direction: Direction) -> usize {
        let mut seen = vec![vec![0u8; self.width]; self.height];
        let mut queue = Vec::with_capacity(4 * self.width * self.height);

        for dir in Self::next_direction(self[start], direction) {
            seen[start.y][start.x] |= dir.flag();
            queue.push((start, dir));
        }

        while let Some((pos, dir)) = queue.pop() {
            if let Some(next) = self.step(&pos, dir) {
                for next_dir in Self::next_direction(self[next], dir) {
                    if seen[next.y][next.x] & next_dir.flag() == 0 {
                        seen[next.y][next.x] |= next_dir.flag();
                        queue.push((next, next_dir));
                    }
                }
            }
        }

        seen.iter().flatten().filter(|&&dirs| dirs != 0).count()
    }

    #[allow(clippy::cast_possible_wrap, clippy::cast_sign_loss)]
    fn step(&self, position: &Point2D<usize>, direction: Direction) -> Option<Point> {
        let x = position.x as isize;
        let y = position.y as isize;

        let (x, y) = match direction {
            Direction::Up => (x, y - 1),
            Direction::Right => (x + 1, y),
            Direction::Down => (x, y + 1),
            Direction::Left => (x - 1, y),
        };

        if x < 0 || x >= self.width as isize || y < 0 || y >= self.height as isize {
            None
        } else {
            Some(Point::new(x as usize, y as usize))
        }
    }

    fn next_direction(cell: Cell, direction: Direction) -> Vec<Direction> {
        use self::Mirror::{Backward, Forward};
        use self::Splitter::{Horizontal, Vertical};
        use Cell::{Empty, Mirror, Splitter};
        use Direction::{Down, Left, Right, Up};

        match (cell, direction) {
            (Mirror(Forward), Right) | (Mirror(Backward), Left) => vec![Up],
            (Mirror(Forward), Left) | (Mirror(Backward), Right) => vec![Down],
            (Mirror(Forward), Down) | (Mirror(Backward), Up) => vec![Left],
            (Mirror(Forward), Up) | (Mirror(Backward), Down) => vec![Right],
            (Splitter(Vertical), Left | Right) => vec![Up, Down],
            (Splitter(Horizontal), Up | Down) => vec![Left, Right],
            (Splitter(Vertical), Up | Down) | (Empty, _) | (Splitter(Horizontal), Left | Right) => {
                vec![direction]
            }
        }
    }
}

impl Index<Point> for Grid {
    type Output = Cell;

    fn index(&self, index: Point) -> &Self::Output {
        &self.cells[index.y][index.x]
    }
}

#[derive(Debug, Eq, PartialEq, Copy, Clone)]
enum Cell {
    Empty,
    Mirror(Mirror),
    Splitter(Splitter),
}

impl Cell {
    fn parse(input: &str) -> IResult<&str, Self> {
        alt((
            value(Self::Empty, complete::char('.')),
            map(Mirror::parse, Self::Mirror),
            map(Splitter::parse, Self::Splitter),
        ))(input)
    }
}

#[derive(Debug, Eq, PartialEq, Copy, Clone)]
enum Mirror {
    Forward,
    Backward,
}

impl Mirror {
    fn parse(input: &str) -> IResult<&str, Self> {
        alt((
            value(Self::Forward, complete::char('/')),
            value(Self::Backward, complete::char('\\')),
        ))(input)
    }
}

#[derive(Debug, Eq, PartialEq, Copy, Clone)]
enum Splitter {
    Horizontal,
    Vertical,
}

impl Splitter {
    fn parse(input: &str) -> IResult<&str, Self> {
        alt((
            value(Self::Horizontal, complete::char('-')),
            value(Self::Vertical, complete::char('|')),
        ))(input)
    }
}

#[cfg(test)]
mod test {
    use super::*;
    use indoc::indoc;

    const TEST_INPUT: &str = indoc! {r"
        .|...\....
        |.-.\.....
        .....|-...
        ........|.
        ..........
        .........\
        ..../.\\..
        .-.-/..|..
        .|....-|.\
        ..//.|....
    "};

    #[test]
    fn test_part1() {
        assert_eq!(Day16.part1(TEST_INPUT), 46);
    }

    #[test]
    fn test_part2() {
        assert_eq!(Day16.part2(TEST_INPUT), Some(51));
    }
}
