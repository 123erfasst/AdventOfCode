use crate::Solution;
use itertools::{Itertools, Position};
use nom::character::complete::{alphanumeric1, line_ending, space1, u32 as n_u32};
use nom::multi::separated_list1;
use nom::sequence::separated_pair;
use nom::IResult;
use rayon::prelude::*;
use std::collections::HashMap;

pub struct Day7;

impl Solution<7> for Day7 {
    type Output = u32;

    fn part1(&self, input: &str) -> Self::Output {
        let mut hands = parse_input(input, false).expect("Format mismatch").1;
        hands.sort_unstable_by_key(|(hand, _)| (hand.h_type, hand.score));
        calculate_winnings(&hands)
    }

    fn part2(&self, input: &str) -> Option<Self::Output> {
        let mut hands = parse_input(input, true).expect("Format mismatch").1;
        hands.sort_unstable_by_key(|(hand, _)| (hand.h_type, hand.score));
        Some(calculate_winnings(&hands))
    }
}

#[allow(clippy::cast_possible_truncation)]
fn calculate_winnings(hands: &[(Hand, Bid)]) -> u32 {
    hands
        .iter()
        .enumerate()
        .map(|(idx, (_, bid))| (idx + 1) as u32 * bid.0)
        .sum()
}

fn parse_input(input: &str, joker_rule: bool) -> IResult<&str, Vec<(Hand, Bid)>> {
    let cards = separated_list1(line_ending, parse_card)(input)?
        .1
        .into_par_iter()
        .map(|(hand, bid)| (Hand::new(hand, joker_rule), Bid(bid)))
        .collect::<Vec<_>>();
    Ok(("", cards))
}

fn parse_card(card: &str) -> IResult<&str, (&str, u32)> {
    separated_pair(alphanumeric1, space1, n_u32)(card)
}

struct Hand {
    h_type: HandType,
    score: u32,
}

impl Hand {
    fn new(hand: &str, joker_rule: bool) -> Self {
        Hand {
            h_type: HandType::get(hand, joker_rule),
            score: Hand::get_score(hand, joker_rule),
        }
    }

    fn get_score(hand: &str, joker_rule: bool) -> u32 {
        hand.chars()
            .filter_map(|card| card_strength(card, joker_rule))
            .fold(0, |score, strength| (score << 4) + strength)
    }
}

const fn card_strength(card: char, joker_rule: bool) -> Option<u32> {
    match card {
        'A' => Some(14),
        'K' => Some(13),
        'Q' => Some(12),
        'J' => {
            if joker_rule {
                Some(0)
            } else {
                Some(11)
            }
        }
        'T' => Some(10),
        n if n.is_ascii_digit() => n.to_digit(10),
        _ => None,
    }
}

#[derive(Ord, PartialOrd, Eq, PartialEq, Copy, Clone, Debug)]
enum HandType {
    FiveOfAKind = 6,
    FourOfAKind = 5,
    FullHouse = 4,
    ThreeOfAKind = 3,
    TwoPair = 2,
    OnePair = 1,
    HighCard = 0,
}

impl HandType {
    fn get(hand: &str, joker_rule: bool) -> Self {
        let counts = hand.chars().counts();
        let values = get_sorted_counts(counts, joker_rule);
        match values.as_slice() {
            [5] => HandType::FiveOfAKind,
            [1, 4] => HandType::FourOfAKind,
            [2, 3] => HandType::FullHouse,
            [1, 1, 3] => HandType::ThreeOfAKind,
            [1, 2, 2] => HandType::TwoPair,
            [1, 1, 1, 2] => HandType::OnePair,
            [1, 1, 1, 1, 1] => HandType::HighCard,
            x => panic!("Got unknown hand {x:?}"),
        }
    }
}

fn get_sorted_counts(card_counts: HashMap<char, usize>, joker_rule: bool) -> Vec<usize> {
    match (joker_rule, card_counts.get(&'J')) {
        (true, Some(5)) => vec![5],
        (true, Some(&joker_count)) => card_counts
            .iter()
            .filter_map(|(card, counts)| (card != &'J').then_some(counts))
            .sorted()
            .with_position()
            .map(|(position, count)| match position {
                Position::Only | Position::Last => count + joker_count,
                _ => *count,
            })
            .collect(),
        _ => card_counts.into_values().sorted().collect::<Vec<_>>(),
    }
}

#[repr(transparent)]
struct Bid(u32);

#[cfg(test)]
mod test {
    use super::*;
    use indoc::indoc;

    const TEST_INPUT: &str = indoc! {"
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
    "};

    #[test]
    fn test_part1() {
        assert_eq!(Day7.part1(TEST_INPUT), 6440);
    }

    #[test]
    fn type_with_joker() {
        assert_eq!(HandType::get("JJJJJ", true), HandType::FiveOfAKind);
        assert_eq!(HandType::get("KTJJT", true), HandType::FourOfAKind);
    }

    #[test]
    fn test_part2() {
        assert_eq!(Day7.part2(TEST_INPUT), Some(5905));
    }
}
