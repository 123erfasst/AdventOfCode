const input = [];

const sampleInput = [
    "seeds: 79 14 55 13",
    "",
    "seed-to-soil map:",
    "50 98 2",
    "52 50 48",
    "",
    "soil-to-fertilizer map:",
    "0 15 37",
    "37 52 2",
    "39 0 15",
    "",
    "fertilizer-to-water map:",
    "49 53 8",
    "0 11 42",
    "42 0 7",
    "57 7 4",
    "",
    "water-to-light map:",
    "88 18 7",
    "18 25 70",
    "",
    "light-to-temperature map:",
    "45 77 23",
    "81 45 19",
    "68 64 13",
    "",
    "temperature-to-humidity map:",
    "0 69 1",
    "1 0 69",
    "",
    "humidity-to-location map:",
    "60 56 37",
    "56 93 4",
];

function SeedResolver(input) {
    this.seeds = input[0].substring(7).split(" ").map(Number);

    const getMapInput = (mapName) => {
        const result = [];
        let collect = false;
        for (const line of input) {
            if (line.startsWith(mapName)) collect = true;
            else if (line === "") collect = false;
            else if (collect) result.push(line);
        }
        return result;
    }

    this.seedToSoilMap = new CompleteMap(getMapInput("seed-to-soil"));
    this.soilToFertilizerMap = new CompleteMap(getMapInput("soil-to-fertilizer"));
    this.fertilizerToWaterMap = new CompleteMap(getMapInput("fertilizer-to-water"));
    this.waterToLightMap = new CompleteMap(getMapInput("water-to-light"));
    this.lightToTemperatureMap = new CompleteMap(getMapInput("light-to-temperature"));
    this.temperatureToHumidityMap = new CompleteMap(getMapInput("temperature-to-humidity"));
    this.humidityToLocationMap = new CompleteMap(getMapInput("humidity-to-location"));
}

SeedResolver.prototype.getSeedLocation = function(seed) {
    const soil = this.seedToSoilMap.getDestination(seed);
    const fertilizer = this.soilToFertilizerMap.getDestination(soil);
    const water = this.fertilizerToWaterMap.getDestination(fertilizer);
    const light = this.waterToLightMap.getDestination(water);
    const temperature = this.lightToTemperatureMap.getDestination(light);
    const humidity = this.temperatureToHumidityMap.getDestination(temperature);
    const location = this.humidityToLocationMap.getDestination(humidity);
    return location;
}

SeedResolver.prototype.getSeedLocations = function() {
    return (this.seeds).map(x => this.getSeedLocation(x));
}

function Pam(input) {
    const split = input.split(" ");
    this.destinationRangeStart = parseInt(split[0]);
    this.sourceRangeStart = parseInt(split[1]);
    this.rangeLength = parseInt(split[2]);
}

Pam.prototype.getDestination = function(num) {
    if (num >= this.sourceRangeStart && num <= this.sourceRangeStart + this.rangeLength) {
        return this.destinationRangeStart + num - this.sourceRangeStart;
    }
    return null;
}

function CompleteMap(input) {
    this.maps = [];
    for (const inp of input) {
        this.maps.push(new Pam(inp));
    }
}

CompleteMap.prototype.getDestination = function(num) {
    for (const map of this.maps) {
        const destination = map.getDestination(num);
        if (destination !== null) return destination;
    }
    return num;
}

const sr = new SeedResolver(input);

const result1 = Math.min(...sr.getSeedLocations());

SeedResolver.prototype.getRanges = function() {
    const result = [];
    for (let i = 0; i < this.seeds.length; i+=2) {
        const seedStart = this.seeds[i];
        const seedLength = this.seeds[i + 1];
        result.push(new Range(seedStart, seedLength));
    }
    return result;
}

function Range(s, l) {
    this.start = s;
    this.length = l;
}

Pam.prototype.getDestinationRanges = function(range) {
    const result = {
        Matching: [],
        NonMatching: []
    };

    const rangeEnd = range.start + range.length;
    const mapEnd = this.sourceRangeStart + this.rangeLength;

    if (range.start < mapEnd && rangeEnd > this.sourceRangeStart) {
        const matchStart = Math.max(range.start, this.sourceRangeStart);
        const matchEnd = Math.min(rangeEnd, mapEnd);

        result.Matching.push(new Range(this.destinationRangeStart + (matchStart - this.sourceRangeStart), matchEnd - matchStart));

        if (range.start < this.sourceRangeStart) {
            result.NonMatching.push(new Range(range.start, this.sourceRangeStart - range.start));
        }

        if (rangeEnd > mapEnd) {
            result.NonMatching.push(new Range(mapEnd, rangeEnd - mapEnd));
        }
    } else {
        result.NonMatching.push(new Range(range.start, range.length));
    }

    return result;
};

CompleteMap.prototype.getDestinationRanges = function(ranges) {
    const result = [];
    let toProcess = ranges;
    for (const map of this.maps) {
        const newToProcess = [];
        for (const toProc of toProcess) {
            const res = map.getDestinationRanges(toProc);
            result.push(...res.Matching);
            newToProcess.push(...res.NonMatching);
        }
        toProcess = newToProcess;
    }
    result.push(...toProcess)
    return result;
}

SeedResolver.prototype.getSeedLocationRanges = function() {
    const ranges = this.getRanges();
    const soil = this.seedToSoilMap.getDestinationRanges(ranges);
    const fertilizer = this.soilToFertilizerMap.getDestinationRanges(soil);
    const water = this.fertilizerToWaterMap.getDestinationRanges(fertilizer);
    const light = this.waterToLightMap.getDestinationRanges(water);
    const temperature = this.lightToTemperatureMap.getDestinationRanges(light);
    const humidity = this.temperatureToHumidityMap.getDestinationRanges(temperature);
    const location = this.humidityToLocationMap.getDestinationRanges(humidity);

    return location;
}

const result2 = Math.min(...sr.getSeedLocationRanges().map(x => x.start));

console.log("First Solution", result1);
console.log("Second Solution", result2);