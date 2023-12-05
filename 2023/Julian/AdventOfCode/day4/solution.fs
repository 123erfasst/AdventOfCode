namespace AdventOfCode.Day04

open System
open System.IO

module Solution =

    let baseDirectory = __SOURCE_DIRECTORY__
    let fileName = "input.txt"
    let fullPath = Path.Combine(baseDirectory, fileName)

    type Card = {
        winning: int Set
        own: int Set
    }

    let parseLine (line: string) =
        let [|_; numberInfo|] = line.Split ':'
        let [|winningString; ownString|] = numberInfo.Split '|'
        let winning = 
            winningString.Split ' '
            |> Seq.filter (fun x -> x <> "")
            |> Seq.map int
            |> set
        let own = 
            ownString.Split ' '
            |> Seq.filter (fun x -> x <> "")
            |> Seq.map int
            |> set
        {winning = winning; own = own}

    let getAmount (card: Card) =
        let winningNumbers = Set.intersect card.winning card.own
        
        if winningNumbers.Count = 0
        then 0 
        else pown 2 (winningNumbers.Count - 1)

    let solvePart1 (input: string seq) =
        input
        |> Seq.map (parseLine >> getAmount)
        |> Seq.sum

    let foldi fold first source  =
       source 
       |> Seq.fold(fun (prev,i) c -> (fold i prev c,i + 1)) (first,0)
       |> fst

    let foldCard (index: int) (acc: Map<int, int>) (card: Card)  =
        let winningNumbers = Set.intersect card.winning card.own
        let existingCopies = Map.tryFind index acc |> Option.defaultValue 0
        [1..winningNumbers.Count]
        |> Seq.fold (
            fun acc id -> 
                Map.change (index + id) (
                    fun x -> 
                        match x with 
                        | Some s -> Some(s + existingCopies) 
                        | None -> Some(existingCopies)) acc) acc
        
    let solvePart2 (input: string seq) = 
        let initialMap = 
            [0..(Seq.length input)-1] 
            |> Seq.map (fun x -> (x, 1)) 
            |> Map
        input
            |> Seq.map parseLine
            |> foldi foldCard initialMap
            |> Map.values
            |> Seq.sum

    let testInput = [
        "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53";
        "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19";
        "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1";
        "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83";
        "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36";
        "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11";
        ]

    let solve () = 
        let input = File.ReadLines fullPath
        (solvePart1(input), solvePart2(input))