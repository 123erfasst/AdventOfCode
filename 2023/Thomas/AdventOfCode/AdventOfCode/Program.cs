using AdventOfCode;

Console.WriteLine("Willkommen zum Advent of code 2023");
var selectedDay = RequestDate();
var selectedPart = RequestPart();

ExecuteDay(selectedDay, selectedPart);


string RequestDate()
{
    Console.WriteLine("Bitte gebe den auszuführenden Tag an");
    var day = Console.ReadLine() ?? RequestDate();
    return day;
}

string RequestPart()
{
    Console.WriteLine("Bitte gebe den auszuführenden Teil an");
    var part = Console.ReadLine() ?? RequestDate();
    return part;
}


void ExecuteDay(string day, string part)
{
    switch (day)
    {
        case "1":
            new Day1().Start(part);
            break;
        case "2":
            new Day2().Start(part);
            break;
        case "3":
            new Day3().Start(part);
            break;
        case "4":
            new Day4().Start(part);
            break;
        case "5":
            new Day5().Start(part);
            break;
        case "6":
            new Day6().Start(part);
            break;
        case "7":
            new Day7().Start(part);
            break;
        case "8":
            new Day8().Start(part);
            break;
        default:
            Console.WriteLine("Tag nicht gefunden");
            break;
    }
}