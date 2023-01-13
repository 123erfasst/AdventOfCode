use std::{collections::HashSet, iter::repeat};

use crate::helper::solution::{Solution, SolutionPair};

#[derive(Clone)]
enum Step {
    Right,
    Up,
    Down,
    Left,
}

use Step::{Down, Left, Right, Up};

#[derive(Debug, Hash, PartialEq, Eq, Clone, Copy)]
struct Position(i32, i32);

#[derive(Debug)]
struct Board {
    knots: Vec<Position>,
    visited: HashSet<Position>,
}

pub fn solve() -> SolutionPair {
    let input = include_str!("./input_day09.txt");

    (solve_part_1(input), solve_part_2(input))
}

fn solve_part_1(input: &'static str) -> Option<Solution> {
    let steps = parse_steps(input);
    let mut initial_board = Board {
        knots: vec![Position(0, 0), Position(0, 0)],
        visited: HashSet::new(),
    };
    initial_board.visited.insert(Position(0, 0));

    let end_board = steps.iter().fold(initial_board, |mut acc, item| {
        let mut new_knots = acc.knots.to_vec();
        new_knots[0] = calculate_step(new_knots[0], item);

        for i in 1..new_knots.len() {
            let head = new_knots[i - 1];
            let tail = new_knots[i];
            new_knots[i] = calculate_move(&head, &tail);
            acc.visited.insert(Position(tail.0, tail.1));
        }

        Board {
            knots: new_knots,
            visited: acc.visited,
        }
    });

    let result = end_board.visited.len();

    Some(Solution::Usize(result))
}

fn solve_part_2(input: &'static str) -> Option<Solution> {
    let steps = parse_steps(input);
    let mut initial_board = Board {
        knots: vec![Position(0, 0); 10],
        visited: HashSet::new(),
    };
    initial_board.visited.insert(Position(0, 0));

    let end_board = steps.iter().fold(initial_board, |mut acc, item| {
        let mut new_knots = acc.knots.to_vec();
        new_knots[0] = calculate_step(new_knots[0], item);

        for i in 1..new_knots.len() {
            let head = new_knots[i - 1];
            let tail = new_knots[i];
            new_knots[i] = calculate_move(&head, &tail);
        }

        acc.visited.insert(*new_knots.last().unwrap());

        Board {
            knots: new_knots,
            visited: acc.visited,
        }
    });

    let result = end_board.visited.len();

    Some(Solution::Usize(result))
}

fn parse_steps(input: &'static str) -> Vec<Step> {
    input
        .lines()
        .flat_map(|line| match line.split_once(" ") {
            Some(("R", amount)) => repeat(Right).take(amount.parse::<usize>().unwrap_or(0)),
            Some(("U", amount)) => repeat(Up).take(amount.parse::<usize>().unwrap_or(0)),
            Some(("L", amount)) => repeat(Left).take(amount.parse::<usize>().unwrap_or(0)),
            Some(("D", amount)) => repeat(Down).take(amount.parse::<usize>().unwrap_or(0)),
            _ => panic!("Something wrong"),
        })
        .collect()
}

fn calculate_step(head_position: Position, step: &Step) -> Position {
    match step {
        Up => Position(head_position.0, head_position.1 + 1),
        Down => Position(head_position.0, head_position.1 - 1),
        Right => Position(head_position.0 + 1, head_position.1),
        Left => Position(head_position.0 - 1, head_position.1),
    }
}

fn calculate_move(head: &Position, tail: &Position) -> Position {
    let x_diff = (head.0 - tail.0).abs();
    let y_diff = (head.1 - tail.1).abs();

    let new_tail_position = {
        if x_diff > 1 || y_diff > 1 {
            let x_sign = (head.0 - tail.0).signum();
            let y_sign = (head.1 - tail.1).signum();
            Position(tail.0 + x_sign, tail.1 + y_sign)
        } else {
            Position(tail.0, tail.1)
        }
    };

    new_tail_position
}
