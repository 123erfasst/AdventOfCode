namespace AdventOfCode.Day12

open System.IO

module Solution =
    
    let baseDirectory = __SOURCE_DIRECTORY__
    let fileName = "input.txt"
    let fullPath = Path.Combine(baseDirectory, fileName)

    type Spring = 
    | Damaged
    | Operational
    | Unknown

    type Row = {Springs: Spring seq; Result: Spring seq}

    let parseRow (times: int) (row: string)  =
        let [|springsRaw; groupsRaw|] = row.Split ' '
        let springs = 
            springsRaw
            |> Seq.map (fun c -> 
                match c with
                | '#' -> Damaged
                | '.' -> Operational
                | _ -> Unknown
            ) 
            |> Seq.replicate times
            |> Seq.reduce (fun first second -> Seq.concat [first;[Unknown];second])
        let result =
            groupsRaw.Split ','
            |> Seq.replicate times
            |> Seq.collect id
            |> Seq.map (fun c -> List.replicate (int c) Damaged)
            |> Seq.collect (fun x -> x @ [Operational])
            |> Seq.toList
            |> (fun groups -> Operational :: groups)

        {Springs = springs; Result = result}

    let getAllPossibilities (row: Row) =
        let initialState = Map [(0,1L)]
        row.Springs
        |> Seq.fold (fun acc resSpring ->
            Map.fold (fun (acc2: Map<int, int64>) k v -> 
                match resSpring with
                | Unknown -> 
                    let withAdditional = 
                        if k + 1 < (Seq.length row.Result) 
                        then (Map.change (k + 1) (fun x -> Some((Option.defaultValue 0L x) + v)) acc2)
                        else acc2
                    if Seq.item k row.Result = Operational 
                    then (Map.change k (fun x -> Some((Option.defaultValue 0L x) + v)) withAdditional)
                    else withAdditional
                | Operational ->
                    let withAdditional = 
                        if k + 1 < (Seq.length row.Result) && Seq.item (k+1) row.Result = Operational
                        then (Map.change (k + 1) (fun x -> Some((Option.defaultValue 0L x) + v)) acc2)
                        else acc2
                    if Seq.item k row.Result = Operational 
                    then (Map.change k (fun x -> Some((Option.defaultValue 0L x) + v)) withAdditional)
                    else withAdditional
                | Damaged ->
                    if k + 1 < (Seq.length row.Result) && Seq.item (k+1) row.Result = Damaged 
                    then Map.change (k + 1) (fun x -> Some((Option.defaultValue 0L x) + v)) acc2
                    else acc2
            ) Map.empty acc
        ) initialState
        |> (fun res -> 
                let last = 
                    Map.tryFind ((Seq.length row.Result) - 1) res
                    |> Option.defaultValue 0
                let previous = 
                    Map.tryFind ((Seq.length row.Result) - 2) res
                    |> Option.defaultValue 0
                last + previous
        )


    let solvePart1 (input: string array) =
        input
        |> Array.map ((parseRow 1) >> (getAllPossibilities))
        |> Array.sum

    let solvePart2 (input: string array) = 
        input
        |> Array.map ((parseRow 5) >> getAllPossibilities)
        |> Array.sum

    let testInput = [|
        "???.### 1,1,3";
        ".??..??...?##. 1,1,3";
        "?#?#?#?#?#?#?#? 1,3,1,6";
        "????.#...#... 4,1,1";
        "????.######..#####. 1,6,5";
        "?###???????? 3,2,1"
    |]

    let solve () = 
        let input = File.ReadAllLines fullPath
        (solvePart1(input), solvePart2(input))