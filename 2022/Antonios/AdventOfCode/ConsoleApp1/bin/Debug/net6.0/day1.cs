using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;


public static class Day1
{
    class elf
    {
        public List<int> CaloriesList = new List<int>();
        public int Sum => CaloriesList.Count == 0 ? 0 : CaloriesList.Sum();
    }

    public static void Resolve()
    {
        string path = Path.Combine(Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location), @"Inputs\day1.txt");
        List<elf> list = new List<elf>();
        int elfCounter = 0;
        list.Add(new elf());
        foreach (string line in System.IO.File.ReadLines(path))
        {
            if (string.IsNullOrEmpty(line))
            {
                elfCounter++;
                list.Add(new elf());
            }
            else
                list[elfCounter].CaloriesList.Add(int.Parse(line));
        }

        var highestCal = list.Max(x => x.Sum);
        var top3 = list.OrderByDescending(x => x.Sum).Take(3).Sum(x=>x.Sum);

        Console.WriteLine("--------< Day1 >-----------");
        Console.WriteLine($"Total sum: {highestCal}");
        Console.WriteLine($"Total sum Top 3: {top3}");
        Console.WriteLine("---------------------------");
    }
}
