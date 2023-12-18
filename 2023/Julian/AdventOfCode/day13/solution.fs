namespace AdventOfCode.Day13

open System
open System.IO

module Solution =
    
    let baseDirectory = __SOURCE_DIRECTORY__
    let fileName = "input.txt"
    let fullPath = Path.Combine(baseDirectory, fileName)

    let getDifferences (a: char seq) (b: char seq) =
        Seq.zip a b
        |> Seq.filter (fun (x, y) -> x <> y)
        |> Seq.length

    let isSymmetric index smudges field =
        let rec isSymmetricInner (lowIndex: int) (smudges: int) (diff: int) =
            let highIndex = lowIndex + diff
            if lowIndex = Array.length field - 1
            then false
            elif field[lowIndex] = field[highIndex]
            then 
                if lowIndex = 0 || highIndex = Array.length field - 1
                then smudges = 0
                else isSymmetricInner (lowIndex - 1) smudges (diff + 2)
            elif getDifferences field[lowIndex] field[highIndex] <= smudges
            then
                let differences = getDifferences field[lowIndex] field[highIndex]
                if lowIndex = 0 || highIndex = Array.length field - 1
                then (smudges - differences) = 0
                else isSymmetricInner (lowIndex - 1) (smudges - differences) (diff + 2)
            else false
        isSymmetricInner index smudges 1

    let findMirrored smudges field =
        field
            |> Array.indexed
            |> Array.tryFindIndex (fun (index, _) -> isSymmetric index smudges field)
            |> Option.map ((+)1)

    let findMirroredIndex smudges field  =
        let rowIndex = 
            findMirrored smudges field
            |> Option.map ((*) 100)
        let columnIndex = 
            field
            |> Array.map Seq.toArray
            |> Array.transpose
            |> findMirrored smudges

        Option.orElse rowIndex columnIndex
        |> Option.defaultValue 0

    let solvePart1 (input: string) =
        input.Split (Environment.NewLine + Environment.NewLine)
        |> Array.map (fun x -> x.Split Environment.NewLine |> findMirroredIndex 0)
        |> Array.sum

    let solvePart2 (input: string) = 
        input.Split (Environment.NewLine + Environment.NewLine)
        |> Array.map (fun x -> x.Split Environment.NewLine |> findMirroredIndex 1)
        |> Array.sum

    let testInput () =
        File.ReadAllText (Path.Combine(baseDirectory, "testInput.txt"))

    let solve () = 
        let input = File.ReadAllText fullPath
        (solvePart1 input, solvePart2 input)