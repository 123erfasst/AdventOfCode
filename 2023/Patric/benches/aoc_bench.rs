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

criterion_main!(day01, day02, day03, day04, day05, day06, day07, day08);
