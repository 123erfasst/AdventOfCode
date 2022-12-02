using System;
using System.Linq;

namespace Day1
{
    class Program
    {
        static void Main(string[] args)
        {
            var input = System.IO.File.ReadAllText("C:\\Workspace\\AdventOfCode\\2022\\Christoph\\AdventOfCode\\Day1\\input.txt");
            var elves = input.Split("\r\n\r\n").Select(x => x.Split("\r\n").Select(int.Parse).Sum());

            Console.WriteLine("Calories of Elf: " + elves.Max());
            Console.WriteLine("Calories of 3 Elves: " + elves.OrderByDescending(num => num).Take(3).Sum());
        }
    }
}