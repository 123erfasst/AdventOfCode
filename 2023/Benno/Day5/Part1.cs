using Xunit;
using Xunit.Abstractions;

namespace Benno.Day5;

public class Part1(ITestOutputHelper output)
{
    private class Range
    {
        private List<long> destinationStarts = [];
        private List<long> sourceStarts = [];
        private List<long> ranges = [];

        public Range()
        {
        }

        public Range(long destinationStart, long sourceStart, long range)
        {
            destinationStarts.Add(destinationStart);
            sourceStarts.Add(sourceStart);
            ranges.Add(range);
        }

        public static Range operator +(Range left, Range right) => new()
        {
            destinationStarts = left.destinationStarts.Concat(right.destinationStarts).ToList(),
            sourceStarts = left.sourceStarts.Concat(right.sourceStarts).ToList(),
            ranges = left.ranges.Concat(right.ranges).ToList()
        };

        public long this[long source]
        {
            get
            {
                for (var i = 0; i < sourceStarts.Count; i++)
                {
                    if (sourceStarts[i] <= source && source < sourceStarts[i] + ranges[i])
                        return destinationStarts[i] + (source - sourceStarts[i]);
                }

                return source;
            }
        }
    }

    private long Calculate(string input)
    {
        var splits = input.Split(
            new[]
            {
                "seeds:",
                "seed-to-soil map:",
                "soil-to-fertilizer map:",
                "fertilizer-to-water map:",
                "water-to-light map:",
                "light-to-temperature map:",
                "temperature-to-humidity map:",
                "humidity-to-location map:"
            },
            StringSplitOptions.RemoveEmptyEntries
        );

        var seeds = splits[0].Split(' ', StringSplitOptions.RemoveEmptyEntries).Select(long.Parse);

        var seedToSoil = splits[1]
            .Split(Environment.NewLine, StringSplitOptions.RemoveEmptyEntries)
            .Select(x => x.Split(' '))
            .Aggregate(new Range(), (acc, x) => acc + new Range(long.Parse(x[0]), long.Parse(x[1]), long.Parse(x[2])));
        var soilToFertilizer = splits[2]
            .Split(Environment.NewLine, StringSplitOptions.RemoveEmptyEntries)
            .Select(x => x.Split(' '))
            .Aggregate(new Range(), (acc, x) => acc + new Range(long.Parse(x[0]), long.Parse(x[1]), long.Parse(x[2])));
        var fertilizerToWater = splits[3]
            .Split(Environment.NewLine, StringSplitOptions.RemoveEmptyEntries)
            .Select(x => x.Split(' '))
            .Aggregate(new Range(), (acc, x) => acc + new Range(long.Parse(x[0]), long.Parse(x[1]), long.Parse(x[2])));
        var waterToLight = splits[4]
            .Split(Environment.NewLine, StringSplitOptions.RemoveEmptyEntries)
            .Select(x => x.Split(' '))
            .Aggregate(new Range(), (acc, x) => acc + new Range(long.Parse(x[0]), long.Parse(x[1]), long.Parse(x[2])));
        var lightToTemperature = splits[5]
            .Split(Environment.NewLine, StringSplitOptions.RemoveEmptyEntries)
            .Select(x => x.Split(' '))
            .Aggregate(new Range(), (acc, x) => acc + new Range(long.Parse(x[0]), long.Parse(x[1]), long.Parse(x[2])));
        var temperatureToHumidity = splits[6]
            .Split(Environment.NewLine, StringSplitOptions.RemoveEmptyEntries)
            .Select(x => x.Split(' '))
            .Aggregate(new Range(), (acc, x) => acc + new Range(long.Parse(x[0]), long.Parse(x[1]), long.Parse(x[2])));
        var humidityToLocation = splits[7]
            .Split(Environment.NewLine, StringSplitOptions.RemoveEmptyEntries)
            .Select(x => x.Split(' '))
            .Aggregate(new Range(), (acc, x) => acc + new Range(long.Parse(x[0]), long.Parse(x[1]), long.Parse(x[2])));

        return seeds.Select(x => humidityToLocation[
                temperatureToHumidity[
                    lightToTemperature[
                        waterToLight[
                            fertilizerToWater[
                                soilToFertilizer[
                                    seedToSoil[x]]]]]]])
            .Min();
    }

    [Fact]
    private void Test()
    {
        var input = @"
seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4
".Trim();

        var result = Calculate(input);

        Assert.Equal(35, result);
    }

    [Fact]
    private void Run()
    {
        var input = File.ReadAllText("../../../Day5/input");

        var result = Calculate(input);

        output.WriteLine(result.ToString());
    }
}