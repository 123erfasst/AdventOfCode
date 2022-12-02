mod helper;
mod days;

use helper::solution::{Solution, SolutionPair};
use std::{time::Instant, env};
use days::day01;
fn main() {
    let args: Vec<String> = env::args().collect();
    if args.len() < 2 {
        panic!("Please provide the day(s) to run as a command-line argument.");
    }

    let days: Vec<u8> = args[1..].iter()
        .map(|x| x.parse().unwrap_or_else(|v| panic!("Not a valid day: {}", v)))
        .collect();

    let mut runtime = 0.0;

    for day in days {
        let func = get_day_solver(day);

        let time = Instant::now();
        let (p1, p2) = func();
        let elapsed_ms = time.elapsed().as_nanos() as f64 / 1_000_000.0;
        
        println!("\n=== Day {:02} ===", day);
        println!("  · Part 1: {}", p1.unwrap_or(Solution::Str("Not Implemented".to_string())));
        println!("  · Part 2: {}", p2.unwrap_or(Solution::Str("Not Implemented".to_string())));
        println!("  · Elapsed: {:.4} ms", elapsed_ms);

        runtime += elapsed_ms;
    }

    println!("Total runtime: {:.4} ms", runtime);
}

fn get_day_solver(day: u8) -> fn() -> SolutionPair {
    match day {
         1 => day01::solve,
         _ => unimplemented!(),
    }
}