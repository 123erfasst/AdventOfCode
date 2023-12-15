using System.Text.RegularExpressions;
using Xunit;
using Xunit.Abstractions;

namespace Benno.Day1;

public class Part2(ITestOutputHelper output)
{
    private static readonly IDictionary<string, string> NumberToChar = new Dictionary<string, string>
    {
        {"one", "1"},
        {"two", "2"},
        {"three", "3"},
        {"four", "4"},
        {"five", "5"},
        {"six", "6"},
        {"seven", "7"},
        {"eight", "8"},
        {"nine", "9"},
        {"zero", "0"}
    };
    
    private static readonly Regex NumberSearchLeft = new(
        "^.*?(1|2|3|4|5|6|7|8|9|one|two|three|four|five|six|seven|eight|nine)",
        RegexOptions.Compiled);
    private static readonly Regex NumberSearchRight = new(
        ".*(1|2|3|4|5|6|7|8|9|one|two|three|four|five|six|seven|eight|nine).*?$",
        RegexOptions.Compiled);
    
    private int Calculate(string input) =>
        input.Split(Environment.NewLine)
            .Select(line => string.Concat(NumberSearchLeft.Match(line).Groups[1], NumberSearchRight.Match(line).Groups[1]))
            .Select(x => NumberToChar.Aggregate(x, (acc, pair) => acc.Replace(pair.Key, pair.Value)))
            .Select(int.Parse)
            .Sum();

    [Fact]
    private void Test()
    {
        var input = @"
two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen
".Trim();

        var result = Calculate(input);
        
        Assert.Equal(281, result);
    }

    [Fact]
    private void Run()
    {
        var input = File.ReadAllText("../../../Day1/input");

        var result = Calculate(input);
        
        output.WriteLine(result.ToString());
    }
}