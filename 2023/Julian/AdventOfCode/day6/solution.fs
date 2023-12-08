namespace AdventOfCode.Day06

open System
open System.IO

module Solution =

    let baseDirectory = __SOURCE_DIRECTORY__
    let fileName = "input.txt"
    let fullPath = Path.Combine(baseDirectory, fileName)

    type Race = {Time: double; Distance: double}

    let parseInput (input: string array) =
        let [|timesRaw; distancesRaw|] = input

        let times = 
            timesRaw.Replace ("Time: ", "")
            |> (fun x -> x.Split " ")
            |> Seq.filter (String.IsNullOrWhiteSpace >> not)
            |> Seq.map int

        let distances =
            distancesRaw.Replace ("Distance: ", "")
            |> (fun x -> x.Split " ")
            |> Seq.filter (String.IsNullOrWhiteSpace >> not)
            |> Seq.map int

        Seq.zip times distances
            |> Seq.map ( fun (time, distance) -> {Time = time; Distance = distance})

    let calculateDistance (maxTime: int64) (holdingTime: int64) =
        (maxTime - holdingTime) * (holdingTime * 1L)

    let getPossibleRaces (race: Race) =
        let root = sqrt ((race.Time * race.Time) - (4.0 * race.Distance))
        let first = 
            floor ((race.Time - root) / 2.0 + 1.0)
            |> int
        let last =
            ceil ((race.Time + root) / 2.0 - 1.0)
            |> int

        last - first + 1

    let solvePart1 (input: string array) =
        parseInput input
        |> Seq.map (fun race -> getPossibleRaces race)
        |> Seq.reduce (*)

    let parseInput2 (input: string array) =
        let [|timesRaw; distancesRaw|] = input

        let time = 
            timesRaw.Replace ("Time: ", "")
            |> (fun x -> x.Replace (" ", ""))
            |> double

        let distance =
            distancesRaw.Replace ("Distance: ", "")
            |> (fun x -> x.Replace (" ", ""))
            |> double

        {Time = time; Distance = distance}
    let solvePart2 (input: string array) = 
        parseInput2 input
        |> getPossibleRaces

    let testInput = [|
        "Time:      7  15   30";
        "Distance:  9  40  200"
    |]

    let solve () = 
        let input = File.ReadAllLines fullPath
        (solvePart1(input), solvePart2(input))