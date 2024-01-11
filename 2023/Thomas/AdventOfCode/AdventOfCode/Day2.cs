using System.Text.RegularExpressions;

namespace AdventOfCode;

public class Day2 : Day
{
    private const string GamerNumber = @"(Game )(\d*)";
    private readonly Regex _rgGamerNumber = new Regex(GamerNumber);
    private const string RedNumber = @"(\d*) (red)";
    private readonly Regex _rgRedNumber = new Regex(RedNumber);
    private const string GreenNumber = @"(\d*) (green)";
    private readonly Regex _rgGreenNumber = new Regex(GreenNumber);
    private const string BlueNumber = @"(\d*) (blue)";
    private readonly Regex _rgBlueNumber = new Regex(BlueNumber);

    protected override void Part1()
    {
        var allowedCubeSet = new CubeSet(0,12, 13, 14);
        const string fileName = "./inputs/day2_part1_input.txt";
        var inputLines = File.ReadLines(fileName);
        var sum = inputLines.Select(GetMaxSet)
            .Where(x => IsValidCubeSet(allowedCubeSet, x))
            .Select(x => x.Game)
            .Sum();

        Console.WriteLine(sum);
    }

    private CubeSet GetMaxSet(string input)
    {
        var game = int.Parse(_rgGamerNumber.Matches(input).Last().Groups[2].Value);
        var reds = _rgRedNumber.Matches(input).Select(x => int.Parse(x.Groups[1].Value)).Append(0);
        var greens = _rgGreenNumber.Matches(input).Select(x => int.Parse(x.Groups[1].Value)).Append(0);
        var blues = _rgBlueNumber.Matches(input).Select(x => int.Parse(x.Groups[1].Value)).Append(0);

        return new CubeSet(game, reds.Max(), greens.Max(), blues.Max());
    }

    private bool IsValidCubeSet(CubeSet allowed, CubeSet value)
        => allowed.Red >= value.Red && allowed.Green >= value.Green && allowed.Blue >= value.Blue;


    protected override void Part2()
    {
        const string fileName = "./inputs/day2_part2_input.txt";
        var inputLines = File.ReadLines(fileName);
        var sum = inputLines.Select(GetMaxSet)
            .Select(x => x.Power)
            .Sum();

        Console.WriteLine(sum);
    }
}

public class CubeSet
{
    public int Game { get; set; }
    public int Red { get; set; }
    public int Green { get; set; }
    public int Blue { get; set; }
    public int Power => Math.Max(Red, 1) * Math.Max(Green, 1) * Math.Max(Blue, 1);

    public CubeSet(int game, int red, int green, int blue)
    {
        Game = game;
        Red = red;
        Green = green;
        Blue = blue;
    }
}