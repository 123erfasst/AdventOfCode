import gleam/dict
import gleam/int
import gleam/list
import gleam/result
import gleam/set
import gleam/string
import utils/input

pub fn solve_part_one(input: String) -> Result(String, String) {
  parse_input(input)
  |> run_step
  |> fn(x) { x.visited }
  |> set.to_list
  |> list.map(fn(a) { a.0 })
  |> list.unique
  |> list.length
  |> int.to_string
  |> Ok
}

pub fn solve_part_two(input: String) -> Result(String, String) {
  parse_input(input)
  |> get_permutations
  |> list.filter(has_loop)
  |> list.length
  |> int.to_string
  |> Ok
}

fn get_permutations(state: GameState) -> List(GameState) {
  let field_list = state.map |> dict.to_list
  list.filter_map(field_list, fn(a) {
    case a.1 {
      "." ->
        Ok(
          GameState(..state, map: state.map |> dict.upsert(a.0, fn(_a) { "#" })),
        )
      _ -> Error("Original Map")
    }
  })
}

fn has_loop(state: GameState) -> Bool {
  let next_coordinates = get_next_coordinates(state)
  let next_field = dict.get(state.map, next_coordinates)

  let has_visited =
    set.contains(state.visited, #(next_coordinates, state.direction))

  case next_field, has_visited {
    Error(_), _ -> False
    Ok(_), True -> True
    Ok(field), False -> step(state, field, next_coordinates) |> has_loop
  }
}

type Direction {
  Up
  Down
  Right
  Left
}

type GameState {
  GameState(
    map: dict.Dict(#(Int, Int), String),
    position: #(Int, Int),
    direction: Direction,
    visited: set.Set(#(#(Int, Int), Direction)),
  )
}

fn run_step(state: GameState) -> GameState {
  let next_coordinates = get_next_coordinates(state)
  let next_field = dict.get(state.map, next_coordinates)

  case next_field {
    Error(_) -> state
    Ok(field) -> step(state, field, next_coordinates) |> run_step
  }
}

fn step(
  state: GameState,
  next_field: String,
  next_coordinates: #(Int, Int),
) -> GameState {
  case next_field {
    "#" -> {
      case state.direction {
        Up -> GameState(..state, direction: Right)
        Right -> GameState(..state, direction: Down)
        Down -> GameState(..state, direction: Left)
        Left -> GameState(..state, direction: Up)
      }
    }
    _ -> {
      GameState(
        ..state,
        position: next_coordinates,
        visited: set.insert(state.visited, #(next_coordinates, state.direction)),
      )
    }
  }
}

fn get_next_coordinates(state: GameState) -> #(Int, Int) {
  case state.direction {
    Up -> #(state.position.0, state.position.1 - 1)
    Down -> #(state.position.0, state.position.1 + 1)
    Right -> #(state.position.0 + 1, state.position.1)
    Left -> #(state.position.0 - 1, state.position.1)
  }
}

fn parse_input(input: String) {
  let map =
    input.lines(input)
    |> list.map(string.split(_, ""))
    |> list.index_map(fn(a, ai) {
      list.index_map(a, fn(b, bi) { #(#(bi, ai), b) })
    })
    |> list.flatten

  let start_position =
    list.find_map(map, fn(a) {
      case a.1 {
        "^" -> Ok(a.0)
        _ -> Error("NotFound")
      }
    })
    |> result.unwrap(#(0, 0))

  GameState(
    map: map |> dict.from_list,
    position: start_position,
    direction: Up,
    visited: set.new(),
  )
}
