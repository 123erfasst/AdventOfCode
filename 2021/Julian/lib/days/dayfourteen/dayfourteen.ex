defmodule Aoc.DayFourteen do
  def solvePartOne do
    {input, transformations} = parse()

    input
    |> do_steps(transformations, 10)
    |> count_letters()
  end

  def solvePartTwo do
    {input, transformations} = parse()

    input
    |> do_steps(transformations, 40)
    |> count_letters()
  end

  defp parse do
    [input, transformations] =
      File.read!("./lib/days/dayfourteen/input.txt")
      |> String.trim()
      |> String.split("\n\n")

    {parse_input(input), parse_transformations(transformations)}
  end

  defp parse_transformations(transformations) do
    transformations
    |> String.split("\n")
    |> Enum.map(fn row ->
      row
      |> String.split(" -> ")
      |> Enum.map(&String.to_charlist/1)
      |> List.to_tuple()
    end)
    |> Map.new()
  end

  defp parse_input(input) do
    input
    |> String.to_charlist()
    |> Enum.chunk_every(2, 1)
    |> Enum.frequencies()
  end

  defp do_steps(input, _, 0) do
    input
  end

  defp do_steps(input, transformations, count) do
    input
    |> do_step(transformations)
    |> do_steps(transformations, count - 1)
  end

  defp do_step(input, transformations) do
    input
    |> Enum.flat_map(fn
      {pair = [first, second], amount} when is_map_key(transformations, pair) ->
        Map.get(transformations, pair)
        |> (fn result ->
              [{[first, result], amount}, {[result, second], amount}]
            end).()

      {pair, amount} ->
        [{pair, amount}]
    end)
    |> Enum.reduce(%{}, fn {pair, amount}, acc ->
      Map.update(acc, pair |> List.to_charlist(), amount, fn x -> x + amount end)
    end)
  end

  defp count_letters(result) do
    result
    |> Enum.map(fn {[head | _], amount} -> {head, amount} end)
    |> Enum.reduce(%{}, fn {letter, amount}, map ->
      Map.update(map, letter, amount, fn x -> x + amount end)
    end)
    |> Map.values()
    |> Enum.min_max()
    |> (fn {min, max} -> max - min end).()
  end
end
