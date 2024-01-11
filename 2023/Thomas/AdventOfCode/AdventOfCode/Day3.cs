using System.Text.RegularExpressions;

namespace AdventOfCode;

public class Day3 : Day
{
    private const string PartNumber = @"([0-9]+)";
    private readonly Regex _rgPartNumber = new Regex(PartNumber);

    private const string Symbol = @"[^0-9,^.]";
    private readonly Regex _rgSymbol = new Regex(Symbol);
    private const string Gear = @"\*";
    private readonly Regex _rgGear = new Regex(Gear);


    protected override void Part1()
    {
        const string fileName = "./inputs/day3_part1_input.txt";
        var inputLines = File.ReadLines(fileName).ToList();
        var sum = SumConnectedPartNumbers(inputLines);


        Console.WriteLine(sum);
    }

    private int SumConnectedPartNumbers(List<string> inputLines)
    {
        var res = new List<int>();
        for (var i = 0; i < inputLines.Count; i++)
        {
            res = res.Concat(GetConnectedEngineParts(inputLines, i)).ToList();
        }

        return res.Sum();
    }

    private IEnumerable<int> GetConnectedEngineParts(List<string> inputLines, int i)
    {
        var pre = i == 0 ? "" : inputLines[i - 1];
        var current = inputLines[i];
        var next = i == inputLines.Count - 1 ? "" : inputLines[i + 1];

        var matches = _rgPartNumber.Matches(current);
        var prevSymbols = _rgSymbol.Matches(pre).Select(x => x.Index);
        var currentSymbols = _rgSymbol.Matches(current).Select(x => x.Index);
        var nextSymbols = _rgSymbol.Matches(next).Select(x => x.Index);
        var symbolPositions = prevSymbols.Concat(currentSymbols).Concat(nextSymbols).ToHashSet();
        var engineParts = matches.Select(x => new EnginePart(x.Index, x.Length, int.Parse(x.Value)));

        var connectedEngineParts = engineParts.Where(part => part.ConnectingArea.Overlaps(symbolPositions)).ToList();
        var res = connectedEngineParts.Select(part => part.Value);
        return res;
    }

    protected override void Part2()
    {
        const string fileName = "./inputs/day3_part2_input.txt";
        var inputLines = File.ReadLines(fileName).ToList();
        var sum = AddGearRatios(inputLines);
        Console.WriteLine(sum);
    }

    private int AddGearRatios(List<string> inputLines)
    {
        return inputLines.Select((t, i) => GetConnectedGears(inputLines, i)).Sum();
    }

    private int GetConnectedGears(List<string> inputLines, int i)
    {
        var pre = i == 0 ? "" : inputLines[i - 1];
        var current = inputLines[i];
        var next = i == inputLines.Count - 1 ? "" : inputLines[i + 1];

        var gears = _rgGear.Matches(current);
        var prevParts = _rgPartNumber.Matches(pre).Select(x => new EnginePart(x.Index, x.Length, int.Parse(x.Value)));
        var currentParts = _rgPartNumber.Matches(current)
            .Select(x => new EnginePart(x.Index, x.Length, int.Parse(x.Value)));
        var nextParts = _rgPartNumber.Matches(next).Select(x => new EnginePart(x.Index, x.Length, int.Parse(x.Value)));
        var allParts = prevParts.Concat(currentParts).Concat(nextParts).ToList();
        var engineGears = gears.Select(x => x.Index).ToHashSet();

        return engineGears.Select(gear => allParts.Where(part => part.ConnectingArea.Contains(gear)).ToList())
            .Where(connectedParts => connectedParts.Count() >= 2)
            .Aggregate(0,
                (current1, connectedParts) =>
                    current1 + (connectedParts.Select(x => x.Value).Aggregate((x, y) => x * y)));
    }
}

public class EnginePart
{
    public int Index { get; set; }
    public int Length { get; set; }
    public int Value { get; set; }

    public HashSet<int> ConnectingArea =>
        Enumerable.Range(Math.Max(Index - 1, 0), Length + (Index == 0 ? 1 : 2)).ToHashSet();

    public EnginePart(
        int index,
        int length,
        int value)
    {
        Index = index;
        Length = length;
        Value = value;
    }
}