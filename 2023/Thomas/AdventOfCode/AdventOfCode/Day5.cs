using System.Text.RegularExpressions;

namespace AdventOfCode;

public class Day5 : Day
{
    private const string Number = @"[0-9]+";
    private readonly Regex _rgNumber = new Regex(Number);

    private const string SeedRange = @"([0-9]+ [0-9]+)";
    private readonly Regex _rgSeedRange = new Regex(SeedRange);

    protected override void Part1()
    {
        var seed = "./inputs/day5/seeds.txt";
        var seedSoil = "./inputs/day5/seedSoil.txt";
        var soilFertilizer = "./inputs/day5/soilFertilizer.txt";
        var fertilizerWater = "./inputs/day5/fertilizerWater.txt";
        var waterLight = "./inputs/day5/waterLight.txt";
        var lightTemperature = "./inputs/day5/lightTemperature.txt";
        var temperatureHumidity = "./inputs/day5/temperatureHumidity.txt";
        var humidityLocation = "./inputs/day5/humidityLocation.txt";
        var seeds = GetSeeds(seed);
        var seedSoils = GetPlantingMapping(seedSoil);
        var soilFertilizers = GetPlantingMapping(soilFertilizer);
        var fertilizerWaters = GetPlantingMapping(fertilizerWater);
        var waterLights = GetPlantingMapping(waterLight);
        var lightTemperatures = GetPlantingMapping(lightTemperature);
        var temperatureHumidities = GetPlantingMapping(temperatureHumidity);
        var humidityLocations = GetPlantingMapping(humidityLocation);


        var plantingInfos = seeds.Select(x => GetPlantingInfo(
            seedSoils,
            soilFertilizers,
            fertilizerWaters,
            waterLights,
            lightTemperatures,
            temperatureHumidities,
            humidityLocations,
            x
        ));
        var lowestLocation = plantingInfos.Select(x => x.Location).Min();

        Console.WriteLine(lowestLocation);
    }

    private PlantIngInfo GetPlantingInfo(
        List<PlantingMapping> seedSoils,
        List<PlantingMapping> soilFertilizers,
        List<PlantingMapping> fertilizerWaters,
        List<PlantingMapping> waterLights,
        List<PlantingMapping> lightTemperatures,
        List<PlantingMapping> temperatureHumidities,
        List<PlantingMapping> humidityLocations,
        long seed
    )
    {
        var soil = GetLinked(seedSoils, seed);
        var fertilizer = GetLinked(soilFertilizers, soil);
        var water = GetLinked(fertilizerWaters, fertilizer);
        var light = GetLinked(waterLights, water);
        var temperature = GetLinked(lightTemperatures, light);
        var humidity = GetLinked(temperatureHumidities, temperature);
        var location = GetLinked(humidityLocations, humidity);

        return new PlantIngInfo(
            seed,
            soil,
            fertilizer,
            water,
            light,
            temperature,
            humidity,
            location
        );
    }

    private long GetLinked(List<PlantingMapping> links, long source)
    {
        var mapping = links.FirstOrDefault(link => link.Intersects(source));

        return mapping?.GetLinked(source) ?? source;
    }

    private IEnumerable<long> GetSeeds(string path)
    {
        var seedsInput = File.ReadLines(path).ToList();
        var seeds = seedsInput.SelectMany(input => _rgNumber.Matches(input).Select(x => long.Parse(x.Value)));
        return seeds;
    }

    private List<PlantingMapping> GetPlantingMapping(string path)
    {
        var seedSoils = File.ReadLines(path).ToList();
        var mappings = seedSoils
            .Select(seedSoil => _rgNumber.Matches(seedSoil).Select(x => long.Parse(x.Value)).ToList())
            .Select(matches => new PlantingMapping(matches[0], matches[1], matches[2]))
            .ToList();
        return mappings;
    }


