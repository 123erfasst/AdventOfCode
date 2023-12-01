use aoc_2023::*;
use criterion::{black_box, criterion_group, criterion_main, Criterion};
use paste::paste;
macro_rules! bench_day {
    ($name:ident, $day:expr) => {
        paste! {
            fn [<bench $name>](c: &mut Criterion) {
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
            criterion_group!($name, [<bench $name>]);
        }
    };
}

bench_day!(day01, day1::Day1);

criterion_main!(day01,);
