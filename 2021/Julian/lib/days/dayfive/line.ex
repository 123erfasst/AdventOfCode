defmodule Aoc.DayFive.Line do
  alias Aoc.DayFive.Line
  defstruct start_position: [x: 0, y: 0], end_position: [x: 0, y: 0]

  def parse_line([startP, endP]) do
    %Line{start_position: parse_position(startP), end_position: parse_position(endP)}
  end

  defp parse_position(stringP) do
    [x, y] =
      stringP
      |> String.split(",", trim: true)
      |> Enum.map(&String.to_integer/1)

    [x: x, y: y]
  end

  def horizontal_or_vertical?(%Line{start_position: [x: x1, y: y1], end_position: [x: x2, y: y2]}) do
    x1 == x2 or y1 == y2
  end

  def get_all_positions(%Line{start_position: [x: x1, y: y1], end_position: [x: x2, y: y2]})
      when x1 == x2 or y1 == y2 do
    for x <- x1..x2, y <- y1..y2, do: [x: x, y: y]
  end

  def get_all_positions(%Line{start_position: [x: x1, y: y1], end_position: [x: x2, y: y2]}) do
    lrange = x1..x2//if(x1 < x2, do: 1, else: -1)
    rrange = y1..y2//if(y1 < y2, do: 1, else: -1)
    Enum.zip_with([lrange, rrange], fn [x, y] -> [x: x, y: y] end)
  end
end
