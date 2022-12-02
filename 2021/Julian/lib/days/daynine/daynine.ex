defmodule Aoc.DayNine do
  def solvePartOne do
    parse()
    |> get_low_points()
    |> Enum.map(fn {_, value} -> 1 + value end)
    |> Enum.sum()
  end

  def solvePartTwo do
    map = parse()

    res =
      map
      |> get_low_points()
      |> Enum.map(&find_basin(map, &1))
      |> Enum.map(&Enum.count(&1))
      |> Enum.sort(:desc)
      |> Enum.take(3)

    res
    |> Enum.map(&IO.inspect/1)

    res
  end

  defp parse do
    File.stream!("./lib/days/daynine/input.txt")
    |> Stream.map(&String.trim/1)
    |> Stream.with_index()
    |> Stream.flat_map(fn {row, y} ->
      row
      |> String.split("", trim: true)
      |> Enum.map(&String.to_integer/1)
      |> Enum.with_index()
      |> Enum.map(fn {cell, x} -> {{x, y}, cell} end)
    end)
    |> Map.new()
  end

  defp get_adjacent_cells({x, y}) do
    [{x - 1, y}, {x + 1, y}, {x, y - 1}, {x, y + 1}]
  end

  defp is_low_point(map, {coords, value}) do
    coords
    |> get_adjacent_cells()
    |> Enum.map(fn adj -> Map.get(map, adj, 10) end)
    |> (&(Enum.min(&1) > value)).()
  end

  defp get_low_points(map) do
    map
    |> Enum.filter(&is_low_point(map, &1))
  end

  defp find_basin(map, low_point = {coords, _}) do
    filtered_map = Map.delete(map, coords)

    adj_low_points =
      coords
      |> get_adjacent_cells()
      |> Enum.map(fn adj -> {adj, Map.get(map, adj, 10)} end)
      |> Enum.filter(&is_low_point(filtered_map, &1))

    filter_adjacent_map =
      adj_low_points
      |> Enum.reduce(filtered_map, fn {pos, _}, acc -> Map.delete(acc, pos) end)

    [
      low_point
      | Enum.flat_map(adj_low_points, fn point ->
          find_basin(filter_adjacent_map, point)
        end)
        |> Enum.uniq()
        |> Enum.filter(fn {_, value} -> value != 9 end)
    ]
  end
end
