defmodule Aoc.DayEleven do
  def solvePartOne do
    parse()
    |> simulate_steps(100)
    |> (fn {_, flashes} -> flashes end).()
  end

  def solvePartTwo do
    parse()
    |> run_steps_until_sync()
  end

  defp parse do
    File.stream!("./lib/days/dayeleven/testinput.txt")
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

  defp simulate_steps(map, count, flashes \\ 0)

  defp simulate_steps(map, count, flashes) when count > 0 do
    {c_map, c_flashes} =
      Map.keys(map)
      |> Enum.reduce(map, fn coords, acc -> simulate_step(acc, coords) end)
      |> clean_and_count(flashes)

    simulate_steps(c_map, count - 1, c_flashes)
  end

  defp simulate_steps(map, _, flashes) do
    {map, flashes}
  end

  defp octopus_are_sync(map) do
    map
    |> Enum.filter(fn {_, value} -> value > 9 end)
    |> (&(Enum.count(&1) == Enum.count(map))).()
  end

  defp run_steps_until_sync(map, curr_step \\ 1) do
    Map.keys(map)
    |> Enum.reduce(map, fn coords, acc -> simulate_step(acc, coords) end)
    |> (fn
          map ->
            if octopus_are_sync(map) do
              curr_step
            else
              map
              |> clean_and_count(0)
              |> (fn {map, _} -> run_steps_until_sync(map, curr_step + 1) end).()
            end
        end).()
  end

  defp clean_and_count(map, flashes) do
    Enum.reduce(map, {map, flashes}, fn
      {coords, value}, {acc_map, flashes} when value > 9 ->
        {Map.put(acc_map, coords, 0), flashes + 1}

      _, acc ->
        acc
    end)
  end

  defp get_adjacent_cells({x, y}) do
    [
      {x, y + 1},
      {x, y - 1},
      {x - 1, y},
      {x + 1, y},
      {x - 1, y + 1},
      {x - 1, y - 1},
      {x + 1, y - 1},
      {x + 1, y + 1}
    ]
  end

  defp simulate_step(map, coords) when is_map_key(map, coords) do
    new_value = Map.get(map, coords) + 1

    updated_map =
      map
      |> Map.put(coords, new_value)

    if new_value == 10 do
      get_adjacent_cells(coords)
      |> Enum.reduce(updated_map, fn adj_coord, acc -> simulate_step(acc, adj_coord) end)
    else
      updated_map
    end
  end

  defp simulate_step(map, _) do
    map
  end
end
