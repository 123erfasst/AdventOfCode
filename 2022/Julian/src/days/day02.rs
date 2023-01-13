use std::str::FromStr;

use crate::helper::solution::{Solution, SolutionPair};

enum FirstPick {
    A,
    B,
    C,
}

impl FromStr for FirstPick {
    type Err = ();

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        match s {
            "A" => Ok(Self::A),
            "B" => Ok(Self::B),
            "C" => Ok(Self::C),
            _ => panic!("illegal pick"),
        }
    }
}

enum SecondPick {
    X,
    Y,
    Z,
}

impl FromStr for SecondPick {
    type Err = ();

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        match s {
            "X" => Ok(Self::X),
            "Y" => Ok(Self::Y),
            "Z" => Ok(Self::Z),
            _ => panic!("illegal pick"),
        }
    }
}

struct Round(FirstPick, SecondPick);

impl Round {
    fn get_score_rnd1(self: Self) -> i32 {
        let shape_score = match self.1 {
            SecondPick::X => 1,
            SecondPick::Y => 2,
            SecondPick::Z => 3,
        };

        let match_score = match (self.0, self.1) {
            (FirstPick::A, SecondPick::X) => 3,
            (FirstPick::B, SecondPick::Y) => 3,
            (FirstPick::C, SecondPick::Z) => 3,
            (FirstPick::A, SecondPick::Y) => 6,
            (FirstPick::B, SecondPick::Z) => 6,
            (FirstPick::C, SecondPick::X) => 6,
            (FirstPick::A, SecondPick::Z) => 0,
            (FirstPick::B, SecondPick::X) => 0,
            (FirstPick::C, SecondPick::Y) => 0,
        };

        shape_score + match_score
    }

    fn get_score_rnd2(self: Self) -> i32 {
        match (self.0, self.1) {
            (FirstPick::A, SecondPick::X) => 3 + 0,
            (FirstPick::B, SecondPick::X) => 1 + 0,
            (FirstPick::C, SecondPick::X) => 2 + 0,
            (FirstPick::A, SecondPick::Y) => 1 + 3,
            (FirstPick::B, SecondPick::Y) => 2 + 3,
            (FirstPick::C, SecondPick::Y) => 3 + 3,
            (FirstPick::C, SecondPick::Z) => 1 + 6,
            (FirstPick::A, SecondPick::Z) => 2 + 6,
            (FirstPick::B, SecondPick::Z) => 3 + 6,
        }
    }
}

pub fn solve() -> SolutionPair {
    let input = include_str!("./input_day02.txt");

    (solve_part_1(input), solve_part_2(input))
}

fn solve_part_1(input: &'static str) -> Option<Solution> {
    Some(Solution::I32(
        input
            .lines()
            .map(|line| {
                let picks: Vec<&str> = line.split_whitespace().collect();
                let first = FirstPick::from_str(picks[0]).unwrap();
                let second = SecondPick::from_str(picks[1]).unwrap();
                let result = Round(first, second).get_score_rnd1();
                result
            })
            .sum(),
    ))
}

fn solve_part_2(input: &'static str) -> Option<Solution> {
    Some(Solution::I32(
        input
            .lines()
            .map(|line| {
                let picks: Vec<&str> = line.split_whitespace().collect();
                let first = FirstPick::from_str(picks[0]).unwrap();
                let second = SecondPick::from_str(picks[1]).unwrap();
                let result = Round(first, second).get_score_rnd2();
                result
            })
            .sum(),
    ))
}
