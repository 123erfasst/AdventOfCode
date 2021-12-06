defmodule Aoc.DaySix do
  def solvePartOne do
    parse()
    |> run_population(80)
    |> count_values()
  end

  def solvePartTwo do
    parse()
    |> run_population(256)
    |> count_values()
  end

  defp parse do
    File.read!("./lib/days/daysix/input.txt")
    |> String.split(",", trim: true)
    |> Enum.map(&String.to_integer/1)
  end

  defp run_population(input, n) do
    Enum.reduce(input, %{}, fn x, acc -> cached_computation(acc, x - n) end)
  end

  defp cached_computation(cache, n) when is_map_key(cache, n) do
    Map.update(cache, n, {0, 1}, fn {value, amount} -> {value, amount + 1} end)
  end

  defp cached_computation(cache, n) do
    Map.put(cache, n, {process_fish(n), 1})
  end

  defp process_fish(n) when n >= 0 do
    1
  end

  defp process_fish(n) do
    process_fish(n + 7) + process_fish(n + 9)
  end

  defp count_values(cache) do
    cache
    |> Map.values()
    |> Enum.reduce(0, fn {value, amount}, acc -> acc + value * amount end)
  end
end
