use crate::util::grid::Grid;
use crate::util::point::Point2D;
use crate::Solution;
use std::collections::VecDeque;

use bucket_queue::bucket_queue::*;
use bucket_queue::FirstInFirstOutQueue;

pub struct Day17;

impl Solution<17> for Day17 {
    type Output = u32;

    fn part1(&self, input: &str) -> Self::Output {
        let (_, city) = Grid::parse_transform(input, |c| {
            c.to_string().parse::<u32>().expect("Number format")
        })
        .expect("Format");

        cheapest_path::<1, 3>(&city)
    }

    fn part2(&self, input: &str) -> Option<Self::Output> {
        let (_, city) = Grid::parse_transform(input, |c| {
            c.to_string().parse::<u32>().expect("Number format")
        })
        .expect("Format");

        Some(cheapest_path::<4, 10>(&city))
    }
}

type Point = Point2D<usize>;

fn cheapest_path<const MIN: usize, const MAX: usize>(grid: &Grid<u32>) -> u32 {
    use Axis::{Horizontal, Vertical};
    let target = Point::new(grid.width - 1, grid.height - 1);
    let mut queue = BucketQueue::<VecDeque<_>>::new();
    let mut cost = Grid::new(grid.width, grid.height, [0_u32; 2]);

    queue.enqueue((Point::new(0, 0), Horizontal), 0);
    queue.enqueue((Point::new(0, 0), Vertical), 0);

    while let Some((pos, direction)) = queue.dequeue_min() {
        if pos == target {
            return cost[&pos][direction.ordinal()];
        }
        if direction == Horizontal {
            step_next::<MIN, MAX, _, _>(
                grid, &mut cost, &mut queue, &pos, Horizontal, move_l, check_l,
            );
            step_next::<MIN, MAX, _, _>(
                grid, &mut cost, &mut queue, &pos, Horizontal, move_r, check_r,
            );
        } else {
            step_next::<MIN, MAX, _, _>(
                grid, &mut cost, &mut queue, &pos, Vertical, move_u, check_u,
            );
            step_next::<MIN, MAX, _, _>(
                grid, &mut cost, &mut queue, &pos, Vertical, move_d, check_d,
            );
        }
    }

    unreachable!("Cheapest path must exist")
}

fn step_next<const MIN: usize, const MAX: usize, F, P>(
    grid: &Grid<u32>,
    cost: &mut Grid<[u32; 2]>,
    queue: &mut BucketQueue<VecDeque<(Point2D<usize>, Axis)>>,
    pos: &Point2D<usize>,
    axis: Axis,
    next_pos: F,
    bounds_predicate: P,
) where
    F: Fn(&Point, usize) -> Point,
    P: Fn(&Grid<u32>, &Point, usize) -> bool,
{
    let target = Point::new(grid.width, grid.height);
    let mut steps = cost[&pos][axis.ordinal()];
    for i in (1..=MAX).take_while(|&i| bounds_predicate(grid, pos, i)) {
        let next_pos = next_pos(pos, i);
        let next_axis = axis.rotate();
        steps += grid[&next_pos];

        let next_cost = &mut cost[&next_pos][next_axis.ordinal()];
        let is_unseen = *next_cost == 0;
        let is_cheaper = steps < *next_cost;
        if i >= MIN && (is_unseen || is_cheaper) {
            queue.enqueue((next_pos, next_axis), heuristic(&next_pos, &target, steps));
            *next_cost = steps;
        }
    }
}

const fn move_l(point: &Point, steps: usize) -> Point {
    Point::new(point.x - steps, point.y)
}

const fn move_r(point: &Point, steps: usize) -> Point {
    Point::new(point.x + steps, point.y)
}

const fn move_u(point: &Point, steps: usize) -> Point {
    Point::new(point.x, point.y - steps)
}

const fn move_d(point: &Point, steps: usize) -> Point {
    Point::new(point.x, point.y + steps)
}

const fn check_l(_grid: &Grid<u32>, point: &Point, steps: usize) -> bool {
    steps <= point.x
}

const fn check_r(grid: &Grid<u32>, point: &Point, steps: usize) -> bool {
    point.x + steps < grid.width
}

const fn check_u(_grid: &Grid<u32>, point: &Point, steps: usize) -> bool {
    steps <= point.y
}

const fn check_d(grid: &Grid<u32>, point: &Point, steps: usize) -> bool {
    point.y + steps < grid.height
}

fn heuristic(point: &Point, target: &Point, cost: u32) -> usize {
    cost as usize + point.manhattan_distance(target)
}

#[derive(Debug, Eq, PartialEq, Hash, Copy, Clone)]
#[repr(u8)]
pub enum Axis {
    Horizontal = 0,
    Vertical = 1,
}

impl Axis {
    #[inline]
    #[must_use]
    pub const fn rotate(self) -> Axis {
        match self {
            Axis::Horizontal => Axis::Vertical,
            Axis::Vertical => Axis::Horizontal,
        }
    }

    #[inline]
    #[must_use]
    pub const fn ordinal(self) -> usize {
        self as usize
    }
}

#[cfg(test)]
mod test {
    use super::*;
    use indoc::indoc;

    const TEST_INPUT: &str = indoc! {"
        2413432311323
        3215453535623
        3255245654254
        3446585845452
        4546657867536
        1438598798454
        4457876987766
        3637877979653
        4654967986887
        4564679986453
        1224686865563
        2546548887735
        4322674655533
    "};

    const SECOND_TEST_INPUT: &str = indoc! {"
        111111111111
        999999999991
        999999999991
        999999999991
        999999999991
    "};

    #[test]
    fn test_part1() {
        assert_eq!(Day17.part1(TEST_INPUT), 102);
    }

    #[test]
    fn test_part2() {
        assert_eq!(Day17.part2(TEST_INPUT), Some(94));
        assert_eq!(Day17.part2(SECOND_TEST_INPUT), Some(71));
    }
}
