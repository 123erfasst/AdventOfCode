defmodule Aoc.DayFive do
  alias Aoc.DayFive.Line

  def solvePartOne do
    parse()
    |> Stream.filter(&Line.horizontal_or_vertical?(&1))
    |> get_intersections()
  end

  def solvePartTwo do
    parse()
    |> get_intersections()
  end

  defp parse do
    File.stream!("./lib/days/dayfive/input.txt")
    |> Stream.map(&String.trim/1)
    |> Stream.map(fn line ->
      line
      |> String.split(" -> ")
      |> Line.parse_line()
    end)
  end

  defp get_intersections(stream) do
    stream
    |> Stream.map(&Line.get_all_positions/1)
    |> Stream.flat_map(& &1)
    |> Enum.frequencies()
    |> Enum.filter(fn {_, count} -> count >= 2 end)
    |> Enum.count()
  end
end
