defmodule Aoc.DayThirteen do
  def solvePartOne do
    {points, [instr | _]} = parse()

    fold_along(instr, points)
    |> MapSet.size()
  end

  def solvePartTwo do
    {points, instructions} = parse()

    instructions
    |> Enum.reduce(points, &fold_along/2)
    |> print()
  end

  defp parse do
    [points, instruction] =
      File.read!("./lib/days/daythirteen/input.txt")
      |> String.trim()
      |> String.split("\n\n")

    {parse_points(points), parse_instructions(instruction)}
  end

  defp parse_points(points) do
    points
    |> String.split("\n")
    |> Enum.map(fn row ->
      row
      |> String.split(",")
      |> Enum.map(&String.to_integer/1)
      |> List.to_tuple()
    end)
    |> MapSet.new()
  end

  defp parse_instructions(instructions) do
    instructions
    |> String.split("\n")
    |> Enum.map(&String.replace(&1, "fold along ", ""))
    |> Enum.map(fn str ->
      str
      |> String.split("=")
      |> (fn [dir, value] -> {dir, value |> String.to_integer()} end).()
    end)
  end

  defp fold_along({"y", value}, set) do
    changed =
      set
      |> Enum.filter(fn {_, y} -> y > value end)
      |> MapSet.new()

    unchanged = MapSet.difference(set, changed)

    changed
    |> Enum.map(fn {x, y} -> {x, value - (y - value)} end)
    |> MapSet.new()
    |> MapSet.union(unchanged)
  end

  defp fold_along({"x", value}, set) do
    changed =
      set
      |> Enum.filter(fn {x, _} -> x > value end)
      |> MapSet.new()

    unchanged = MapSet.difference(set, changed)

    changed
    |> Enum.map(fn {x, y} -> {value - (x - value), y} end)
    |> MapSet.new()
    |> MapSet.union(unchanged)
  end

  defp print(set) do
    {max_x, _} = Enum.max_by(set, &elem(&1, 0))
    {_, max_y} = Enum.max_by(set, &elem(&1, 1))

    Enum.map_join(0..max_y, "\n", fn y ->
      Enum.map_join(0..max_x, &if({&1, y} in set, do: "|", else: " "))
    end)
    |> IO.puts()
  end
end
