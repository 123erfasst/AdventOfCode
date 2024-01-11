namespace AdventOfCode;

public abstract class Day
{
    protected abstract void Part1();
    protected abstract void Part2();

    public void Start(string part)
    {
        switch (part)
        {
            case "1":
                Part1();
                break;
            case "2":
                Part2();
                break;
            default:
                Console.WriteLine("Teil nicht gefunden");
                break;
        }
    }
}