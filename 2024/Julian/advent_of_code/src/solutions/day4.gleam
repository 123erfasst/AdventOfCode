import gleam/dict
import gleam/int
import gleam/list
import gleam/result
import gleam/string
import utils/input

pub fn solve_part_one(input: String) -> Result(String, String) {
  let map = parse_input(input)

  dict.keys(map)
  |> list.map(count_xmas(map, _))
  |> list.fold(0, int.add)
  |> int.to_string
  |> Ok
}

pub fn solve_part_two(input: String) -> Result(String, String) {
  let map = parse_input(input)
  dict.keys(map)
  |> list.filter(is_mas(map, _))
  |> list.length
  |> int.to_string
  |> Ok
}

fn is_mas(map: dict.Dict(#(Int, Int), String), coord: #(Int, Int)) -> Bool {
  let letter = dict.get(map, coord)

  case letter {
    Ok("A") -> {
      let first_matched = {
        matches_word(map, #(coord.0 - 2, coord.1 - 2), #(1, 1), ["S", "A", "M"])
        || matches_word(map, #(coord.0 - 2, coord.1 - 2), #(1, 1), [
          "M", "A", "S",
        ])
      }
      let second_matched = {
        matches_word(map, #(coord.0 - 2, coord.1 + 2), #(1, -1), ["S", "A", "M"])
        || matches_word(map, #(coord.0 - 2, coord.1 + 2), #(1, -1), [
          "M", "A", "S",
        ])
      }

      first_matched && second_matched
    }
    _ -> False
  }
}

fn count_xmas(map: dict.Dict(#(Int, Int), String), coord: #(Int, Int)) -> Int {
  let letter = dict.get(map, coord)

  case letter {
    Ok("X") -> {
      [
        #(1, 1),
        #(0, 1),
        #(1, 0),
        #(-1, -1),
        #(-1, 0),
        #(-1, 1),
        #(0, -1),
        #(1, -1),
      ]
      |> list.filter(matches_word(map, coord, _, ["M", "A", "S"]))
      |> list.length
    }
    _ -> 0
  }
}

fn matches_word(
  map: dict.Dict(#(Int, Int), String),
  coord: #(Int, Int),
  dir: #(Int, Int),
  remaining_letters: List(String),
) -> Bool {
  let letter = list.first(remaining_letters) |> result.unwrap("")
  let other = list.rest(remaining_letters) |> result.unwrap([])
  let new_coords = get_new_coords(coord, dir)
  let value = dict.get(map, new_coords)

  case value, other {
    Ok(v), [] if v == letter -> True
    Ok(v), [_, ..] if v == letter -> matches_word(map, new_coords, dir, other)
    _, _ -> False
  }
}

fn get_new_coords(coord: #(Int, Int), dir: #(Int, Int)) -> #(Int, Int) {
  #(coord.0 + dir.0, coord.1 + dir.1)
}

fn parse_input(input: String) {
  input.lines(input)
  |> list.map(string.split(_, ""))
  |> list.index_map(fn(a, ai) {
    list.index_map(a, fn(b, bi) { #(#(bi, ai), b) })
  })
  |> list.flatten
  |> dict.from_list
}
