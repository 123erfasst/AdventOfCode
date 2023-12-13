use crate::Solution;
use rayon::prelude::*;
use std::iter::repeat;
use std::str::FromStr;

pub struct Day12;

impl Solution<12> for Day12 {
    type Output = u64;

    fn part1(&self, input: &str) -> Self::Output {
        input
            .par_lines()
            .filter_map(|line| Record::from_str(line).ok())
            .map(|record| record.possible_arrangements())
            .sum()
    }

    fn part2(&self, input: &str) -> Option<Self::Output> {
        let sum = input
            .par_lines()
            .filter_map(|line| Record::from_str(line).ok())
            .map(|record| record.unfold(5))
            .map(|record| record.possible_arrangements())
            .sum();

        Some(sum)
    }
}

struct Record {
    springs: Vec<Spring>,
    groups: Vec<u32>,
}

impl Record {
    fn unfold(&self, factor: usize) -> Self {
        Self {
            springs: repeat(&self.springs)
                .take(factor)
                .cloned()
                .reduce(|mut acc, mut springs| {
                    acc.push(Spring::Unknown);
                    acc.append(&mut springs);
                    acc
                })
                .unwrap_or_default(),
            groups: repeat(&self.groups)
                .take(factor)
                .flatten()
                .copied()
                .collect(),
        }
    }

    fn possible_arrangements(&self) -> u64 {
        use Spring::{Damaged, Operational};

        let mut springs = self.springs.clone();
        springs.insert(0, Operational);

        let mut cache = vec![0; springs.len() + 1];
        let mut next = vec![0; springs.len() + 1];
        cache[0] = 1;

        for (i, _) in springs.iter().take_while(|&&c| c != Damaged).enumerate() {
            cache[i] = 1;
        }

        for &group_size in &self.groups {
            let mut group = 0;

            for (i, &spring) in springs.iter().enumerate() {
                if spring == Operational {
                    group = 0;
                } else {
                    group += 1;
                }

                if spring != Damaged {
                    next[i + 1] += next[i];
                }

                if group >= group_size && springs[i - group_size as usize] != Damaged {
                    next[i + 1] += cache[i - group_size as usize];
                }
            }

            cache.iter_mut().for_each(|c| *c = 0);
            std::mem::swap(&mut cache, &mut next);
        }

        *cache.last().unwrap()
    }
}

impl FromStr for Record {
    type Err = ();

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        use nom::character::complete::u32;
        use nom::character::complete::{char, one_of, space1};
        use nom::combinator::map;
        use nom::multi::{many1, separated_list1};
        use nom::sequence::separated_pair;

        map(
            separated_pair(
                many1(map(
                    one_of::<&str, &str, nom::error::Error<&str>>(".#?"),
                    |c| match c {
                        '.' => Spring::Operational,
                        '#' => Spring::Damaged,
                        '?' => Spring::Unknown,
                        _ => unreachable!(),
                    },
                )),
                space1,
                separated_list1(char(','), u32),
            ),
            |(springs, groups)| Record { springs, groups },
        )(s)
        .map(|r| r.1)
        .map_err(|_| ())
    }
}

#[derive(Debug, Eq, PartialEq, Copy, Clone)]
enum Spring {
    Operational,
    Damaged,
    Unknown,
}

#[cfg(test)]
mod test {
    use super::*;
    use indoc::indoc;

    const TEST_INPUT: &str = indoc! {"
        ???.### 1,1,3
        .??..??...?##. 1,1,3
        ?#?#?#?#?#?#?#? 1,3,1,6
        ????.#...#... 4,1,1
        ????.######..#####. 1,6,5
        ?###???????? 3,2,1
    "};

    #[test]
    fn test_part1() {
        assert_eq!(Day12.part1(TEST_INPUT), 21);
    }

    #[test]
    fn test_part2() {
        assert_eq!(Day12.part2(TEST_INPUT), Some(525_152));
    }
}
