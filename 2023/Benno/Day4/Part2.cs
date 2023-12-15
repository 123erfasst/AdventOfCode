using Xunit;
using Xunit.Abstractions;

namespace Benno.Day4;

public class Part2(ITestOutputHelper output)
{
    private int Calculate(string input) =>
        input.Split(Environment.NewLine)
            .Pipe(rows => rows
                .Select(x => x.Split(": ")[1])
                .Select(x => x.Split('|'))
                .Select(x => (
                    winning: x[0].Split(' ', StringSplitOptions.RemoveEmptyEntries).Select(int.Parse),
                    scratched: x[1].Split(' ', StringSplitOptions.RemoveEmptyEntries).Select(int.Parse)
                )).Select(x => x.winning.Intersect(x.scratched).Count())
                .ToList()
                .Pipe(wins =>
                {
                    var result = Enumerable.Repeat(1, wins.Count).ToList();
                    for (var i = 0; i < wins.Count; i++)
                        if (wins[i] > 0)
                            for (var r = 0; r < result[i]; r++)
                                for (var j = 1; j <= wins[i]; j++)
                                    result[i + j] += 1;
                    return result;
                })
                .Sum());

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

        Assert.Equal(30, result);
    }

    [Fact]
    private void Run()
    {
        var input = File.ReadAllText("../../../Day4/input");

        var result = Calculate(input);

        output.WriteLine(result.ToString());
    }
}