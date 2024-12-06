import gleam/dict
import gleam/int
import gleam/list
import gleam/option.{None, Some}
import gleam/order
import gleam/result
import gleam/set.{type Set}
import gleam/string

pub fn solve_part_one(input: String) -> Result(String, String) {
  let #(rules, rows) = parse_input(input)
  rows
  |> list.filter(is_valid_row(rules, _))
  |> list.map(get_center_number)
  |> list.fold(0, int.add)
  |> int.to_string
  |> Ok
}

pub fn solve_part_two(input: String) -> Result(String, String) {
  let #(rules, rows) = parse_input(input)
  rows
  |> list.filter(fn(row) { !is_valid_row(rules, row) })
  |> list.map(sort_invalid_rows(rules, _))
  |> list.map(get_center_number)
  |> list.fold(0, int.add)
  |> int.to_string
  |> Ok
}

fn sort_invalid_rows(rules: dict.Dict(Int, Rules), row: List(Int)) -> List(Int) {
  row
  |> list.sort(fn(x, y) {
    let rules_for_sort =
      dict.get(rules, x) |> result.unwrap(Rules(set.new(), set.new()))

    case set.contains(rules_for_sort.before, y) {
      True -> order.Lt
      _ ->
        case set.contains(rules_for_sort.after, y) {
          True -> order.Gt
          _ -> order.Eq
        }
    }
  })
}

fn is_valid_row(rules: dict.Dict(Int, Rules), row: List(Int)) -> Bool {
  row
  |> list.index_map(fn(val, index) { #(val, index) })
  |> list.all(fn(x) {
    let #(val, index) = x
    let before = list.take(row, index) |> set.from_list
    let after = list.drop(row, index + 1) |> set.from_list

    let val_rules =
      dict.get(rules, val) |> result.unwrap(Rules(set.new(), set.new()))

    set.is_disjoint(val_rules.after, before)
    && set.is_disjoint(val_rules.before, after)
  })
}

fn get_center_number(row: List(Int)) -> Int {
  let l = list.length(row) / 2
  list.index_map(row, fn(x, i) { #(x, i) })
  |> list.find(fn(a) { a.1 == l })
  |> result.unwrap(#(0, 0))
  |> fn(a) { a.0 }
}

fn parse_input(input: String) {
  let #(rules, input) = case string.split(input, "\r\n\r\n") {
    [rules, input] -> {
      case string.split(rules, "\r\n"), string.split(input, "\r\n") {
        rules, input -> #(rules, input)
      }
    }
    _ -> #([], [])
  }

  #(parse_rules(rules), parse_input_row(input))
}

type Rules {
  Rules(before: Set(Int), after: Set(Int))
}

fn parse_rules(rules: List(String)) -> dict.Dict(Int, Rules) {
  rules
  |> list.fold(dict.new(), fn(acc, rule_row) {
    let #(key, value) = case string.split(rule_row, "|") {
      [key, value] -> #(
        key |> int.parse |> result.unwrap(0),
        value |> int.parse |> result.unwrap(0),
      )
      _ -> #(0, 0)
    }
    dict.upsert(acc, key, fn(x) {
      case x {
        Some(v) -> Rules(..v, after: set.insert(v.after, value))
        None -> Rules(set.new(), set.new() |> set.insert(value))
      }
    })
    |> dict.upsert(value, fn(x) {
      case x {
        Some(v) -> Rules(..v, before: set.insert(v.before, key))
        None -> Rules(set.new() |> set.insert(key), set.new())
      }
    })
  })
}

fn parse_input_row(row: List(String)) -> List(List(Int)) {
  row
  |> list.map(fn(x) {
    case string.split(x, ",") {
      [_, ..] as inputs ->
        inputs |> list.map(fn(b) { int.parse(b) |> result.unwrap(0) })
      _ -> []
    }
  })
}
