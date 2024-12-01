import gleam/string
import simplifile.{type FileError, read}

pub fn read_day_input(
  day: String,
  use_example_data: Bool,
) -> Result(String, FileError) {
  let path_helper = case use_example_data {
    True -> day <> "-example"
    False -> day
  }

  let path = "./src/inputs/day-" <> path_helper <> ".txt"
  read(path)
}

pub fn lines(input: String) -> List(String) {
  string.split(input, "\r\n")
}
