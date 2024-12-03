import solutions/day3
import argv
import gleam/io
import gleam/result
import solutions/day1
import solutions/day2
import utils/input.{read_day_input}
import utils/solution.{Solution}

pub fn main() {
  let result = case argv.load().arguments {
    [day, "test"] -> {
      solve_day(day, True)
    }
    [day] -> {
      solve_day(day, False)
    }
    _ -> Error("No day supplied")
  }

  case result {
    Ok(Solution(part_one, part_two)) ->
      io.println("Part 1: " <> part_one <> " Part 2: " <> part_two)
    Error(err) -> io.println(err)
  }
}

pub fn solve_day(
  day: String,
  use_example_data: Bool,
) -> Result(solution.Solution, String) {
  let input =
    read_day_input(day, use_example_data)
    |> result.map_error(fn(_x) { "FileError" })

  case day, input {
    "1", Ok(unwrapped_input) -> {
      use part_one <- result.try(day1.solve_part_one(unwrapped_input))
      use part_two <- result.try(day1.solve_part_two(unwrapped_input))
      Ok(Solution(part_one, part_two))
    }
    "2", Ok(unwrapped_input) -> {
      use part_one <- result.try(day2.solve_part_one(unwrapped_input))
      use part_two <- result.try(day2.solve_part_two(unwrapped_input))
      Ok(Solution(part_one, part_two))
    }
    "3", Ok(unwrapped_input) -> {
      use part_one <- result.try(day3.solve_part_one(unwrapped_input))
      use part_two <- result.try(day3.solve_part_two(unwrapped_input))
      Ok(Solution(part_one, part_two))
    }
    _, Error(err) -> Error(err)
    _, _ -> Error("No solution supplied")
  }
}
