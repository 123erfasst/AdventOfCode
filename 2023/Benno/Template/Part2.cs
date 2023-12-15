using System.Text.RegularExpressions;
using Xunit;
using Xunit.Abstractions;

namespace Benno.Template;

public class Part2(ITestOutputHelper output)
{
    private int Calculate(string input)
    {
        return 0;
    }

    [Fact]
    private void Test()
    {
        var input = @"

".Trim();

        var result = Calculate(input);
        
        Assert.Equal(0, result);
    }

    [Fact]
    private void Run()
    {
        var input = File.ReadAllText("../../../Day/input");

        var result = Calculate(input);
        
        output.WriteLine(result.ToString());
    }
}