using System;
using System.Linq;

namespace Day2
{
    class Program
    {
        static void Main(string[] args)
        {
            var input = System.IO.File.ReadAllText("C:\\Workspace\\AdventOfCode\\2022\\Christoph\\AdventOfCode\\Day2\\input.txt");

            var part1 = input.Split("\r\n").Select(x => x.Split(" ")).Select(y => CalculatePoints(y[0], y[1])).Sum();
            var part2 = input.Split("\r\n").Select(x => x.Split(" ")).Select(y => CalculatePoints2(y[0], y[1])).Sum();
            
            Console.WriteLine("part1: " + part1 + "  part2: " + part2);
        }

        private static readonly string[] Input1 = { "A", "B", "C" };
        private static readonly string[] Input2 = { "X", "Y", "Z" };
        
        private static int CalculatePoints2(string v1, string v2)
        {
            var winningPoints = Array.IndexOf(Input2, v2) * 3;
            var myPlay = (Array.IndexOf(Input1, v1) + Array.IndexOf(Input2, v2) + 2) % 3;
            
            return winningPoints + myPlay + 1;
        }
        private static int CalculatePoints(string v1, string v2)
        {
            return (Array.IndexOf(Input1, v1) - Array.IndexOf(Input2, v2) + 1) % 3 * 3 + Array.IndexOf(Input2, v2) + 1;
        }
    }
}