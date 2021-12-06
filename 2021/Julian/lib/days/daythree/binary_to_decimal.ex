defmodule Aoc.DayThree.BinaryToDecimal do

    def binary_to_decimal n do
        digits = Integer.digits n
        sum_up digits, 0
    end

    def sum_up([], acc), do: acc
    def sum_up list, acc do
        [ head | tail ] = list
        length = Enum.count list
        if head === 1 do
            sum_up tail, acc + (:math.pow(2, length - 1) |> round)
        else
            if head !== 0 do
                raise "Encountered a digit other than 0 or 1"
            else
                sum_up tail, acc
            end
        end
    end
end
