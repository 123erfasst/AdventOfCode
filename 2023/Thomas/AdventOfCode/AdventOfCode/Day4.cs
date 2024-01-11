using System.Text.RegularExpressions;

namespace AdventOfCode;

public class Day4 : Day
{
    private const string Number = @"[0-9]+";
    private readonly Regex _rgNumber = new Regex(Number);


    protected override void Part1()
    {
        const string fileName = "./inputs/day4_part1_input.txt";
        var inputLines = File.ReadLines(fileName).ToList();
        var sum = inputLines.Select(GetCardValue).Sum();

        Console.WriteLine(sum);
    }

    private int GetCardValue( string input)
    {
        var numberString = input.Split(":")[1];
        var cardSplits = numberString.Split("|");
        var winningSplit = cardSplits[0];
        var selectedSplit = cardSplits[1];
        var winningNumbers = _rgNumber.Matches(winningSplit).Select(x => int.Parse(x.Value)).ToList();
        var selectedNumbers = _rgNumber.Matches(selectedSplit).Select(x => int.Parse(x.Value)).ToList();
        var intersecting = winningNumbers.Intersect(selectedNumbers);
        var res = 0;
        
        foreach (var unused in intersecting)
        {
            if (res == 0) res += 1;
            else res *= 2;
        }

        return res;
    }


    protected override void Part2()
    {
        const string fileName = "./inputs/day4_part2_input.txt";
        var inputLines = File.ReadLines(fileName).ToList();
        var sum = GetCardCount(inputLines);

        Console.WriteLine(sum);
    }
    
    private int GetCardCount( List<string> inputs)
    {
        var cards = inputs.Select(GetCard).ToList();

        for (var cardPosition = 0; cardPosition < cards.Count; cardPosition++)
        {
            for (var cardCount = 0; cardCount < cards[cardPosition].Count; cardCount++)
                for (var i = 1; i <= cards[cardPosition].Value; i++)
                    cards[cardPosition + i].Copy();
        }
        
        var res = cards.Sum(x => x.Count);
        
        return res;
    }
    
    private Card GetCard(string input)
    {
        var numberString = input.Split(":")[1];
        var cardSplits = numberString.Split("|");
        var winningSplit = cardSplits[0];
        var selectedSplit = cardSplits[1];
        var winningNumbers = _rgNumber.Matches(winningSplit).Select(x => int.Parse(x.Value)).ToList();
        var selectedNumbers = _rgNumber.Matches(selectedSplit).Select(x => int.Parse(x.Value)).ToList();
        var intersecting = winningNumbers.Intersect(selectedNumbers);
        var value = intersecting.Count();
        return new Card(value, 1);
    }

}

public class Card
{
    public int Value { get; set; }
    public int Count { get; set; }

    public Card(int value, int count)
    {
        Value = value;
        Count = count;
    }

    public void Copy()
    {
        Count += 1;
    }
    
} 

