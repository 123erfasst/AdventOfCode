namespace AdventOfCode.Day02

open System
open System.IO

module Solution =

    let baseDirectory = __SOURCE_DIRECTORY__
    let fileName = "input.txt"
    let fullPath = Path.Combine(baseDirectory, fileName)

    type Round = {
        Blue: int
        Red: int
        Green: int
    }

    let initialRound = {
        Blue = 0;
        Red = 0;
        Green = 0;
    }

    type Game = {
        ID: int
        Rounds: Round seq
    }

    let (|Suffix|_|) (p:string) (s:string) =
        if s.EndsWith(p) then
            Some(s.Substring(0, s.Length - p.Length))
        else
            None

    let parseGame (line: string) =
         let [|gamePart; bags|] = line.Split(':')
         let [|_; gameId|] = gamePart.Split(' ')
         let rounds = 
            bags.Split(';')
            |> Seq.map (fun round -> 
                round.Split(',') 
                |> Seq.fold (fun acc line -> 
                    match line with
                    | Suffix "blue" amount -> {acc with Blue = amount.Trim() |> int}
                    | Suffix "red" amount -> {acc with Red = amount.Trim() |> int}
                    | Suffix "green" amount -> {acc with Green = amount.Trim() |> int}
                    | _ -> acc
                    ) initialRound)
         {ID= gameId |> int; Rounds = rounds}

    let testInput = [ "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green";
"Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue";
"Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red";
"Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red";
"Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"]

    let maximum = {
        Blue = 14;
        Green = 13;
        Red = 12;
    }

    let solvePart1 (input: string seq) =
        input
        |> Seq.map parseGame
        |> Seq.filter (fun game -> 
            game.Rounds 
            |> Seq.forall (fun item -> 
                item.Blue <= maximum.Blue 
                && item.Red <= maximum.Red 
                && item.Green <= maximum.Green))
        |> Seq.map (fun game -> game.ID)
        |> Seq.sum

    let getMaxCount (getColor: Round -> int) (game: Game) =
        game.Rounds 
            |> Seq.map getColor 
            |> Seq.max

    let calculatePower (game: Game) =
        let maxRed = getMaxCount (fun round -> round.Red) game
        let maxBlue = getMaxCount (fun round -> round.Blue) game
        let maxGreen = getMaxCount (fun round -> round.Green) game

        maxRed * maxBlue * maxGreen

    let solvePart2 (input: string seq) = 
        input
            |> Seq.map (parseGame >> calculatePower)
            |> Seq.sum

    let solve () = 
        let input = File.ReadLines fullPath
        (solvePart1(input), solvePart2(input))