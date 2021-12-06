defmodule Aoc.DayFour do
  def solvePartOne do
    {:ok, contents} = File.read("./lib/days/dayfour/input.txt")
    [input: input, boards: board] = parseInput(contents)

    run_bingo(board, input)
    |> calculate_score()
  end

  def solvePartTwo do
    {:ok, contents} = File.read("./lib/days/dayfour/input.txt")
    [input: input, boards: board] = parseInput(contents)

    run_losing_bingo(board, input)
    |> calculate_score()
  end

  defp parseInput(s) do
    [input | boards] = String.split(s, "\n\n", trim: true)

    processed_boards =
      boards
      |> Enum.map(&process_boards(&1))
      |> Enum.map(fn value -> value ++ Enum.zip_with(value, fn x -> x end) end)

    [input: process_input(input), boards: processed_boards]
  end

  defp process_boards(board) do
    board
    |> String.split("\n", trim: true)
    |> Enum.map(&process_row(&1))
  end

  defp process_row(row) do
    row
    |> String.split(" ", trim: true)
    |> Enum.map(fn x -> String.to_integer(x) end)
  end

  defp process_input(input) do
    input
    |> String.split(",", trim: true)
    |> Enum.map(&String.to_integer(&1))
  end

  defp run_bingo(boards, [head | tail]) do
    filtered_boards =
      boards
      |> Enum.map(&filter_board(&1, head))

    case find_winning_board(filtered_boards) do
      nil -> run_bingo(filtered_boards, tail)
      winning_board -> [winning_number: head, board: winning_board]
    end
  end

  defp filter_board(board, value) do
    board
    |> Enum.map(fn row ->
      row
      |> Enum.filter(fn cell -> cell != value end)
    end)
  end

  defp has_winning_row(board) do
    board
    |> Enum.any?(fn row -> length(row) == 0 end)
  end

  defp find_winning_board(boards) do
    boards
    |> Enum.find(&has_winning_row(&1))
  end

  defp calculate_score(winning_number: num, board: board) do
    unmarked =
      board
      |> List.flatten()
      |> Enum.uniq()
      |> Enum.sum()

    unmarked * num
  end

  defp run_losing_bingo(boards, [head | tail]) do
    filtered_boards =
      boards
      |> Enum.map(&filter_board(&1, head))
      |> Enum.filter(fn board -> !has_winning_row(board) end)

    case filtered_boards do
      [losing_board | []] -> run_bingo([losing_board], tail)
      [_ | _] -> run_losing_bingo(filtered_boards, tail)
    end
  end
end
