use crate::helper::solution::{Solution, SolutionPair};

pub fn solve() -> SolutionPair {
    let input = include_str!("./input_day01.txt");

    (solve_part_1(input), solve_part_2(input))
}

fn solve_part_1(input: &'static str) -> Option<Solution> {
    let trees = parse_lines(input)
        .iter()
        .enumerate()
        .map(|(x_index, x_row)| x_row.iter().enumerate().filter(|(y_index, _)| false));
}

fn solve_part_2(input: &'static str) -> Option<Solution> {
    None
}

fn parse_lines(input: &'static str) -> Vec<Vec<u32>> {
    input
        .lines()
        .map(|line| line.chars().filter_map(|c| c.to_digit(10)).collect())
        .collect()
}

fn is_highest_tree(x: usize, y: usize, trees: Vec<Vec<u32>>) -> bool {
    let tree = trees[x][y];
    let x_range = trees.len() - 1;
    let y_range = trees[x].len() - 1;
    let positive_x = x..x_range;
    let negative_x = 0..x;
    let positive_y = y..y_range;
    let negative_y = 0..y;

    positive_x.any(|new_x| trees[new_x][y] >=)
}
