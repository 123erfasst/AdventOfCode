use crate::Solution;
use ahash::AHashMap;
use nom::branch::alt;
use nom::character::complete::{alphanumeric1, char as n_char, line_ending, space1};
use nom::combinator::map;
use nom::multi::{fold_many1, many1};
use nom::sequence::{delimited, separated_pair, terminated, tuple};
use nom::IResult;
use rayon::prelude::*;

pub struct Day8;

impl Solution<8> for Day8 {
    type Output = usize;

    fn part1(&self, input: &str) -> Self::Output {
        let (steps, graph) = parse_input(input).expect("Format mismatch").1;
        iterate_graph(&graph, &steps, "AAA")
            .take_while(|&current| current != "ZZZ")
            .count()
            + 1
    }

    fn part2(&self, input: &str) -> Option<Self::Output> {
        let (steps, graph) = parse_input(input).expect("Format mismatch").1;
        let starts = graph
            .keys()
            .filter(|node| node.ends_with('A'))
            .collect::<Vec<_>>();
        let step_counts = starts.par_iter().map(|node| {
            iterate_graph(&graph, &steps, node)
                .take_while(|&current| !current.ends_with('Z'))
                .count()
                + 1
        });

        Some(step_counts.reduce(|| 1, lcm))
    }
}

fn iterate_graph<'a>(
    graph: &'a Graph,
    steps: &'a [Step],
    start: &'a str,
) -> impl Iterator<Item = &'a str> + 'a {
    steps.iter().cycle().scan(start, move |current, step| {
        let &node = graph.get(current)?;
        *current = match step {
            Step::Left => node.0,
            Step::Right => node.1,
        };
        Some(*current)
    })
}

fn lcm(a: usize, b: usize) -> usize {
    (a * b) / gcd(a, b)
}

fn gcd(mut a: usize, mut b: usize) -> usize {
    while b != 0 {
        (a, b) = (b, a % b);
    }
    a
}

#[derive(Debug)]
enum Step {
    Left,
    Right,
}

type Graph<'a> = AHashMap<&'a str, (&'a str, &'a str)>;

fn parse_input(input: &str) -> IResult<&str, (Vec<Step>, Graph)> {
    separated_pair(parse_steps, many1(line_ending), parse_graph)(input)
}

fn parse_steps(path: &str) -> IResult<&str, Vec<Step>> {
    many1(map(alt((n_char('L'), n_char('R'))), |c| match c {
        'L' => Step::Left,
        'R' => Step::Right,
        _ => unreachable!(),
    }))(path)
}

fn parse_graph(input: &str) -> IResult<&str, Graph> {
    fold_many1(
        terminated(
            separated_pair(
                alphanumeric1,
                tuple((space1, n_char('='), space1)),
                delimited(
                    n_char('('),
                    separated_pair(alphanumeric1, tuple((n_char(','), space1)), alphanumeric1),
                    n_char(')'),
                ),
            ),
            line_ending,
        ),
        AHashMap::new,
        |mut map, (key, value)| {
            map.insert(key, value);
            map
        },
    )(input)
}

#[cfg(test)]
mod test {
    use super::*;
    use indoc::indoc;

    const FIRST_TEST_INPUT: &str = indoc! {"
        RL

        AAA = (BBB, CCC)
        BBB = (DDD, EEE)
        CCC = (ZZZ, GGG)
        DDD = (DDD, DDD)
        EEE = (EEE, EEE)
        GGG = (GGG, GGG)
        ZZZ = (ZZZ, ZZZ)
    "};

    const SECOND_TEST_INPUT: &str = indoc! {"
        LLR

        AAA = (BBB, BBB)
        BBB = (AAA, ZZZ)
        ZZZ = (ZZZ, ZZZ)
    "};

    #[test]
    fn test_part1() {
        assert_eq!(Day8.part1(FIRST_TEST_INPUT), 2);
        assert_eq!(Day8.part1(SECOND_TEST_INPUT), 6);
    }

    const THIRD_TEST_INPUT: &str = indoc! {"
        LR

        11A = (11B, XXX)
        11B = (XXX, 11Z)
        11Z = (11B, XXX)
        22A = (22B, XXX)
        22B = (22C, 22C)
        22C = (22Z, 22Z)
        22Z = (22B, 22B)
        XXX = (XXX, XXX)
    "};

    #[test]
    fn test_part2() {
        assert_eq!(Day8.part2(THIRD_TEST_INPUT), Some(6));
    }
}
