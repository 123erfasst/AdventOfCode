namespace AdventOfCode.Day01

open System
open System.IO

module Solution =

    let baseDirectory = __SOURCE_DIRECTORY__
    let fileName = "input.txt"
    let fullPath = Path.Combine(baseDirectory, fileName)

    let solvePart1 (input: string seq) =
        input
        |> Seq.map (fun x -> 
            (String.filter Char.IsDigit x) 
            |> (fun s -> String [|(Seq.head s);(Seq.last s)|] )
            |> int)
        |> Seq.sum

    type Acc = {
        foundNumbers: string
        stringAcc: string
    }

    let initial = {foundNumbers = ""; stringAcc = ""}

    let (|Suffix|_|) (p:string) (s:string) =
        if s.EndsWith(p) then
            Some(s)
        else
            None

    let (|Dig|_|) str =
        match str with
            | Suffix "one" _ -> Some("1")
            | Suffix "two" _ -> Some("2")
            | Suffix "three" _ -> Some("3")
            | Suffix "four" _ -> Some("4")
            | Suffix "five" _ -> Some("5")
            | Suffix "six" _ -> Some("6")
            | Suffix "seven" _ -> Some("7")
            | Suffix "eight" _ -> Some("8")
            | Suffix "nine" _ -> Some("9")
            | _ -> None

    let replaceSpelledDigitsFun = fun (acc: Acc) (c: char)  ->
        if Char.IsDigit c 
        then {foundNumbers = (acc.foundNumbers + c.ToString()); stringAcc = ""} 
        else match (acc.stringAcc + c.ToString()) with
                | Dig d -> {foundNumbers = (acc.foundNumbers + d); stringAcc = acc.stringAcc + c.ToString()}
                | _ -> {acc with stringAcc = acc.stringAcc + c.ToString()}

    let replaceSpelledDigits = fun (c: string) -> 
        (Seq.fold replaceSpelledDigitsFun initial c) |> (fun x -> x.foundNumbers)

    let solvePart2 (input: string seq) = 
        input
        |> Seq.map (fun x -> 
            (replaceSpelledDigits x)
            |> String.filter Char.IsDigit 
            |> (fun s -> String [|(Seq.head s);(Seq.last s)|] )
            |> int)
        |> Seq.sum

    let solve () = 
        let input = File.ReadLines fullPath
        (solvePart1(input),solvePart2(input))