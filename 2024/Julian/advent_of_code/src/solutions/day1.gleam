import gleam/dict
import gleam/int
import gleam/list
import gleam/result
import gleam/string
import utils/input

pub fn solve_part_one(input: String) -> Result(String, String) {
  parse_input(input)
  |> list.map(list.sort(_, int.compare))
  |> list.transpose
  |> list.map(fn(x) {
    list.reduce(x, fn(a, b) { a - b }) |> result.unwrap(0) |> int.absolute_value
  })
  |> list.fold(0, fn(a, b) { a + b })
  |> int.to_string
  |> Ok
}

pub fn solve_part_two(input: String) -> Result(String, String) {
  let x = parse_input(input)

  let #(occurences, value_list) = case x {
    [occ, values] -> #(occ, values)
    _ -> #([], [])
  }

  let occurence_map =
    list.group(occurences, fn(x) { x })
    |> dict.map_values(fn(_key, a) { list.length(a) })

  value_list
  |> list.map(fn(a) { a * { dict.get(occurence_map, a) |> result.unwrap(0) } })
  |> list.fold(0, fn(a, b) { a + b })
  |> int.to_string
  |> Ok
}

fn parse_input(input: String) -> List(List(Int)) {
  input.lines(input)
  |> list.filter_map(parse_row)
  |> list.transpose
}

fn parse_row(row: String) -> Result(List(Int), String) {
  let splitted_row = string.split(row, "   ")

  case splitted_row {
    [first, second] -> {
      let parsed_first = int.parse(first) |> result.unwrap(0)
      let parsed_second = int.parse(second) |> result.unwrap(0)
      Ok([parsed_second, parsed_first])
    }
    _ -> Error("Parse Error")
  }
}
