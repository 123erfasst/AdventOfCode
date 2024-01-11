using System.Text.RegularExpressions;

namespace AdventOfCode;

public class Day6 : Day
{
    private const string Number = @"[0-9]+";
    private readonly Regex _rgNumber = new Regex(Number);

    protected override void Part1()
    {
        const string fileName = "./inputs/day6_part1_input.txt";
        var inputLines = File.ReadLines(fileName).ToList();
        var races = GetRaces(inputLines);
        var sum = races.Select(GetWinningCount).Aggregate((x, y) => x * y);
        

        Console.WriteLine(sum);
    }
    
    protected override void Part2()
    {
        const string fileName = "./inputs/day6_part2_input.txt";
        var inputLines = File.ReadLines(fileName).ToList();
        var race = GetRace(inputLines);
        var sum = GetTimes(race.Time, race.Distance);
        
        Console.WriteLine(sum);
    }

    private List<Race> GetRaces(List<string> inputLines)
    {
        var raceTimes = inputLines.First();
        var raceRecord = inputLines.Last();
        var times = _rgNumber.Matches(raceTimes).Select(x =>  int.Parse(x.Value)).ToList();
        var distances = _rgNumber.Matches(raceRecord).Select(x =>  int.Parse(x.Value)).ToList();

        var races = times.Select((x, i) => new Race(x, distances[i])).ToList();
        return races;
    }
    
    private LongRace GetRace(List<string> inputLines)
    {
        var raceTimes = inputLines.First();
        var raceRecord = inputLines.Last();
        var time = _rgNumber.Matches(raceTimes.Replace(" ", ""))
            .Select(x =>  long.Parse(x.Value))
            .ToList()
            .First();
        var distance = _rgNumber.Matches(raceRecord.Replace(" ", ""))
            .Select(x =>  long.Parse(x.Value))
            .ToList()
            .First();

        var race = new LongRace(time, distance);
        return race;
    }
    
    private int GetWinningCount(Race race)
    {
        var wins=  Enumerable.Range(0, race.Time)
            .Select(x => GetDistance(x, race.Time))
            .Where(x => x > race.Distance);
        return wins.Count();
    }

    private int GetDistance(int seconds, int time)
    {
        var res = (time - seconds) * seconds;
        return res;
    }
    
    private long GetTimes(double time, double distance)
    {
        var last = time / 2 + Math.Sqrt(0 - distance + time * time / 4);
        var first = time / 2 - Math.Sqrt(0 - distance + time * time / 4);
        
        return (long)last - (long)first;
    }
}

public class Race
{
    public int Time { get; set; }
    public int Distance { get; set; }

    public Race(int time, int distance)
    {
        Time = time;
        Distance = distance;
    }
}

public class LongRace
{
    public long Time { get; set; }
    public long Distance { get; set; }

    public LongRace(long time, long distance)
    {
        Time = time;
        Distance = distance;
    }
}
