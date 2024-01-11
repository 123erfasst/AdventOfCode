using System.Text.RegularExpressions;
using Xunit;
using Xunit.Abstractions;

namespace Benno.Day3;

public class Part2(ITestOutputHelper output)
{
    private static readonly Regex Numbers = new(@"\d+", RegexOptions.Compiled);
    
    private int Calculate(string input) =>
        input.Split(Environment.NewLine)
            .Window()
            .SelectMany(window =>
            {
                var (previous, current, next) = window;
                var result = new List<int>();

                var numberMatches = Numbers.Matches(previous ?? "")
                    .Concat(Numbers.Matches(current))
                    .Concat(Numbers.Matches(next ?? ""))
                    .ToList();

                for (var i = 0; i < current.Length; i++)
                {
                    var cursor = current[i];
                    if (cursor != '*') continue;

                    var adjacentNumbers = numberMatches.FindAll(x => x.Index - 1 <= i && i <= x.Index + x.Length);

                    if (adjacentNumbers.Count == 2)
                        result.Add(int.Parse(adjacentNumbers[0].Value) * int.Parse(adjacentNumbers[1].Value));
                }

                return result;
            })
            .Sum();

    [Fact]
    private void Test()
    {
        var input = @"
467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..
".Trim();

        var result = Calculate(input);
        
        Assert.Equal(467835, result);
    }

    [Fact]
    private void Run()
    {
        var input = File.ReadAllText("../../../Day3/input");

        var result = Calculate(input);
        
        output.WriteLine(result.ToString());
    }
}