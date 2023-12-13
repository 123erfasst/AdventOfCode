namespace AdventOfCode.Day11

open System.IO

module Solution =
    
    let baseDirectory = __SOURCE_DIRECTORY__
    let fileName = "input.txt"
    let fullPath = Path.Combine(baseDirectory, fileName)

    type Field =
        | Galaxy
        | Space

    let parseInput (input: string array) = 
        input
        |> array2D
        |> Array2D.map (fun c -> 
            match c with
            | '#' -> Galaxy
            | _ -> Space)

    let isEmptyRow y (galaxies: Field[,]) =
        galaxies[y,*]
        |> Array.forall (fun f -> f = Space)

    let isEmptyColumn x (galaxies: Field[,]) =
        galaxies[*,x]
        |> Array.forall (fun f -> f = Space)

    let calculateCostField (expansion: int64) (galaxyMap: Field[,]) =
        galaxyMap
        |> Array2D.mapi (fun y x _ -> 
            let rowCost = if isEmptyRow y galaxyMap then expansion else 1
            let colCost = if isEmptyColumn x galaxyMap then expansion else 1
            rowCost * colCost)

    let getPathCost (rowStart, colStart) (rowEnd, colEnd) (costMap: int64[,]) =
        let rowRange = 
            [ (min rowStart rowEnd) + 1 .. max rowStart rowEnd ] 
            |> List.map (fun row -> costMap[row, colStart])
        let colRange = 
            [ (min colStart colEnd) + 1 .. max colStart colEnd ] 
            |> List.map (fun col -> costMap[rowStart, col])
        List.sum rowRange + List.sum colRange

    let getGalaxyCoordinates (galaxyMap: Field[,]) =
        let xLength = Array2D.length1 galaxyMap
        let yLength = Array2D.length2 galaxyMap

        [0..(xLength - 1)]
        |> List.collect (fun x ->
            [0..(yLength - 1)]
            |> List.choose (fun y -> 
                if galaxyMap[x,y] = Galaxy 
                then Some((x,y)) 
                else None)
        )

    let getAllPairs (galaxies: (int * int) list) =
        galaxies
        |> List.mapi (fun galaxyIndex galaxy -> 
            List.skip (galaxyIndex + 1) galaxies
            |> List.map (fun pair -> (galaxy, pair))
        ) |> List.collect id

    let getAllCosts (expansion: int64) (galaxyMap: Field[,]) =
        let costMap = calculateCostField expansion galaxyMap
        getGalaxyCoordinates galaxyMap
        |> getAllPairs
        |> Seq.sumBy (fun (pair1, pair2) -> getPathCost pair1 pair2 costMap)


    let solvePart1 (input: string array) =
        input
        |> parseInput
        |> getAllCosts 2L

    let solvePart2 (input: string array) = 
        input
        |> parseInput
        |> getAllCosts 1000000L

    let testInput = [|
        "...#......";
        ".......#..";
        "#.........";
        "..........";
        "......#...";
        ".#........";
        ".........#";
        "..........";
        ".......#..";
        "#...#.....";
    |]

    let solve () = 
        let input = File.ReadAllLines fullPath
        (solvePart1(input), solvePart2(input))