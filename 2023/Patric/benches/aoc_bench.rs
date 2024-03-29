use aoc_2023::*;
use criterion::{black_box, criterion_group, criterion_main, Criterion};
use paste::paste;
macro_rules! bench_day {
    ($name:ident, $day:expr) => {
        paste! {
            fn [<bench_$name>](c: &mut Criterion) {
                let solution = $day;
                let input = include_str!(concat!("../inputs/", stringify!($name), ".txt"));
                let unified_input = &input.replace("\r\n", "\n");
                let mut group = c.benchmark_group(stringify!($name));
                group.noise_threshold(0.05);
                group.bench_function("part1", |b| {
                    b.iter(|| solution.part1(black_box(unified_input)))
                });
                group.bench_function("part2", |b| {
                    b.iter(|| solution.part2(black_box(unified_input)))
                });
                group.finish()
            }
            criterion_group!($name, [<bench_$name>]);
        }
    };
}

bench_day!(day01, day1::Day1);
bench_day!(day02, day2::Day2);
bench_day!(day03, day3::Day3);
bench_day!(day04, day4::Day4);
bench_day!(day05, day5::Day5);
bench_day!(day06, day6::Day6);
bench_day!(day07, day7::Day7);
bench_day!(day08, day8::Day8);
bench_day!(day09, day9::Day9);
bench_day!(day10, day10::Day10);
bench_day!(day11, day11::Day11);
bench_day!(day12, day12::Day12);
bench_day!(day13, day13::Day13);
bench_day!(day14, day14::Day14);
bench_day!(day15, day15::Day15);
bench_day!(day16, day16::Day16);
bench_day!(day17, day17::Day17);
bench_day!(day18, day18::Day18);

criterion_main!(
    day01, day02, day03, day04, day05, day06, day07, day08, day09, day10, day11, day12, day13,
    day14, day15, day16, day17, day18
);
