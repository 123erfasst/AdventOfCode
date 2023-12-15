using Xunit;
using Xunit.Abstractions;

namespace Benno.Day2;

public class Part2(ITestOutputHelper output)
{
    private int Calculate(string input) =>
        input.Split(Environment.NewLine)
            .Select(line => line.Split(':'))
            .Select(x => (
                game: int.Parse(x[0].Substring("Game ".Length)),
                sets: x[1].Split(';')
                    .SelectMany(y =>
                        y.Split(',')
                            .Select(z => z.Split(' ', StringSplitOptions.RemoveEmptyEntries)))
                    .ToLookup(k => k[1], k => int.Parse(k[0]))
            ))
            .Select(x => x.sets["red"].Max() * x.sets["green"].Max() * x.sets["blue"].Max())
            .Sum();

    [Fact]
    private void Test()
    {
        var input = @"
Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
".Trim();

        var result = Calculate(input);

        Assert.Equal(2286, result);
    }

    [Fact]
    private void Run()
    {
        var input = File.ReadAllText("../../../Day2/input");

        var result = Calculate(input);

        output.WriteLine(result.ToString());
    }
}