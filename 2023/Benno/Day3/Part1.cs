using Xunit;
using Xunit.Abstractions;

namespace Benno.Day3;

public class Part1(ITestOutputHelper output)
{
    private int Calculate(string input) =>
        input.Split(Environment.NewLine)
            .Window()
            .SelectMany(window =>
            {
                var (previous, current, next) = window;
                var result = new List<int>();

                int? startIndex = null;
                for (var i = 0; i < current.Length; i++)
                {
                    var cursor = current[i];
                    if (char.IsNumber(cursor) && !startIndex.HasValue)
                        startIndex = i;
                    if (startIndex.HasValue && (!char.IsNumber(cursor) || i + 1 == current.Length))
                    {
                        var checkStartIndex = Math.Max(startIndex.Value - 1, 0);

                        var checkString = previous?.Substring(checkStartIndex, i + 1 - checkStartIndex) +
                                          next?.Substring(checkStartIndex, i + 1 - checkStartIndex) +
                                          (checkStartIndex == 0 ? "" : current[checkStartIndex]) +
                                          (char.IsNumber(cursor) ? "" : current[i]);
                        var number = int.Parse(current.Substring(startIndex.Value, i - startIndex.Value + (char.IsNumber(cursor) ? 1 : 0)));
                        if (checkString.Any(x => x != '.'))
                            result.Add(number);

                        startIndex = null;
                    }
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

        Assert.Equal(4361, result);
    }

    [Fact]
    private void Run()
    {
        var input = File.ReadAllText("../../../Day3/input");

        var result = Calculate(input);
        
        output.WriteLine(result.ToString());
    }
}