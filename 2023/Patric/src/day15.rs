use crate::Solution;
use nom::branch::alt;
use nom::character::complete;
use nom::character::complete::alpha1;
use nom::combinator::{map, opt};
use nom::sequence::separated_pair;
use nom::IResult;

pub struct Day15;

impl Solution<15> for Day15 {
    type Output = usize;

    fn part1(&self, input: &str) -> Self::Output {
        input
            .trim_end_matches("\n")
            .split(',')
            .map(hash)
            .map(|h| h as usize)
            .sum()
    }

    fn part2(&self, input: &str) -> Option<Self::Output> {
        let boxes = input
            .trim_end_matches("\n")
            .split(',')
            .map(|instr| Instruction::parse(instr).expect("Format").1)
            .fold(vec![vec![]; 256], |mut boxes, instruction| {
                instruction.apply_to(&mut boxes);
                boxes
            });

        let power = boxes
            .iter()
            .enumerate()
            .flat_map(|(box_n, a_box)| {
                a_box
                    .iter()
                    .enumerate()
                    .map(move |(slot_n, lens)| (box_n, slot_n, lens.focal_length))
            })
            .map(|(box_n, slot_n, length)| (box_n + 1) * (slot_n + 1) * length as usize)
            .sum();

        Some(power)
    }
}

struct Instruction<'a> {
    label: &'a str,
    operation: Operation,
}

#[derive(Copy, Clone)]
struct Lens<'a> {
    label: &'a str,
    focal_length: u8,
}

impl<'a> Instruction<'a> {
    fn apply_to(&self, boxes: &mut Vec<Vec<Lens<'a>>>) {
        let a_box = &mut boxes[hash(self.label) as usize];
        let lens_position = a_box
            .iter()
            .position(|&Lens { label, .. }| label == self.label);
        match (&self.operation, lens_position) {
            (Operation::Set(length), Some(i)) => {
                a_box[i] = Lens {
                    label: self.label,
                    focal_length: *length,
                }
            }
            (Operation::Set(length), None) => a_box.push(Lens {
                label: self.label,
                focal_length: *length,
            }),
            (Operation::Remove, Some(i)) => {
                a_box.remove(i);
            }
            (Operation::Remove, None) => (),
        }
    }

    fn parse(input: &'a str) -> IResult<&str, Instruction> {
        map(
            separated_pair(
                alpha1,
                alt((complete::char('-'), complete::char('='))),
                opt(complete::u8),
            ),
            |(label, length): (&str, Option<u8>)| Self {
                label,
                operation: match length {
                    Some(l) => Operation::Set(l),
                    None => Operation::Remove,
                },
            },
        )(input)
    }
}

enum Operation {
    Remove,
    Set(u8),
}

fn hash(s: &str) -> u8 {
    s.bytes()
        .fold(0u8, |hash, c| hash.wrapping_add(c).wrapping_mul(17))
}

#[cfg(test)]
mod test {
    use super::*;
    use indoc::indoc;

    const TEST_INPUT: &str = indoc! {"
        rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
    "};

    #[test]
    fn test_part1() {
        assert_eq!(Day15.part1(TEST_INPUT), 1320);
    }

    #[test]
    fn test_part2() {
        assert_eq!(Day15.part2(TEST_INPUT), Some(145));
    }
}
