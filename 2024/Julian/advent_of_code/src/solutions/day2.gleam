import gleam/int
import gleam/list
import gleam/set
import gleam/string
import utils/input

pub fn solve_part_one(input: String) -> Result(String, String) {
  parse_input(input)
  |> list.filter(is_valid_row)
  |> list.length
  |> int.to_string
  |> Ok
}

pub fn solve_part_two(input: String) -> Result(String, String) {
  parse_input(input)
  |> list.map(get_rows_without_one)
  |> list.filter(fn(row) { list.any(row, is_valid_row) })
  |> list.length
  |> int.to_string
  |> Ok
}

fn parse_input(input: String) -> List(List(Int)) {
  input.lines(input)
  |> list.map(fn(line) {
    string.split(line, " ")
    |> list.filter_map(int.parse)
  })
}

fn get_rows_without_one(row: List(Int)) -> List(List(Int)) {
  row
  |> list.index_map(fn(_a, index) {
    list.flatten([list.take(row, index), list.drop(row, index + 1)])
  })
}

fn is_valid_row(row: List(Int)) -> Bool {
  let windowed_row = row |> list.window_by_2

  let same_direction =
    windowed_row
    |> list.map(fn(a) { int.clamp(a.0 - a.1, -1, 1) })
    |> set.from_list
    |> fn(x) { set.size(x) == 1 }

  same_direction
  && list.all(windowed_row, fn(a) {
    let #(first, second) = a
    let step = int.absolute_value(first - second)
    step >= 1 && step <= 3
  })
}
