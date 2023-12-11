$lines =
'...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....' -split '\r?\n'

$lines = Get-Content 'resources/day11.txt'

$size = $lines.Count

$galaxies = $lines
| ForEach-Object { $y = 0 } {
    $galaxies = $_.ToCharArray()
    | ForEach-Object { $x = 0 } {
        $galaxy = [PSCustomObject]@{
            X = $x
            Y = $y
            Sym = "$_"
        }
        $x++
        $galaxy
    }
    | Where-Object Sym -EQ '#'
    $y++
    $galaxies
}

$emptyRows = [System.Collections.Generic.HashSet[int]]::new([int[]](0..($size-1)))
$emptyRows.ExceptWith([int[]]($galaxies | ForEach-Object { $_.Y }))

$emptyCols = [System.Collections.Generic.HashSet[int]]::new([int[]](0..($size-1)))
$emptyCols.ExceptWith([int[]]($galaxies | ForEach-Object { $_.X }))

function ExpandGalaxies($galaxies, $by)
{
    $galaxies
    | ForEach-Object {
        $galaxy = $_
        $emptyRowsBefore = ($emptyRows | Where-Object { $_ -lt $galaxy.Y }).Count
        $emptyColsBefore = ($emptyCols | Where-Object { $_ -lt $galaxy.X }).Count
        [PSCustomObject]@{
            X = $galaxy.X + $emptyColsBefore * ($by - 1)
            Y = $galaxy.Y + $emptyRowsBefore * ($by - 1)
        }
    }
}

function ComputePaths($galaxies)
{
    0..($galaxies.Count - 1)
    | ForEach-Object {
        $i = $_
        $galaxy = $galaxies[$i]
        $galaxies
        | Select-Object -Skip ($i + 1)
        | ForEach-Object {
            [math]::Abs($galaxy.X - $_.X) +
            [math]::Abs($galaxy.Y - $_.Y)
        }
    }
}

# part 1

ComputePaths (ExpandGalaxies $galaxies 2)
| Measure-Object -Sum
| Select-Object Sum

# part 2

ComputePaths (ExpandGalaxies $galaxies 1000000)
| Measure-Object -Sum
| Select-Object Sum