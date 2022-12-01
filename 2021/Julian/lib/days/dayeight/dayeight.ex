defmodule Aoc.DayEight do
  def solvePartOne do
    parse()
    |> Stream.map(fn {_, output} -> output |> Enum.map(&String.length/1) end)
    |> Stream.flat_map(& &1)
    |> Enum.frequencies()
    |> Map.filter(fn
      {2, _} -> true
      {3, _} -> true
      {4, _} -> true
      {7, _} -> true
      _ -> false
    end)
    |> Map.values()
    |> Enum.sum()
  end

  def solvePartTwo do
    parse()
    |> Stream.map(fn {input, output} ->
      number_map =
        set_chars_for_one({%{}, input})
        |> set_chars_for_four()
        |> set_chars_for_seven()
        |> set_chars_for_eight()
        |> set_chars_for_three()
        |> set_chars_for_nine()
        |> set_chars_for_five()
        |> set_chars_for_two()
        |> set_chars_for_six()
        |> set_chars_for_zero()
        |> Map.new(fn {key, val} -> {val, key} end)

      output
      |> Enum.map(fn val -> Map.get(number_map, val, 0) end)
      |> Enum.join()
      |> String.to_integer()
    end)
    |> Enum.sum()
  end

  defp parse do
    File.stream!("./lib/days/dayeight/input.txt")
    |> Stream.map(&String.trim/1)
    |> Stream.map(fn line ->
      line
      |> String.split(" | ", trim: true)
      |> parse_line()
    end)
  end

  defp parse_line([input, output | _]) do
    parsed_input =
      input
      |> String.split(" ", trim: true)
      |> Enum.map(fn el ->
        el
        |> String.to_charlist()
        |> Enum.sort()
      end)

    parset_output =
      output
      |> String.split(" ", trim: true)
      |> Enum.map(fn el ->
        el
        |> String.to_charlist()
        |> Enum.sort()
      end)

    {parsed_input, parset_output}
  end

  defp set_chars_for_zero({acc, [last]}) do
    Map.put(acc, 0, last)
  end

  defp set_chars_for_one({acc, input}) do
    input
    |> Enum.split_with(fn x -> length(x) == 2 end)
    |> (fn {[found_item | _], rest} ->
          {Map.put(acc, 1, found_item), rest}
        end).()
  end

  defp set_chars_for_two({acc, input}) do
    input
    |> Enum.split_with(fn x -> length(x) == 5 end)
    |> (fn {[found_item | _], rest} ->
          {Map.put(acc, 2, found_item), rest}
        end).()
  end

  defp set_chars_for_three({acc, input}) do
    seven_chars = Map.get(acc, 7, [])

    input
    |> Enum.split_with(fn x -> length(x) == 5 and same_chars(x, seven_chars) == 3 end)
    |> (fn {[found_item | _], rest} -> {Map.put(acc, 3, found_item), rest} end).()
  end

  defp set_chars_for_four({acc, input}) do
    input
    |> Enum.split_with(fn x -> length(x) == 4 end)
    |> (fn {[found_item | _], rest} -> {Map.put(acc, 4, found_item), rest} end).()
  end

  defp set_chars_for_five({acc, input}) do
    nine_chars = Map.get(acc, 9, [])

    input
    |> Enum.split_with(fn x -> length(x) == 5 and same_chars(x, nine_chars) == 5 end)
    |> (fn {[found_item | _], rest} -> {Map.put(acc, 5, found_item), rest} end).()
  end

  defp set_chars_for_six({acc, input}) do
    five_chars = Map.get(acc, 5, [])

    input
    |> Enum.split_with(fn x -> length(x) == 6 and same_chars(x, five_chars) == 5 end)
    |> (fn {[found_item | _], rest} -> {Map.put(acc, 6, found_item), rest} end).()
  end

  defp set_chars_for_seven({acc, input}) do
    input
    |> Enum.split_with(fn x -> length(x) == 3 end)
    |> (fn {[found_item | _], rest} -> {Map.put(acc, 7, found_item), rest} end).()
  end

  defp set_chars_for_eight({acc, input}) do
    input
    |> Enum.split_with(fn x -> length(x) == 7 end)
    |> (fn {[found_item | _], rest} -> {Map.put(acc, 8, found_item), rest} end).()
  end

  defp set_chars_for_nine({acc, input}) do
    three_chars = Map.get(acc, 3, [])

    input
    |> Enum.split_with(fn x -> length(x) == 6 and same_chars(x, three_chars) == 5 end)
    |> (fn {[found_item | _], rest} -> {Map.put(acc, 9, found_item), rest} end).()
  end

  defp same_chars(input, chars) do
    input
    |> Enum.filter(fn el -> Enum.member?(chars, el) end)
    |> Enum.count()
  end
end
