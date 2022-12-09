using System;
using System.Linq;

namespace Day4
{
    class Program
    {
        static void Main(string[] args)
        {
            var input = System.IO.File.ReadAllLines("C:\\Workspace\\AdventOfCode\\2022\\Christoph\\AdventOfCode\\Day4\\input.txt");
            var stuff = input.Select(x => x.Split(",").Select(y => y.Split("-").Select(int.Parse).ToArray()).ToArray())
                .Count(x => (x[0][0] <= x[1][0] && x[0][1] >= x[1][1]) || (x[0][0] >= x[1][0] && x[0][1] <= x[1][1]));
            var stuff2 = input
                .Select(x => x.Split(",")
                    .Select(y => y.Split("-")
                        .Select(int.Parse)
                        .ToArray())
                    .ToArray())
                .Count(x1 => (x1[0][0] >= x1[1][0] && x1[0][0] <= x1[1][1]) || (x1[0][1] >= x1[1][0] && x1[0][1] <= x1[1][1]) || 
                             (x1[1][0] >= x1[0][0] && x1[1][0] <= x1[0][1]) || (x1[1][1] >= x1[0][0] && x1[1][1] <= x1[0][1]));

            Console.WriteLine(stuff);
            Console.WriteLine(stuff2);

        }
    }
}