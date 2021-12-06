defmodule Aoc.DayOne do
  def solvePartOne do
    parse()
    |> sum_input(0)
  end

  def solvePartTwo do
    parse()
    |> map_three_windows()
    |> sum_input(0)
  end

  defp parse do
    {:ok, contents} = File.read("./lib/days/dayone/input.txt")
    contents
    |> String.split("\n", trim: true)
    |> Enum.map(&String.to_integer/1)
  end

  defp sum_input([first, second | tail], acc) do
    sum_input([second | tail], (if second > first, do: acc + 1, else: acc))
  end

  defp sum_input(_, acc) do
    acc
  end

  defp map_three_windows([first, second, third | tail]) do
    [first + second + third | map_three_windows([second , third | tail])]
  end

  defp map_three_windows(_) do
    []
  end
end
