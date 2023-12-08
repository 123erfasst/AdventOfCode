namespace AdventOfCode.Day07

open System
open System.IO

module Solution =

    let baseDirectory = __SOURCE_DIRECTORY__
    let fileName = "input.txt"
    let fullPath = Path.Combine(baseDirectory, fileName)

    type Card = WildCard | Two | Three | Four | Five | Six | Seven | Eight | Nine | Ten | Jack | Queen | King | Ace
    type HandValue = HighCard | Pair | TwoPair | ThreeOfAKind | FullHouse | FourOfAKind | FiveOfAKind

    let getHandValue cards =
        let wildCardCount = 
            cards 
            |> List.filter (fun card -> card = WildCard)
            |> List.length
        let groupedCards = 
            cards
            |> List.filter (fun card -> card <> WildCard)
            |> List.countBy id
            |> List.sortByDescending (fun (_, count) -> count)

        let groupedWithWildCards = 
            match groupedCards with
            | (card, count) :: tail -> (card, count + wildCardCount) :: tail
            | _ -> [WildCard, 5]

        match groupedWithWildCards with
        | [(_, 5)] -> FiveOfAKind
        | [(_,4);(_,1)] -> FourOfAKind
        | [(_,3);(_,2)] -> FullHouse
        | [(_,3);(_,1);(_,1)] -> ThreeOfAKind
        | [(_,2);(_,2);(_,1)] -> TwoPair
        | [(_,2);(_,1);(_,1);(_,1)] -> Pair
        | _ -> HighCard

    [<CustomComparison; CustomEquality>]
    type Hand = 
        {Cards: Card list; Bid: int}
        static member compare first second =
            let firstValue = getHandValue first.Cards
            let secondValue = getHandValue second.Cards
            
            if firstValue = secondValue
            then compare first.Cards second.Cards
            else compare firstValue secondValue

        interface IComparable with
            member this.CompareTo other =
                match other with
                | :? Hand as hand -> Hand.compare this hand
                | _ -> invalidArg "other" "Different Type"

        override this.Equals other =
            match other with
            | :? Hand as hand -> Hand.compare this hand = 0
            | _ -> false

        override this.GetHashCode() = hash this

    let parseCard (char: Char) =
        match char with
        | '2' -> Two
        | '3' -> Three
        | '4' -> Four
        | '5' -> Five
        | '6' -> Six
        | '7' -> Seven
        | '8' -> Eight
        | '9' -> Nine
        | 'T' -> Ten
        | 'J' -> Jack
        | 'Q' -> Queen
        | 'K' -> King
        | 'A' -> Ace
        | _ -> failwith "wrong"

    let parseHand (parseCharToCard: Char -> Card) (input: string) =
        let [|cardsString; bidString|] = input.Split " "
        let cards = 
            cardsString
            |> Seq.toList
            |> List.map parseCharToCard
        let bid = int bidString

        {Cards = cards; Bid = bid}

    let solvePart1 (input: string array) =
        input 
        |> Array.map (parseHand parseCard)
        |> Array.sort
        |> Array.indexed
        |> Array.fold (fun acc (index, hand) -> acc + (hand.Bid * (index + 1))) 0

    let parseCard2 (char: Char) =
        match char with
        | '2' -> Two
        | '3' -> Three
        | '4' -> Four
        | '5' -> Five
        | '6' -> Six
        | '7' -> Seven
        | '8' -> Eight
        | '9' -> Nine
        | 'T' -> Ten
        | 'J' -> WildCard
        | 'Q' -> Queen
        | 'K' -> King
        | 'A' -> Ace
        | _ -> failwith "wrong"

    let solvePart2 (input: string array) = 
        input 
        |> Array.map (parseHand parseCard2)
        |> Array.sort
        |> Array.indexed
        |> Array.fold (fun acc (index, hand) -> acc + (hand.Bid * (index + 1))) 0

    let testInput = [|
        "32T3K 765";
        "T55J5 684";
        "KK677 28";
        "KTJJT 220";
        "QQQJA 483";
    |]

    let solve () = 
        let input = File.ReadAllLines fullPath
        (solvePart1(input), solvePart2(input))