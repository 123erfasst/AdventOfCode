using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

// A = ROCK
// B = PAPER
// C = SCICCORS

// X = ROCK +1
// Y = PAPER +2
// Z = SCICCORS +3

public static class Day2
{
    public static void Resolve()
    {
        string path = Path.Combine(Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location), @"Inputs\day2.txt");
        List<string> lines = System.IO.File.ReadLines(path).ToList();
        int sumPointsPart1= 0;
        int sumPointsPart2 = 0;
        foreach (string line in lines)
        {
            var turns = line.Split(' ');
            sumPointsPart1 += playPart1(turns);
        }

        foreach (string line in lines)
        {
            var turns = line.Split(' ');
            sumPointsPart2 += playPart2(turns);
        }




        Console.WriteLine("--------< Day 2 >-----------");
        Console.WriteLine($"Result P1 {sumPointsPart1}");
        Console.WriteLine($"Result P2 {sumPointsPart2}");
        Console.WriteLine("---------------------------");
    }

    private static int playPart2(string[] turns)
    {
        switch (turns[1])
        {
            case "X":
                // X= Lose
                switch (turns[0])
                {
                    case "A":
                        return 3 + 0;
                    case "B":
                        return 1 + 0;
                    case "C":
                        return 2 + 0;
                }
                break;
            case "Y":
                // Y= DRAW
                switch (turns[0])
                {
                    case "A":
                        return 1 + 3;
                    case "B":
                        return 2 + 3;
                    case "C":
                        return 3 + 3;
                }
                break;
            case "Z":    
                // Z= WIN
                switch (turns[0])
                {
                    case "A":
                        return 2 + 6;
                    case "B":
                        return 3 + 6;
                    case "C":
                        return 1 + 6;
                }
                break;
        }
        return 0;
    }

    private static int playPart1(string[] turns)
    {
        switch (turns[0])
        {
            case "A":
                switch (turns[1])
                {
                    case "X":
                        return 1+3;
                    case "Y":
                        return 2+6;
                    case "Z":
                        return 3+0;
                }
                break;
            case "B":
                switch (turns[1])
                {
                    case "X":
                        return 1 + 0;
                    case "Y":
                        return 2 + 3;
                    case "Z":
                        return 3 + 6;
                }
                break;
            case "C":
                switch (turns[1])
                {
                    case "X":
                        return 1 + 6;
                    case "Y":
                        return 2 + 0;
                    case "Z":
                        return 3 + 3;
                }
                break;             
        }
        return 0;
    }
}
