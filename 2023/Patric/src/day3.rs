use crate::Solution;
use fxhash::{FxBuildHasher, FxHasher};
use std::collections::HashMap;
use std::hash::BuildHasherDefault;
use std::ops::Add;

pub struct Day3;

impl Solution<3> for Day3 {
    type Output = u32;

    fn part1(&self, input: &str) -> Self::Output {
        let symbols = parse_symbols_with_adjacent(input);
        symbols.values().flatten().sum()
    }

    fn part2(&self, input: &str) -> Option<Self::Output> {
        let symbols = parse_symbols_with_adjacent(input);
        let ratio_sum = symbols
            .iter()
            .filter(|(&(_, s), v)| s == '*' && v.len() == 2)
            .map(|(_, v)| v[0] * v[1])
            .sum();
        Some(ratio_sum)
    }
}

fn parse_symbols_with_adjacent(
    input: &str,
) -> HashMap<(Position, char), Vec<u32>, BuildHasherDefault<FxHasher>> {
    let lines = input.lines().map(str::as_bytes).collect::<Vec<_>>();
    let mut symbols = HashMap::with_hasher(FxBuildHasher::default());
    for (row, line) in lines.iter().enumerate() {
        let mut col = 0;
        while col < line.len() {
            let (start_col, mut symbol) = (col, None);
            while col < line.len() && line[col].is_ascii_digit() {
                symbol = symbol.or_else(|| find_symbol(&lines, &Position(row, col)));
                col += 1;
            }
            if let Some(symbol_key) = symbol {
                let num = std::str::from_utf8(&line[start_col..col])
                    .expect("Failed to read number")
                    .parse::<u32>()
                    .expect("Failed to parse number");
                symbols.entry(symbol_key).or_insert(Vec::new()).push(num);
            }
            col += 1;
        }
    }
    symbols
}

fn find_symbol(input: &[&[u8]], position: &Position) -> Option<(Position, char)> {
    for offset in NEIGHBORS {
        let next @ Position(n_row, n_col) = position + offset;
        let Some(&c) = input.get(n_row).and_then(|l| l.get(n_col)) else {
            continue;
        };
        if c != b'.' && !c.is_ascii_digit() {
            return Some((next, c as char));
        }
    }
    None
}

const NEIGHBORS: [Offset; 8] = [
    Offset(-1, -1),
    Offset(-1, 0),
    Offset(-1, 1),
    Offset(0, -1),
    Offset(0, 1),
    Offset(1, -1),
    Offset(1, 0),
    Offset(1, 1),
];

#[derive(Eq, PartialEq, Hash)]
struct Position(usize, usize);

struct Offset(isize, isize);

impl Add<Offset> for &Position {
    type Output = Position;

    #[allow(clippy::cast_sign_loss, clippy::cast_possible_wrap)]
    fn add(self, rhs: Offset) -> Self::Output {
        Position(
            (self.0 as isize + rhs.0) as usize,
            (self.1 as isize + rhs.1) as usize,
        )
    }
}

#[cfg(test)]
mod test {
    use super::*;
    use indoc::indoc;

    const TEST_INPUT: &str = indoc! {"
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...$.*....
        .664.598..
    "};

    #[test]
    fn test_part1() {
        assert_eq!(Day3.part1(TEST_INPUT), 4361);
    }

    #[test]
    fn test_part2() {
        assert_eq!(Day3.part2(TEST_INPUT), Some(467_835));
    }
}
