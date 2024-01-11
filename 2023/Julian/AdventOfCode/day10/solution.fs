namespace AdventOfCode.Day10

open System
open System.IO

module Solution =
    
    let baseDirectory = __SOURCE_DIRECTORY__
    let fileName = "input.txt"
    let fullPath = Path.Combine(baseDirectory, fileName)

    type Tile = 
        | Start
        | Ground
        | NorthAndSouth
        | EastAndWest
        | NorthAndEast
        | NorthAndWest
        | SouthAndWest
        | SouthAndEast

    let parseTile c = 
        match c with
        | '|' -> NorthAndSouth
        | '-' -> EastAndWest
        | 'L' -> NorthAndEast
        | 'J' -> NorthAndWest
        | '7' -> SouthAndWest
        | 'F' -> SouthAndEast
        | 'S' -> Start
        | _ -> Ground

    type PipeMap = {StartPosition: int * int; Pipes: Map<int * int, Tile>}

    let parseInput (str: string array) =
        let map = 
            str
            |> Seq.mapi (fun y row -> 
                Seq.mapi (fun x cell -> ((x,y), parseTile cell)) row)
            |> Seq.collect id
            |> Map
        let initialPosition = Map.findKey (fun _ v -> v = Start) map

        {StartPosition = initialPosition; Pipes = map}

    let (|HasSouth|_|) tile =
        match tile with
        | NorthAndSouth
        | SouthAndEast
        | SouthAndEast -> Some()
        | _ -> None

    let (|HasNorth|_|) tile =
        match tile with
        | NorthAndSouth
        | NorthAndWest
        | NorthAndEast -> Some()
        | _ -> None

    let (|HasEast|_|) tile =
        match tile with
        | EastAndWest
        | SouthAndEast
        | NorthAndEast -> Some()
        | _ -> None

    let (|HasWest|_|) =
        function
        | EastAndWest
        | SouthAndWest
        | NorthAndWest -> Some()
        | _ -> None
         
    let findStartTile startCoords (map: Map<(int * int), Tile>) = 
        let (x, y) = startCoords
        let surroundingTiles = 
            [
                (x, y - 1);
                (x + 1, y);
                (x, y + 1);
                (x - 1, y)
            ] 
            |> List.map (fun k -> Map.tryFind k map |> Option.defaultValue Ground)
        match surroundingTiles with
        | [ HasSouth; _; HasNorth; _ ] -> NorthAndSouth
        | [ _; HasWest; _; HasEast ] -> EastAndWest
        | [ HasSouth; HasWest; _; _ ] -> NorthAndEast
        | [ HasSouth; _; _; HasEast ] -> NorthAndWest
        | [ _; _; HasNorth; HasEast ] -> SouthAndWest
        | [ _; HasWest; HasNorth; _ ] -> SouthAndEast
        | _ -> failwith "Invalid"

    let findTile (coord: int * int) (pipes: Map<int * int, Tile>) =
        let tile = Map.tryFind coord pipes |> Option.defaultValue Ground
        match tile with
        | Start -> findStartTile coord pipes
        | other -> other

    let findNext (curr: int * int) (prev: int * int) (pipes: Map<int * int, Tile>) =
        let (currentX, currentY) = curr
        let tile = findTile curr pipes
        let possibleCoords = 
            match tile with
            | NorthAndSouth -> [(currentX, currentY - 1); (currentX, currentY + 1)]
            | EastAndWest -> [(currentX + 1, currentY); (currentX - 1, currentY)]
            | NorthAndEast -> [(currentX + 1, currentY); (currentX, currentY - 1)]
            | NorthAndWest -> [(currentX - 1, currentY); (currentX, currentY - 1)]
            | SouthAndEast -> [(currentX + 1, currentY); (currentX, currentY + 1)]
            | SouthAndWest -> [(currentX - 1, currentY); (currentX, currentY + 1)]
            | _ -> failwith "Invalid"

        possibleCoords
        |> List.find (fun p -> p <> prev)

    let findCircle (pipeMap: PipeMap) =
        let rec findCircleInner (curr: int * int) (prev: int * int) (acc: (int * int) list)  =
            let nextPosition = findNext curr prev pipeMap.Pipes
            let nextTile = Map.find nextPosition pipeMap.Pipes
            
            if nextTile = Start 
            then curr :: acc 
            else findCircleInner nextPosition curr (curr :: acc)
        findCircleInner (findNext pipeMap.StartPosition pipeMap.StartPosition pipeMap.Pipes) pipeMap.StartPosition [pipeMap.StartPosition]

    let solvePart1 (input: string array) =
        parseInput input
        |> findCircle
        |> List.length
        |> (fun x -> x / 2)

    let determinant (aX, aY) (bX, bY) = 
        aX * bY - aY * bX

    let solvePart2 (input: string array) = 
        let pipeMap = parseInput input
        let (steps, area, _) = 
            findCircle pipeMap
            |> List.fold (
                fun (steps, area, corner) newPos ->
                    let currentTile = Map.find newPos pipeMap.Pipes

                    match currentTile with
                    | NorthAndSouth -> (steps + 1, area, corner)
                    | EastAndWest -> (steps + 1, area, corner)
                    | _ -> (steps + 1, area + determinant corner newPos, newPos)
                    ) (0, 0, pipeMap.StartPosition)

        Math.Abs(area) / 2 - steps / 2 + 1

    let testInput1 = [|
        "-L|F7";
        "7S-7|";
        "L|7||";
        "-L-J|";
        "L|-JF";
    |]

    let testInput2 = [|
        "..F7.";
        ".FJ|.";
        "SJ.L7";
        "|F--J";
        "LJ...";
    |]

    let testInputPart2 = [|
        "...........";
        ".S-------7.";
        ".|F-----7|.";
        ".||.....||.";
        ".||.....||.";
        ".|L-7.F-J|.";
        ".|..|.|..|.";
        ".L--J.L--J.";
        "...........";
    |]

    let solve () = 
        let input = File.ReadAllLines fullPath
        (solvePart1(input), solvePart2(input))