    protected override void Part2()
    {
        var seed = "./inputs/day5/seeds.txt";
        var seedSoil = "./inputs/day5/seedSoil.txt";
        var soilFertilizer = "./inputs/day5/soilFertilizer.txt";
        var fertilizerWater = "./inputs/day5/fertilizerWater.txt";
        var waterLight = "./inputs/day5/waterLight.txt";
        var lightTemperature = "./inputs/day5/lightTemperature.txt";
        var temperatureHumidity = "./inputs/day5/temperatureHumidity.txt";
        var humidityLocation = "./inputs/day5/humidityLocation.txt";
        var seeds = GetSeedRanges(seed);
        var seedSoils = GetPlantingMapping(seedSoil);
        var soilFertilizers = GetPlantingMapping(soilFertilizer);
        var fertilizerWaters = GetPlantingMapping(fertilizerWater);
        var waterLights = GetPlantingMapping(waterLight);
        var lightTemperatures = GetPlantingMapping(lightTemperature);
        var temperatureHumidities = GetPlantingMapping(temperatureHumidity);
        var humidityLocations = GetPlantingMapping(humidityLocation);


        var seedSoilsMapped = GetLinkedRanges(seedSoils, seeds);
        var fertilizersMapped = GetLinkedRanges(soilFertilizers, seedSoilsMapped);
        var watersMapped = GetLinkedRanges(fertilizerWaters, fertilizersMapped);
        var lightsMapped = GetLinkedRanges(waterLights, watersMapped);
        var temperaturesMapped = GetLinkedRanges(lightTemperatures, lightsMapped);
        var humiditiesMapped = GetLinkedRanges(temperatureHumidities, temperaturesMapped);
        var locationsMapped = GetLinkedRanges(humidityLocations, humiditiesMapped);

        var lowestLocation = locationsMapped.Select(x => x.Start).Min();

        Console.WriteLine(lowestLocation);
    }

    private List<SeedRange> GetSeedRanges(string path)
    {
        var seedsInput = File.ReadLines(path).ToList();
        var seedRanges = seedsInput.SelectMany(input => _rgSeedRange.Matches(input).Select(x =>
        {
            var splits = _rgNumber.Matches(x.Value).Select(y => long.Parse(y.Value)).ToList();
            return new SeedRange(splits.First(), splits.First() + splits.Last() - 1);
        })).ToList();
        return seedRanges;
    }

    private List<SeedRange> GetLinkedRanges(List<PlantingMapping> links, List<SeedRange> sources)
    {
        var res = new List<SeedRange>();
        var partsToMap = sources;
        foreach (var link in links)
        {
            var linkResult = MapRange(link, partsToMap);
            partsToMap = linkResult.toMap;
            res.AddRange(linkResult.mapped);
        }
        res.AddRange(partsToMap);
        return res;
    }
    
    private (List<SeedRange> mapped, List<SeedRange> toMap) MapRange(PlantingMapping link, List<SeedRange> sources)
    {
        var leftRangeParts = new List<SeedRange>();
        var mappedRanges = new List<SeedRange>();

        foreach (var part in sources)
        {
            var leftRange = part;
            if (leftRange.End < link.SourceStart || part.Start > link.SourceStart + link.Range)
            {
                leftRangeParts.Add(part);
                continue;
            }
            
            if (leftRange.Start < link.SourceStart)
            {
                leftRangeParts.Add(new SeedRange(leftRange.Start, link.SourceStart - 1));
                leftRange = new SeedRange(link.SourceStart, leftRange.End);
            }

            if ( leftRange.End > link.SourceStart + link.Range - 1)
            {
                leftRangeParts.Add(new SeedRange(link.SourceStart + link.Range, leftRange.End));
                leftRange = new SeedRange(leftRange.Start, link.SourceStart + link.Range);
            }

            var mappedRange = link.MapRange(leftRange);
            mappedRanges.Add(mappedRange);
        }

        return (mappedRanges, leftRangeParts);

    }

}

public class SeedRange
{
    public long Start { get; set; }
    public long End { get; set; }

    public SeedRange(long start, long end)
    {
        Start = start;
        End = end;
    }
}

public class PlantIngInfo
{
    public long Seed { get; set; }
    public long Soil { get; set; }
    public long Fertilizer { get; set; }
    public long Water { get; set; }
    public long Light { get; set; }
    public long Temperature { get; set; }
    public long Humidity { get; set; }
    public long Location { get; set; }


    public PlantIngInfo(
        long seed,
        long soil,
        long fertilizer,
        long water,
        long light,
        long temperature,
        long humidity,
        long location)
    {
        Seed = seed;
        Soil = soil;
        Fertilizer = fertilizer;
        Water = water;
        Light = light;
        Temperature = temperature;
        Humidity = humidity;
        Location = location;
    }
}

public class PlantingMapping
{
    public long TargetStart { get; set; }
    public long SourceStart { get; set; }
    public long Range { get; set; }

    public PlantingMapping(
        long targetStart,
        long sourceStart,
        long range)
    {
        TargetStart = targetStart;
        SourceStart = sourceStart;
        Range = range;
    }

    public bool Intersects(long source)
        => source <= SourceStart + Range && source >= SourceStart;

    public long GetLinked(long source)
    {
        var toAdd = source - SourceStart;
        var linked = TargetStart + toAdd;
        return linked;
    }

    public SeedRange MapRange(SeedRange source)
    {
        var toAdd = SourceStart - TargetStart;
        var linkedStart = source.Start - toAdd;
        var linkedEnd = source.End - toAdd;
        return new SeedRange(linkedStart, linkedEnd);
    }
}