use crate::Solution;
use itertools::Itertools;
use nom::bytes::complete::tag;
use nom::character::complete::{alpha1, char, line_ending, space1, u64 as n_u64};
use nom::combinator::map;
use nom::multi::{many1, separated_list1};
use nom::sequence::{delimited, preceded, separated_pair, tuple};
use nom::IResult;

pub struct Day5;

impl Solution<5> for Day5 {
    type Output = u64;

    fn part1(&self, input: &str) -> Self::Output {
        let (seeds, maps) = parse_input(input).expect("Format mismatch").1;

        let locations = maps.iter().fold(seeds, |seeds, mappings| {
            seeds
                .into_iter()
                .map(|seed| {
                    mappings
                        .iter()
                        .find(|mapping| mapping.should_map(seed))
                        .map_or(seed, |mapping| mapping.offset(seed))
                })
                .collect()
        });
        *locations.iter().min().expect("Locations was empty")
    }

    fn part2(&self, input: &str) -> Option<Self::Output> {
        let (seeds, maps) = parse_input(input).expect("Format mismatch").1;
        let seeds_ranges = seeds
            .into_iter()
            .tuples()
            .map(|(start, length)| (start, start + length))
            .collect::<Vec<_>>();

        let locations = iterate_seeds(&seeds_ranges, &maps);
        Some(
            locations
                .iter()
                .map(|range| range.0)
                .min()
                .expect("Locations was empty"),
        )
    }
}

fn iterate_seeds(seeds_ranges: &[SeedRange], maps: &[Vec<Mapping>]) -> Vec<SeedRange> {
    maps.iter()
        .fold(seeds_ranges.to_vec(), |seed_ranges, mappings| {
            seed_ranges
                .into_iter()
                .flat_map(|seed_range| map_seed_range(seed_range, mappings))
                .collect::<Vec<_>>()
        })
}

fn map_seed_range(seed_range: SeedRange, mappings: &[Mapping]) -> Vec<SeedRange> {
    let (mut mapped, mut unmapped, mut next_unmapped) = (vec![], vec![seed_range], vec![]);
    for mapping in mappings {
        for seed in unmapped.drain(..) {
            let (pre_interval, mapped_interval, post_interval) = construct_intervals(seed, mapping);

            if pre_interval.0 < pre_interval.1 {
                next_unmapped.push(pre_interval);
            }
            if mapped_interval.0 < mapped_interval.1 {
                mapped.push((
                    mapping.offset(mapped_interval.0),
                    mapping.offset(mapped_interval.1),
                ));
            }
            if post_interval.0 < post_interval.1 {
                next_unmapped.push(post_interval);
            }
        }
        unmapped.append(&mut next_unmapped);
    }
    mapped.append(&mut unmapped);
    mapped
}

fn construct_intervals(
    (seed_begin, seed_end): SeedRange,
    mapping: &Mapping,
) -> (SeedRange, SeedRange, SeedRange) {
    let pre_interval = (seed_begin, seed_end.min(mapping.source_start));
    let mapped_interval = (
        seed_begin.max(mapping.source_start),
        seed_end.min(mapping.source_end()),
    );
    let post_interval = (seed_begin.max(mapping.source_end()), seed_end);

    (pre_interval, mapped_interval, post_interval)
}

type SeedRange = (u64, u64);

fn parse_input(input: &str) -> IResult<&str, (Vec<u64>, Vec<Vec<Mapping>>)> {
    separated_pair(
        parse_seeds,
        many1(line_ending),
        separated_list1(many1(line_ending), parse_mappings),
    )(input)
}

fn parse_seeds(input: &str) -> IResult<&str, Vec<u64>> {
    preceded(
        tuple((tag("seeds:"), space1)),
        separated_list1(space1, n_u64),
    )(input)
}

fn parse_mappings(input: &str) -> IResult<&str, Vec<Mapping>> {
    map(
        preceded(
            tuple((
                separated_list1(char('-'), alpha1),
                space1,
                tag("map:"),
                line_ending,
            )),
            separated_list1(
                line_ending,
                tuple((n_u64, delimited(space1, n_u64, space1), n_u64)),
            ),
        ),
        |mappings| {
            mappings
                .into_iter()
                .map(|(target_start, source_start, range)| Mapping {
                    target_start,
                    source_start,
                    range,
                })
                .collect()
        },
    )(input)
}

struct Mapping {
    target_start: u64,
    source_start: u64,
    range: u64,
}

impl Mapping {
    const fn source_end(&self) -> u64 {
        self.source_start + self.range
    }

    fn should_map(&self, seed: u64) -> bool {
        (self.source_start..self.source_end()).contains(&seed)
    }

    const fn offset(&self, seed: u64) -> u64 {
        self.target_start + seed - self.source_start
    }
}

#[cfg(test)]
mod test {
    use super::*;
    use indoc::indoc;

    const TEST_INPUT: &str = indoc! {"
        seeds: 79 14 55 13

        seed-to-soil map:
        50 98 2
        52 50 48

        soil-to-fertilizer map:
        0 15 37
        37 52 2
        39 0 15

        fertilizer-to-water map:
        49 53 8
        0 11 42
        42 0 7
        57 7 4

        water-to-light map:
        88 18 7
        18 25 70

        light-to-temperature map:
        45 77 23
        81 45 19
        68 64 13

        temperature-to-humidity map:
        0 69 1
        1 0 69

        humidity-to-location map:
        60 56 37
        56 93 4
    "};

    #[test]
    fn test_part1() {
        assert_eq!(Day5.part1(TEST_INPUT), 35);
    }

    #[test]
    fn test_part2() {
        assert_eq!(Day5.part2(TEST_INPUT), Some(46));
    }
}
