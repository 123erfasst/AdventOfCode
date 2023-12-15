using Xunit;
using Xunit.Abstractions;

namespace Benno.Day4;

public class Part1(ITestOutputHelper output)
{
    private int Calculate(string input) =>
        input.Split(Environment.NewLine)
            .Select(x => x.Split(": ")[1])
            .Select(x => x.Split('|'))
            .Select(x => (
                winning: x[0].Split(' ', StringSplitOptions.RemoveEmptyEntries).Select(int.Parse),
                scratched: x[1].Split(' ', StringSplitOptions.RemoveEmptyEntries).Select(int.Parse)
            )).Select(x => (int) Math.Pow(2, x.winning.Intersect(x.scratched).Count() - 1))
            .Sum();

    [Fact]
    private void Test()
    {
        var input = @"
Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
".Trim();

        var result = Calculate(input);

        Assert.Equal(13, result);
    }

    [Fact]
    private void Run()
    {
        var input = File.ReadAllText("../../../Day4/input");

        var result = Calculate(input);

        output.WriteLine(result.ToString());
    }
}