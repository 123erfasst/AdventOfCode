using Xunit;
using Xunit.Abstractions;

namespace Benno.Day1;

public class Part1(ITestOutputHelper output)
{
    private int Calculate(string input) =>
        input.Split(Environment.NewLine)
            .Select(line => string.Concat(line.First(char.IsNumber), line.Last(char.IsNumber)))
            .Select(int.Parse)
            .Sum();

    [Fact]
    private void Test()
    {
        var input = @"
1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet
".Trim();

        var result = Calculate(input);

        Assert.Equal(142, result);
    }

    [Fact]
    private void Run()
    {
        var input = File.ReadAllText("../../../Day1/input");

        var result = Calculate(input);

        output.WriteLine(result.ToString());
    }
}