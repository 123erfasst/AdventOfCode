using System.Text.RegularExpressions;

namespace AdventOfCode;

public class Day8 : Day
{
    private const string Directions = @"([A-Z|1-9]+)";
    private readonly Regex _rgDirections = new Regex(Directions);

    protected override void Part1()
    {
        const string fileName = "./inputs/day8_part1_input.txt";
        var inputLines = File.ReadLines(fileName).ToList();
        var directionString = inputLines.First();
        var elements = inputLines.Skip(2).Select(GetElement).ToDictionary(x => x.Name);
        var start = elements.GetValueOrDefault("AAA");
        if (start == null) return;
        var steps = GetSteps(directionString, elements, start);


        Console.WriteLine(steps);
    }

    private static long GetSteps(string instructions, Dictionary<string, Element> elements, Element start)
    {
        var instructionCount = instructions.Length;
        var current = start;
        var count = 0;

        while (current.Name != "ZZZ")
        {
            var instruction = count % instructionCount;
            current = current.GetNext(instructions[instruction], elements);
            count += 1;
        }

        return count;
    }

    protected override void Part2()
    {
        const string fileName = "./inputs/day8_part2_input.txt";
        var inputLines = File.ReadLines(fileName).ToList();
        var directionString = inputLines.First();
        var elements = inputLines.Skip(2).Select(GetElement).ToList();
        var elementsDict = elements.ToDictionary(x => x.Name);
        var starts = elements.Where(x => x.Name.EndsWith("A")).ToList();
        var steps = GetLcmGhostSteps(directionString, elementsDict, starts);

        Console.WriteLine(steps);
    }

    private static int GetGhostSteps(string instructions, Dictionary<string, Element> elements, Element start)
    {
        var instructionCount = instructions.Length;
        var current = start;
        var count = 0;

        while (!current.Name.EndsWith("Z"))
        {
            var instruction = count % instructionCount;
            current = current.GetNext(instructions[instruction], elements);
            count += 1;
        }

        return count;
    }

    private static long GetLcmGhostSteps(string instructions, Dictionary<string, Element> elements,
        IEnumerable<Element> starts)
    {
        var counts = starts.Select(x => GetGhostSteps(instructions, elements, x)).ToList();
        var res = LcmOfElements(counts);
        return res;
    }

    private Element GetElement(string input)
    {
        var matches = _rgDirections.Matches(input);
        var name = matches[0].Value;
        var left = matches[1].Value;
        var right = matches[2].Value;
        return new Element(name, left, right);
    }

    private static long LcmOfElements(IList<int> elementArray)
    {
        long lcmOfArrayElements = 1;
        var divisor = 2;

        while (true)
        {
            var counter = 0;
            var divisible = false;
            for (var i = 0; i < elementArray.Count; i++)
            {
                switch (elementArray[i])
                {
                    case 0:
                        return 0;
                    case < 0:
                        elementArray[i] *= -1;
                        break;
                }

                if (elementArray[i] == 1)
                {
                    counter++;
                }

                if (elementArray[i] % divisor != 0) continue;
                divisible = true;
                elementArray[i] /= divisor;
            }

            if (divisible)
            {
                lcmOfArrayElements *= divisor;
            }
            else
            {
                divisor++;
            }

            if (counter == elementArray.Count)
            {
                return lcmOfArrayElements;
            }
        }
    }
}

internal class Element
{
    public string Name { get; set; }
    private string Right { get; set; }
    private string Left { get; set; }

    public Element(
        string name,
        string left,
        string right
    )
    {
        Name = name;
        Left = left;
        Right = right;
    }

    public Element GetNext(char command, Dictionary<string, Element> elements)
    {
        var next = elements.GetValueOrDefault(command == 'L' ? Left : Right);
        return next;
    }
}