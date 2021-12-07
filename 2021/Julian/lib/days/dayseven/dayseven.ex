defmodule Aoc.DaySeven do
  def solvePartOne do
    parse()
    |> get_crabs_distances()
    |> Enum.zip_with(fn x -> x end)
    |> Enum.map(&Enum.sum(&1))
    |> Enum.min()
  end

  def solvePartTwo do
    parse()
    |> get_crabs_distances()
    |> Enum.map(&Enum.map(&1, fn dis -> calculate_rising_fuel(dis) end))
    |> Enum.zip_with(fn x -> x end)
    |> Enum.map(&Enum.sum(&1))
    |> Enum.min()
  end

  defp parse do
    File.read!("./lib/days/dayseven/input.txt")
    |> String.split(",", trim: true)
    |> Enum.map(&String.to_integer/1)
  end

  defp get_crabs_distances(crabs) do
    max = Enum.max(crabs)
    min = Enum.min(crabs)

    crabs
    |> Enum.map(fn crab ->
      Enum.map(min..max, fn pos -> abs(crab - pos) end)
    end)
  end

  defp calculate_rising_fuel(distance) do
    0..distance
    |> Enum.sum()
  end
end
