using System.Text.RegularExpressions;

namespace AdventOfCode;

public class Day7 : Day
{
    private const string Number = @"([0-9,A,K,Q,J,T]+)";
    private readonly Regex _rgNumber = new Regex(Number);

    protected override void Part1()
    {
        const string fileName = "./inputs/day7_part1_input.txt";
        var inputLines = File.ReadLines(fileName).ToList();
        var sum = inputLines.Select(GetRound)
            .OrderBy(x => x.HandType)
            .ThenBy(x => x.NumberValue)
            .ToList()
            .Select((x, i) => x.Bid * (i + 1))
            .Sum();


        Console.WriteLine(sum);
    }

    private Round GetRound(string input)
    {
        var matches = _rgNumber.Matches(input);
        var hand = matches.First().Value;
        var bit = int.Parse(matches.Last().Value);
        return new Round(hand, bit, false);
    }
    private Round GetRoundWithJoker(string input)
    {
        var matches = _rgNumber.Matches(input);
        var hand = matches.First().Value;
        var bit = int.Parse(matches.Last().Value);
        return new Round(hand, bit, true);
    }


    protected override void Part2()
    {
        const string fileName = "./inputs/day7_part2_input.txt";
        var inputLines = File.ReadLines(fileName).ToList();
        var res = inputLines.Select(GetRoundWithJoker)
            .OrderBy(x => x.HandType)
            .ThenBy(x => x.NumberValue)
            .ToList();
        res.ForEach(x =>Console.WriteLine($@"Value: {x.Value}  Type: {x.HandType.ToString()}   NValue: {x.NumberValue}"));
            
        var sum    = res.Select((x, i) => x.Bid * (i + 1))
            .Sum();
        

        Console.WriteLine(sum);
    }
}

public enum HandType
{
    HighCard,
    OnePair,
    TwoPair,
    ThreeOfAKind,
    FullHouse,
    FourOfAKind,
    FiveOfAKind,
}

public class Round
{
    private const string Groups = @"(.)\1+";
    private readonly Regex _rgGroups = new Regex(Groups);

    private const string GroupsWithoutJoker = @"([2-9,A,K,Q,T])\1+";
    private readonly Regex _rgGroupsWithoutJoker = new Regex(GroupsWithoutJoker);
    public string Value { get; set; }
    public bool WithJoker { get; set; }
    public long NumberValue => GetNumberValue();
    public HandType HandType => GetHandType();
    public int Bid { get; set; }


    public Round(
        string value,
        int bid,
        bool withJoker
    )
    {
        Value = value;
        Bid = bid;
        WithJoker = withJoker;
    }

    private long GetNumberValue()
    {
        var number = Value.Select(CharToNumber)
            .Reverse()
            .Select((x, position) => (long) (x * Math.Pow(100, position)))
            .Sum();
        return number;
    }

    private long CharToNumber(char input)
    {
        return input switch
        {
            'A' => 14,
            'K' => 13,
            'Q' => 12,
            'J' => WithJoker ? 1 : 11,
            'T' => 10,
            _ => long.Parse(input.ToString())
        };
    }

    private HandType GetHandType()
    {
        var sortedHand = string.Join("", Value.ToCharArray().OrderBy(x => x).ToArray());
        var matches = _rgGroups.Matches(sortedHand);

        if (!WithJoker)
            switch (matches.Count)
            {
                case 0:
                    return HandType.HighCard;
                case 1:
                    if (matches.Any(match => match.Length == 2)) return HandType.OnePair;
                    if (matches.Any(match => match.Length == 3)) return HandType.ThreeOfAKind;
                    if (matches.Any(match => match.Length == 4)) return HandType.FourOfAKind;
                    if (matches.Any(match => match.Length == 5)) return HandType.FiveOfAKind;
                    throw new ArgumentOutOfRangeException("Hand does not fir to any Type");
                case 2:
                {
                    var first = matches.First();
                    var last = matches.Last();
                    if (first.Length == 3 || last.Length == 3) return HandType.FullHouse;
                    return HandType.TwoPair;
                }
                case 3:
                    return HandType.TwoPair;
                default:
                    throw new ArgumentOutOfRangeException("Hand does not fir to any Type");
            }

        var jokerCount = Value.Count(x => x == 'J');
        if (jokerCount == 5) return HandType.FiveOfAKind;
        var matchesWithoutJoker = _rgGroupsWithoutJoker.Matches(sortedHand);
        var maxMatchLength = matchesWithoutJoker.Count > 0 ? matchesWithoutJoker.Select(x => x.Length).Max() : jokerCount > 0 ? 1 : 0;
        var maxLengthWithJoker = maxMatchLength + jokerCount;

        return maxLengthWithJoker switch
        {
            2 when matchesWithoutJoker.Count() == 2 => HandType.TwoPair,
            3 when matchesWithoutJoker.Count() == 2 => HandType.FullHouse,
            _ => maxLengthWithJoker switch
            {
                0 => HandType.HighCard,
                1 => HandType.OnePair,
                2 => HandType.OnePair,
                3 => HandType.ThreeOfAKind,
                4 => HandType.FourOfAKind,
                5 => HandType.FiveOfAKind,
                _ => throw new ArgumentOutOfRangeException("Hand does not fir to any Type")
            }
        };
    }
}

// 246323253 too high
// 246285222
// 245906949 too low
