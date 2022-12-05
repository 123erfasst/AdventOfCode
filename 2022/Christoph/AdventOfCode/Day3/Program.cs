using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Threading.Tasks;

namespace Day3
{
    class Program
    {
        static void Main(string[] args)
        {
            var input = System.IO.File.ReadAllLines("C:\\Workspace\\AdventOfCode\\2022\\Christoph\\AdventOfCode\\Day3\\input.txt");

            var stuff = input
                .Select(x => x.Substring(0, x.Length / 2).Intersect(x.Substring(x.Length/2, x.Length - x.Length/2)))
                .Select(x => x.First())
                .Select(CharValue)
                .Sum();

            var stuff2 = input
                .Select((value, index) => new { PairNum = index / 3, value })
                .GroupBy(pair => pair.PairNum)
                .Select(grp => grp.Select(g => g.value)
                    .ToArray())
                .Select(x => x[0].Intersect(x[1].Intersect(x[2])).First())
                .Select(CharValue)
                .Sum();


            Console.WriteLine(stuff);
            Console.WriteLine(stuff2);

        }
        private static int CharValue(char c)
        {
            return c <= 'Z' ? c - 'A' + 27 : c - 'a' + 1;
        }
    }
}