defmodule Aoc.DayTwelve do
  def solvePartOne do
    parse()
    |> get_all_paths()
  end

  def solvePartTwo do
    parse()
  end

  defp parse do
    File.stream!("./lib/days/daytwelve/testinput.txt")
    |> Stream.map(&String.trim/1)
    |> Stream.map(&String.split(&1, "-"))
    |> Enum.reduce(%{}, fn
      [start, des | _], acc ->
        Map.update(acc, start, [des], fn x -> [des | x] end)
    end)
  end

  defp get_all_paths(map, current_path \\ [], cave \\ "start")

  defp get_all_paths(map, current_path, cave) when is_map_key(map, cave) do
    map
    |> Map.get(cave)
    |> Enum.filter(fn path ->
      String.upcase(path) == path or !Enum.member?(current_path, path)
    end)
    |> Enum.map(&get_all_paths(map, [cave | current_path], &1))
  end

  defp get_all_paths(_, current_path, cave) do
    [cave | current_path]
    |> Enum.reverse()
  end
end
