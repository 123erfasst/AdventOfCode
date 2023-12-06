namespace AdventOfCode.Day03

open System
open System.IO

module Solution =

    let baseDirectory = __SOURCE_DIRECTORY__
    let fileName = "input.txt"
    let fullPath = Path.Combine(baseDirectory, fileName)

    type Coord = {X: int; Y: int}

    type Number = {coords: Coord; value: int}
    type SpecialCharacter = {coords: Coord; value: char}

    type ParserState =
        | Whitespace
        | Number of Number

    type Parser = {state: ParserState; values: Number list; specialCharacters: SpecialCharacter list}

    let initialParser = {state = Whitespace; values = []; specialCharacters = []}

    let runParser (y: int) (x: int) (parser: Parser) (c: char) =
        if c = '.' 
        then match parser.state with
                    | Whitespace -> parser
                    | Number n -> {parser with state = Whitespace; values = parser.values @ [n]}
        elif Char.IsDigit c
            then match parser.state with
                    | Whitespace -> {parser with state = Number {coords = {X = x; Y = y}; value = c.ToString() |> int}}
                    | Number n -> {parser with state = Number {n with value = n.value * 10 + (c.ToString() |> int)}}
        else match parser.state with
                    | Whitespace -> {parser with specialCharacters = parser.specialCharacters @ [{coords = {X = x; Y = y}; value = c}] }
                    | Number n -> {parser with state = Whitespace; values = parser.values @ [n]; specialCharacters = parser.specialCharacters @ [{coords = {X = x; Y = y}; value = c}] }

    let foldi fold first source  =
       source 
       |> Seq.fold(fun (prev,i) c -> (fold i prev c,i + 1)) (first,0)
       |> fst

    type Board = {
        specialCharacters: SpecialCharacter list
        values: Number list
    }

    let parseLine (y: int) (str: string) =
        foldi (runParser y) initialParser (str + ".")
        |> (fun line -> {specialCharacters = line.specialCharacters; values = line.values})

    let parseBoard (input: string seq) =
        input
        |> Seq.mapi parseLine
        |> Seq.reduce (fun acc curr -> {specialCharacters = acc.specialCharacters @ curr.specialCharacters; values = acc.values @ curr.values})

    let getAdjacentCells (coords: Coord) (length: int) =
        let leftX = coords.X - 1
        let rightX = coords.X + length
        let leftCell = {coords with X = leftX}
        let rightCell = {coords with X = rightX}
        let topCells = [leftX..rightX] |> List.map (fun x -> {Y = coords.Y - 1; X = x})
        let bottomCells = [leftX..rightX] |> List.map (fun x -> {Y = coords.Y + 1; X = x})
        [leftCell; rightCell] @ topCells @ bottomCells

    let isValidPart (coords: Coord Set) (num: Number) =
        let adjCells = getAdjacentCells num.coords num.value
        adjCells
        |> List.exists (fun cell -> Set.contains cell coords)

    let solvePart1 (input: string seq) =
        let board = input |> parseBoard
        let specialCharacterSet = 
            board.specialCharacters 
                |> List.map (fun x -> x.coords) 
                |> set
        let isValidOnBoard = isValidPart specialCharacterSet
        board.values 
        |> List.filter isValidOnBoard
        |> List.map (fun x -> x.value)
        |> List.sum

    let getGearRatio (values: Number list) (c: SpecialCharacter) =
        let adjacentValues = 
            values 
            |> List.filter (fun num ->
                let adjCells = 
                    (getAdjacentCells num.coords (num.value.ToString().Length)) 
                    |> set
                Set.contains c.coords adjCells
            )
            |> List.map (fun x -> x.value)
        if adjacentValues.Length = 2 then List.reduce (*) adjacentValues else 0
        
    let solvePart2 (input: string seq) = 
        let board = input |> parseBoard
        let getGearRatioForBoard = getGearRatio board.values
        board.specialCharacters
            |> List.filter (fun x -> x.value = '*')
            |> List.map getGearRatioForBoard
            |> List.sum

    let testInput = [
        "467..114..";
        "...*......";
        "..35..633.";
        "......#...";
        "617*......";
        ".....+.58.";
        "..592.....";
        "......755.";
        "...$.*....";
        ".664.598..";
        ]

    let solve () = 
        let input = File.ReadLines fullPath
        (solvePart1(input), solvePart2(input))