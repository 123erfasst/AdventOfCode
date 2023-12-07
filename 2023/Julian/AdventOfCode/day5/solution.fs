namespace AdventOfCode.Day05

open System
open System.IO

module Solution =

    let baseDirectory = __SOURCE_DIRECTORY__
    let fileName = "input.txt"
    let fullPath = Path.Combine(baseDirectory, fileName)
    let getSeeds (str: string) =
        str.Replace ("seeds: ", "") 
        |> (fun row -> row.Split " ") 
        |> Seq.map int64

    type DestSourceMapping = {destStart: int64; destEnd: int64; sourceStart: int64; sourceEnd: int64}

    let tryFindFromMap (key: int64) (mapping: DestSourceMapping) =
        if key >= mapping.sourceStart && key <= mapping.sourceEnd 
            then Some(key - mapping.sourceStart + mapping.destStart)
            else None

    let parseMapLine (str: string) =
        let [|dest; source; len|] = 
            str.Split " "
            |> Array.map int64

        {destStart = dest; destEnd = dest + len - 1L; sourceStart = source; sourceEnd = source + len - 1L}

    let parseMap (str: string) =
        str.Split Environment.NewLine
            |> Seq.skip 1
            |> Seq.map parseMapLine

    let getMap (str: string) (key: int64) =
        parseMap str
        |> Seq.tryPick (tryFindFromMap key)
        |> Option.defaultValue key

    let getMapping (maps: string array)  =
        let [| 
            seedToSoil; 
            soilToFertilizer; 
            fertiliszerToWater; 
            waterToLight; 
            lightToTemperature;
            temperatureToHumidity;
            humidityToLocation|] = maps
        let findSoil = getMap seedToSoil
        let findFertilizer = getMap soilToFertilizer
        let findWater = getMap fertiliszerToWater
        let findLight = getMap waterToLight
        let findTemperature = getMap lightToTemperature
        let findHumidity = getMap temperatureToHumidity
        let findLocation = getMap humidityToLocation

        let findLocationFromSeed =
            findSoil >> findFertilizer >> findWater >> findLight >> findTemperature >> findHumidity >> findLocation

        Seq.map findLocationFromSeed >> Seq.min

    let solvePart1 (input: string) =
        let splittedInput = 
            input.Split (Environment.NewLine + Environment.NewLine)
        getSeeds (Array.head splittedInput)
        |> getMapping (Array.tail splittedInput)
    
    type SeedRange = {start: int64; stop: int64}

    let getSeedRanges (str: string) =
        str.Replace ("seeds: ", "") 
            |> (fun row -> row.Split " ") 
            |> Array.map int64
            |> Array.chunkBySize 2
            |> Array.map (fun [|source; len|] -> {start = source; stop = source +  len - 1L})
    

    type MappingState = {mapped: SeedRange seq; notmapped: SeedRange seq}

    let splitSeedRange (mapping: DestSourceMapping) (seedRange: SeedRange) =
        if seedRange.start <= mapping.sourceEnd && seedRange.stop >= mapping.sourceStart
        then 
            let prev = 
                if seedRange.start < mapping.sourceStart 
                then [{seedRange with stop = mapping.sourceStart - 1L}] 
                else []
            let after = 
                if seedRange.stop > mapping.sourceEnd 
                then [{seedRange with start = mapping.sourceEnd + 1L}] 
                else []
            let mappedStart = Math.Max (mapping.sourceStart, seedRange.start)
            let mappedEnd = Math.Min (mapping.sourceEnd, seedRange.stop)
            let newMapped = {start = mapping.destStart + (mappedStart - mapping.sourceStart); stop = mapping.destStart + (mappedEnd - mapping.sourceStart)}
            {notmapped = prev @ after; mapped = [newMapped] }
        else {notmapped = [seedRange]; mapped = []}

    let getMap2 (str: string) (seedRange: SeedRange seq) =
        parseMap str
        |> Seq.fold (fun acc mapping -> 
            let newMappingState = Seq.map (splitSeedRange mapping)  acc.notmapped
            let mapped = Seq.collect (fun x -> x.mapped) newMappingState
            let notmapped = Seq.collect (fun x -> x.notmapped) newMappingState
            {mapped = Seq.concat [acc.mapped; mapped]; notmapped = notmapped}
            ) {mapped= []; notmapped = seedRange}
        |> (fun state -> 
                Seq.concat [state.mapped; state.notmapped]
            )    

    let getMapping2 (maps: string array) (seeds: SeedRange seq)  =
        printfn "Seeds: %A" (Seq.toList seeds)
        let [| 
            seedToSoil; 
            soilToFertilizer; 
            fertiliszerToWater; 
            waterToLight; 
            lightToTemperature;
            temperatureToHumidity;
            humidityToLocation|] = maps
        let findSoil = getMap2 seedToSoil
        let findFertilizer = getMap2 soilToFertilizer
        let findWater = getMap2 fertiliszerToWater
        let findLight = getMap2 waterToLight
        let findTemperature = getMap2 lightToTemperature
        let findHumidity = getMap2 temperatureToHumidity
        let findLocation = getMap2 humidityToLocation

        let findLocationFromSeed =
            findSoil 
            >> findFertilizer 
            >> findWater 
            >> findLight 
            >> findTemperature 
            >> findHumidity 
            >> findLocation

        let locations = 
            findLocationFromSeed seeds

        locations 
        |> Seq.map (fun x -> x.start)
        |> Seq.min


    let solvePart2 (input: string) = 
        let splittedInput = 
            input.Split (Environment.NewLine + Environment.NewLine)
        let seeds = getSeedRanges (Array.head splittedInput)
        getMapping2 (Array.tail splittedInput) seeds

    let testInput () =
        File.ReadAllText (Path.Combine(baseDirectory, "testInput.txt"))

    let solve () = 
        let input = File.ReadAllText fullPath
        (solvePart1(input), solvePart2(input))