defmodule Aoc.DayThree do
  alias Aoc.DayThree.BinaryToDecimal

  def solvePartOne do
    parsed_value =
      parse()
      |> Enum.zip_with(&fold_row(&1))
      |> Enum.map(&get_most_common_bit(&1))

    gamma_value = BinaryToDecimal.sum_up(parsed_value, 0)

    epsilon_value =
      parsed_value
      |> reverse_binary_value_list()
      |> BinaryToDecimal.sum_up(0)

    gamma_value * epsilon_value
  end

  def solvePartTwo do
    parse_result = parse()

    oxygen_rating =
      calculate_rating(&get_most_common_bit(&1), parse_result, [])
      |> BinaryToDecimal.sum_up(0)

    scrubber_rating =
      calculate_rating(&get_least_common_bit(&1), parse_result, [])
      |> BinaryToDecimal.sum_up(0)

    oxygen_rating * scrubber_rating
  end

  defp parse do
    {:ok, contents} = File.read("./lib/days/daythree/input.txt")

    contents
    |> String.split("\n", trim: true)
    |> Enum.map(fn value ->
      String.split(value, "", trim: true) |> Enum.map(&String.to_integer/1)
    end)
  end

  defp fold_row(x) do
    [value: Enum.sum(x), length: length(x)]
  end

  def get_most_common_bit(value: x, length: y) do
    if x >= y / 2 do
      1
    else
      0
    end
  end

  def get_least_common_bit(value: x, length: y) do
    if x >= y / 2 do
      0
    else
      1
    end
  end

  defp reverse_binary_value_list([head | tail]) do
    case head do
      0 -> [1 | reverse_binary_value_list(tail)]
      1 -> [0 | reverse_binary_value_list(tail)]
      _ -> []
    end
  end

  defp reverse_binary_value_list([]) do
    []
  end

  def calculate_rating(comp, x = [_ | _], acc) do
    most_common =
      x
      |> swap_dimensions()
      |> hd()
      |> fold_row()
      |> comp.()

    filtered_list = Enum.filter(x, fn value -> hd(value) == most_common end)

    case filtered_list do
      [head | []] -> (head ++ acc) |> Enum.reverse()
      [_ | _] -> calculate_rating(comp, remove_first_elements(filtered_list), [most_common | acc])
      _ -> [most_common | acc] |> Enum.reverse()
    end
  end

  def calculate_rating(_, _, acc) do
    acc
    |> Enum.reverse()
  end

  defp swap_dimensions(value) do
    Enum.zip_with(value, fn x -> x end)
  end

  defp remove_first_elements(value) do
    value
    |> swap_dimensions()
    |> tl()
    |> swap_dimensions()
  end
end
