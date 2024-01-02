using System.Text.RegularExpressions;

namespace AdventOfCode;

public class Day1 : Day
{
    private const string Digit = "([0-9])";
    private readonly Regex _rgDigit = new Regex(Digit);
    private const string DigitAndWord = "([0-9]|one|two|three|four|five|six|seven|eight|nine)";
    private readonly Regex _rgDigitAndWord = new Regex(DigitAndWord);

    protected override void Part1()
    {
        const string fileName = "./inputs/day1_part1_input.txt";
        var inputLines = File.ReadLines(fileName);
        var sum = inputLines.Select(GetNumber).Sum();

        Console.WriteLine(sum);
    }

    private int GetNumber(string input)
    {
        var matches = _rgDigit.Matches(input);
        var first = matches.First().Value;
        var last = matches.Last().Value;
        var res = first + last;
        return int.Parse(res);
    }

    protected override void Part2()
    {
        const string fileName = "./inputs/day1_part2_input.txt";
        var inputLines = File.ReadLines(fileName);
        var sum = inputLines.Select(GetNumberByDigitAndWord).Sum();

        Console.WriteLine(sum);
    }

    private int GetNumberByDigitAndWord(string input)
    {
        var matches = _rgDigitAndWord.Matches(input);
        var first = WordToDigit(matches.First().Value);
        var last = WordToDigit(matches.Last().Value);
        var res = first + last;
        return int.Parse(res);
    }

    private string WordToDigit(string word)
    {
        return word switch
        {
            "one" => "1",
            "two" => "2",
            "three" => "3",
            "four" => "4",
            "five" => "5",
            "six" => "6",
            "seven" => "7",
            "eight" => "8",
            "nine" => "9",
            _ => word
        };
    }
}