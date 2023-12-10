$lines =
'-L|F7
7S-7|
L|7||
-L-J|
L|-JF' -split '\r?\n'

$lines = Get-Content 'resources/day10.txt'

$dirs = @{
    'N' = [PSCustomObject]@{ DX =  0; DY = -1; Valid = '|7FS' }
    'E' = [PSCustomObject]@{ DX =  1; DY =  0; Valid = '-7JS' }
    'S' = [PSCustomObject]@{ DX =  0; DY =  1; Valid = '|JLS' }
    'W' = [PSCustomObject]@{ DX = -1; DY =  0; Valid = '-FLS' }
}

$start = 0..($lines.Count - 1)
| ForEach-Object {
    [System.Tuple]::Create($_, $lines[$_].IndexOf('S'))
}
| Where-Object { $_.Item2 -ne -1 }

$validTransitions = @{
    '|' = 'NS'
    '-' = 'EW'
    'F' = 'ES'
    '7' = 'SW'
    'L' = 'NE'
    'J' = 'NW'
    'S' = 'NESW'
}

# part 1

$path = try
{
    1..[int]::MaxValue
    | ForEach-Object { $paths = ,[System.Collections.Stack]@(,$start) } {
        $newPaths = [System.Collections.Stack]@()
        $paths
        | ForEach-Object {
            $path = $_
            $pos = $path.Peek()
            $posCh = $lines[$pos.Item1][$pos.Item2]
            $prev = $path | Select-Object -Skip 1 -First 1
            $validTransitions["$posCh"].ToCharArray()
            | ForEach-Object {
                $dir = $dirs["$_"]
                $newPos = [System.Tuple]::Create($pos.Item1 + $dir.DY, $pos.Item2 + $dir.DX)
                $newCh = $lines[$newPos.Item1][$newPos.Item2]
                if ($dir.Valid.Contains($newCh) -and
                    $newPos -ne $prev)
                {
                    if ($start -eq $newPos)
                    {
                        throw $newPath
                    }
                    $newPath = $path.Clone()
                    $newPath.Push($newPos)
                    $newPaths.Push($newPath)
                }
            }
        }
        $paths = $newPaths
    }
} catch { $_.TargetObject }

$path.Count / 2

# part 2

0..($lines.Count - 1)
| ForEach-Object { $seen = @{}; $path | ForEach-Object { $seen[$_] = $null } } {
    $row = $_
    $prev = $null
    $intersections = 0
    $acc = 0
    0..($lines[0].Length - 1)
    | ForEach-Object {
        $col = $_
        $ch = $lines[$row][$col]
        $pos = [System.Tuple]::Create($row, $col)
        if ($seen.ContainsKey($pos))
        {
            switch ("$ch")
            {
                '.'
                {
                    if (($intersections % 2) -eq 1)
                    {
                        $acc++
                    }
                    $prev = "$ch"
                }
                'S' { $intersections++; $prev = "$ch" }
                '|' { $intersections++; $prev = "$ch" }
                '7'
                {
                    if ($prev -ne 'L')
                    {
                        $intersections++
                    }
                    $prev = "$ch"
                }
                'J'
                {
                    if ($prev -ne 'F')
                    {
                        $intersections++
                    }
                    $prev = "$ch"
                }
                'F' { $intersections++; $prev = "$ch" }
                'L' { $intersections++; $prev = "$ch" }
                Default {}
            }
        } else
        {
            if (($intersections % 2) -eq 1)
            {
                $acc++
            }
        }
    }
    $acc
}
| Measure-Object -Sum
| Select-Object Sum
