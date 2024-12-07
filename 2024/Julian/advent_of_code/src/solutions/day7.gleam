import gleam/int
import gleam/list
import gleam/result
import gleam/string
import utils/input

pub fn solve_part_one(input: String) -> Result(String, String) {
  parse_input(input)
  |> list.filter(has_possible_result(get_possible_values_one, _))
  |> list.map(fn(a) { a.result })
  |> list.fold(0, int.add)
  |> int.to_string
  |> Ok
}

fn has_possible_result(
  solve_fn: fn(List(Int), Int) -> List(Int),
  equ: Equation,
) -> Bool {
  let values = case equ.args {
    [first, ..rest] -> {
      rest
      |> list.fold([first], fn(acc, a) { solve_fn(acc, a) })
    }
    _ -> []
  }

  list.contains(values, equ.result)
}

fn get_possible_values_one(value: List(Int), next_value: Int) -> List(Int) {
  let additions =
    value
    |> list.map(int.add(_, next_value))
  let multiplications =
    value |> list.map(fn(x) { int.product([x, next_value]) })

  list.flatten([additions, multiplications])
}

pub fn solve_part_two(input: String) -> Result(String, String) {
  parse_input(input)
  |> list.filter(has_possible_result(get_possible_values_two, _))
  |> list.map(fn(a) { a.result })
  |> list.fold(0, int.add)
  |> int.to_string
  |> Ok
}

fn combine(a: Int, b: Int) {
  { int.to_string(a) <> int.to_string(b) } |> int.parse |> result.unwrap(0)
}

fn get_possible_values_two(value: List(Int), next_value: Int) -> List(Int) {
  let additions =
    value
    |> list.map(int.add(_, next_value))
  let multiplications =
    value |> list.map(fn(x) { int.product([x, next_value]) })

  let combinations = value |> list.map(combine(_, next_value))

  list.flatten([additions, multiplications, combinations])
}

type Equation {
  Equation(result: Int, args: List(Int))
}

fn parse_input(input: String) -> List(Equation) {
  input.lines(input)
  |> list.filter_map(fn(a) {
    case string.split(a, ":") {
      [value, args] -> {
        let parsed_value = int.parse(value) |> result.unwrap(0)
        let parsed_args =
          string.trim(args) |> string.split(" ") |> list.filter_map(int.parse)

        Ok(Equation(parsed_value, parsed_args))
      }
      _ -> Error("ParseError")
    }
  })
}
