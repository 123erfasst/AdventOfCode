defmodule Aoc.DayTwo do
  def solvePartOne do
    parse()
    |> Enum.reduce([0,0], &applyInstructionPartOne(&1, &2))
    |> Enum.product()
  end

  def solvePartTwo do
    parse()
    |> Enum.reduce([0, 0, 0], &applyInstructionPartTwo(&1, &2))
    |> (fn ([x,y | _]) -> x * y end).()
  end

  defp parse do
    {:ok, contents} = File.read("./lib/days/daytwo/input.txt")
    contents
    |> String.split("\n", trim: true)
    |> Enum.map(&String.split(&1, " ", trim: true) |> parseTuple)
  end

  defp parseTuple ([first, second | _]) do
    {first, String.to_integer(second)}
  end

  defp applyInstructionPartOne({instruction, value}, [x, y]) do
    case instruction do
      "forward" -> [x + value, y]
      "up" -> [x, y - value]
      "down" -> [x, y + value]
      _ -> [x, y]
    end
  end

  defp applyInstructionPartTwo({instruction, value}, [x, y, aim]) do
    case instruction do
      "forward" -> [x + value, y + aim * value, aim]
      "up" -> [x, y, aim - value]
      "down" -> [x, y, aim + value]
      _ -> [x, y, aim]
    end
  end
end
