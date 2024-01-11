use crate::util::point::Point2D;
use crate::Solution;
use nom::branch::alt;
use nom::bytes::complete::tag;
use nom::character::complete;
use nom::character::complete::{alphanumeric1, line_ending, space1};
use nom::combinator::{iterator, map};
use nom::sequence::{delimited, terminated, tuple};
use nom::IResult;

pub struct Day18;

impl Solution<18> for Day18 {
    type Output = i64;

    fn part1(&self, input: &str) -> Self::Output {
        lava_volume(&mut iterator(input, Move::parse_one))
    }

    fn part2(&self, input: &str) -> Option<Self::Output> {
        Some(lava_volume(&mut iterator(input, Move::parse_two)))
    }
}

fn lava_volume(moves: impl Iterator<Item = Move>) -> i64 {
    let (_, area, perimeter) = moves.fold(
        (Point::default(), 0, 0),
        |(previous, area, perimeter), m| {
            let position = previous + m.direction * m.distance;
            (
                position,
                area + Point::determinant(previous, position),
                perimeter + m.distance,
            )
        },
    );

    area / 2 + perimeter / 2 + 1
}

type Point = Point2D<i64>;

struct Move {
    direction: Point,
    distance: i64,
}

impl Move {
    fn parse_one(input: &str) -> IResult<&str, Self> {
        Self::parse(input, |(direction, distance, _)| Self {
            direction,
            distance,
        })
    }

    fn parse_two(input: &str) -> IResult<&str, Self> {
        Self::parse(input, |(_, _, instruction)| {
            let direction = match instruction.as_bytes()[5] {
                b'0' => Point::new(1, 0),
                b'1' => Point::new(0, 1),
                b'2' => Point::new(-1, 0),
                b'3' => Point::new(0, -1),
                _ => unreachable!(),
            };
            let distance = i64::from_str_radix(&instruction[..5], 16).expect("Format");
            Self {
                direction,
                distance,
            }
        })
    }

    fn parse<F>(input: &str, transform: F) -> IResult<&str, Self>
    where
        F: FnMut((Point2D<i64>, i64, &str)) -> Self,
    {
        let direction = map(
            alt((
                complete::char('U'),
                complete::char('R'),
                complete::char('D'),
                complete::char('L'),
            )),
            |dir| match dir {
                'U' => Point::new(0, -1),
                'R' => Point::new(1, 0),
                'D' => Point::new(0, 1),
                'L' => Point::new(-1, 0),
                _ => unreachable!(),
            },
        );

        let color = delimited(tag("(#"), alphanumeric1, complete::char(')'));

        map(
            tuple((
                terminated(direction, space1),
                terminated(complete::i64, space1),
                terminated(color, line_ending),
            )),
            transform,
        )(input)
    }
}

#[cfg(test)]
mod test {
    use super::*;
    use indoc::indoc;

    const TEST_INPUT: &str = indoc! {"
        R 6 (#70c710)
        D 5 (#0dc571)
        L 2 (#5713f0)
        D 2 (#d2c081)
        R 2 (#59c680)
        D 2 (#411b91)
        L 5 (#8ceee2)
        U 2 (#caa173)
        L 1 (#1b58a2)
        U 2 (#caa171)
        R 2 (#7807d2)
        U 3 (#a77fa3)
        L 2 (#015232)
        U 2 (#7a21e3)
    "};

    #[test]
    fn test_part1() {
        assert_eq!(Day18.part1(TEST_INPUT), 62);
    }

    #[test]
    fn test_part2() {
        assert_eq!(Day18.part2(TEST_INPUT), Some(952_408_144_115));
    }
}
