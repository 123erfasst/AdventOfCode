defmodule Aoc.DayTen do
  def solvePartOne do
    parse()
    |> Stream.map(&process_line(&1, []))
    |> Stream.map(fn {value, _} -> value end)
    |> Enum.sum()
  end

  def solvePartTwo do
    parse()
    |> Stream.map(&process_line(&1, []))
    |> Stream.filter(fn {value, _} -> value == 0 end)
    |> Stream.map(fn {_, stack} -> stack end)
    |> Stream.map(&finish_lines(&1, 0))
    |> Enum.sort()
    |> get_middle_value()
  end

  defp get_middle_value(list) do
    Enum.at(list, (length(list) - 1) |> div(2))
  end

  defp parse do
    File.stream!("./lib/days/dayten/input.txt")
    |> Stream.map(&String.trim/1)
    |> Stream.map(&String.graphemes/1)
  end

  defp process_line([head | tail], stack) do
    case head do
      "[" ->
        process_line(tail, [head | stack])

      "(" ->
        process_line(tail, [head | stack])

      "{" ->
        process_line(tail, [head | stack])

      "<" ->
        process_line(tail, [head | stack])

      "]" ->
        if hd(stack) != "[", do: {57, stack}, else: process_line(tail, tl(stack))

      ")" ->
        if hd(stack) != "(", do: {3, stack}, else: process_line(tail, tl(stack))

      "}" ->
        if hd(stack) != "{", do: {1197, stack}, else: process_line(tail, tl(stack))

      ">" ->
        if hd(stack) != "<", do: {25137, stack}, else: process_line(tail, tl(stack))

      _ ->
        process_line(tail, stack)
    end
  end

  defp process_line([], stack) do
    {0, stack}
  end

  defp finish_lines([head | tail], acc) do
    case head do
      "(" -> finish_lines(tail, acc * 5 + 1)
      "[" -> finish_lines(tail, acc * 5 + 2)
      "{" -> finish_lines(tail, acc * 5 + 3)
      "<" -> finish_lines(tail, acc * 5 + 4)
    end
  end

  defp finish_lines([], acc) do
    acc
  end
end
