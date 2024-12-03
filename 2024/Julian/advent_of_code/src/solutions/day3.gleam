import gleam/int
import gleam/string
import gleam/option
import gleam/list
import gleam/regexp

pub fn solve_part_one(input: String) -> Result(String, String) {
  let assert Ok(reg) = regexp.from_string("mul\\(([\\d]+,[\\d]+)\\)")
  
  regexp.scan(reg, input)
  |> list.map(get_matched_value)
  |> list.fold(0, int.add)
  |> int.to_string
  |> Ok
}

fn get_matched_value(match: regexp.Match) -> Int {
    list.fold(match.submatches, 0, fn(acc, b) {
                case b {
                    option.Some(v) -> {
                        string.split(v, ",")
                        |> list.filter_map(int.parse)
                        |> int.product
                        |> int.add(acc)
                    }
                    _ -> acc
                }
            })
}

type State {
    State(value: Int, enabled: Bool)
}

fn step(state: State, match: regexp.Match) {
    case match.content, state.enabled {
        "mul(" <> _, True -> {
            let value = get_matched_value(match)
            State(..state, value: state.value + value)
        }
        "don't()", _ -> State(..state, enabled: False)
        "do()", _ -> State(..state, enabled: True)
        _, _ -> state
    }
}

pub fn solve_part_two(input: String) -> Result(String, String) {
    let assert Ok(reg) = regexp.from_string("mul\\(([\\d]+,[\\d]+)\\)|do\\(\\)|don't\\(\\)")
    regexp.scan(reg, input)
    |> list.fold(State(0, True), step)
    |> fn(x) {x.value}
    |> int.to_string
    |> Ok
}
