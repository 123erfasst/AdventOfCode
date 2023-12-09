namespace AdventOfCode.Day09

open System
open System.IO
open AdvenfOfCode.Helper

module Solution =
    
    let baseDirectory = __SOURCE_DIRECTORY__
    let fileName = "input.txt"
    let fullPath = Path.Combine(baseDirectory, fileName)

    let parseRow (input: string) =
        input.Split " "
        |> Seq.map int

    let removeLast (list: int List) =
        list
        |> List.rev 
        |> List.tail 
        |> List.rev

    let rec getPrevAndNextValue (values: int list) =
        if List.forall (fun x -> x = 0) values
        then (0,0) else
            let differences = 
                List.zip (List.skip 1 values) (removeLast values)
                |> List.map (fun (first, second) -> first - second)
            let (prev, next) = getPrevAndNextValue(differences)
            ((List.head values) - prev, (List.last values) + next)

    let solvePart1 (input: string array) =
        input
        |> Seq.map (parseRow >> Seq.toList >> getPrevAndNextValue)
        |> Seq.map (fun (_, next) -> next)
        |> Seq.sum

    let solvePart2 (input: string array) = 
        input
        |> Seq.map (parseRow >> Seq.toList >> getPrevAndNextValue)
        |> Seq.map (fun (prev, _) -> prev)
        |> Seq.sum

    let testInput = [|
        "0 3 6 9 12 15";
        "1 3 6 10 15 21";
        "10 13 16 21 30 45";
    |]

    let solve () = 
        let input = File.ReadAllLines fullPath
        (solvePart1(input), solvePart2(input))