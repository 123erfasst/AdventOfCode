namespace AdventOfCode.Day08

open System
open System.IO
open AdvenfOfCode.Helper

module Solution =
    
    let baseDirectory = __SOURCE_DIRECTORY__
    let fileName = "input.txt"
    let fullPath = Path.Combine(baseDirectory, fileName)

    type LRMap = {LeftNode: string; RightNode: string}

    let parseMap (input: string) =
        let [|key; mapString|] = input.Split " = "
        let [|left; right|] = 
            mapString.Replace("(", "")
            |> (fun s -> s.Replace(")", ""))
            |> (fun s -> s.Split ", " )
        (key, {LeftNode = left; RightNode = right})

    type Direction = Left | Right

    let parseDirection (input: string) = 
        input
        |> Seq.toList
        |> Seq.map (fun c -> if c = 'R' then Right else Left)
        |> CircularCollection.init

    let parseInput (input: string array) =
        let directions = parseDirection (Array.head input)
        let maps =
            Seq.tail input
            |> Seq.filter (String.IsNullOrEmpty >> not)
            |> Seq.map parseMap
            |> Map
        (directions, maps)

    let goStep (map: Map<string, LRMap>) (direction: Direction) (key: string) =
        let lrMap = map[key]
        match direction with
        | Left -> lrMap.LeftNode
        | Right -> lrMap.RightNode

    let walkPath (endCondition: string -> bool) (directions: Direction array * int * int) (map: Map<string, LRMap>) (start: string) =
        let rec walkPathInner (directions: Direction array * int * int) (current: string) (steps: int64) =
            let direction = CircularCollection.item directions
            let newNode = goStep map direction current
            if(endCondition newNode) 
            then steps + 1L 
            else walkPathInner (CircularCollection.moveNext directions) newNode (steps + 1L)
        walkPathInner directions start 0L

    let solvePart1 (input: string array) =
        let (directions, maps) = parseInput input
        walkPath (fun node -> node = "ZZZ") directions  maps "AAA"


    let rec gcd (a: int64) (b: int64) =
        if b = 0L then a
        elif a = 0L then b
        else gcd b (a % b)

    let lcm (a: int64) (b: int64) = a * b / (gcd a b)

    let solvePart2 (input: string array) = 
        let (directions, map) = parseInput input
        let startNodes = 
            Map.keys map 
            |> Seq.filter (fun key -> key[2] = 'A') 
            |> Seq.toList
        
        startNodes
        |> List.map (fun start -> walkPath (fun node -> node[2] = 'Z') directions map start)
        |> List.reduce lcm

    let testInput1 = [|
        "RL";
        "";
        "AAA = (BBB, CCC)";
        "BBB = (DDD, EEE)";
        "CCC = (ZZZ, GGG)";
        "DDD = (DDD, DDD)";
        "EEE = (EEE, EEE)";
        "GGG = (GGG, GGG)";
        "ZZZ = (ZZZ, ZZZ)";
    |]

    let testInput2 = [|
        "LLR";
        "";
        "AAA = (BBB, BBB)";
        "BBB = (AAA, ZZZ)";
        "ZZZ = (ZZZ, ZZZ)";
    |]

    let solve () = 
        let input = File.ReadAllLines fullPath
        (solvePart1(input), solvePart2(input))