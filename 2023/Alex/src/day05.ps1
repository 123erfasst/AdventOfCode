$lines =
'seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4' -split '\r?\n'

$lines = Get-Content 'resources/day05.txt'

$seeds = $lines[0]
| Select-String -Pattern '(\d+)' -AllMatches
| ForEach-Object { $_.Matches }
| ForEach-Object { [long]$_.Groups[1].Value }

$maps = ($lines[2..$lines.Length] -join ' ')
| Select-String -Pattern '(?<source>\w+)-to-(?<destination>\w+) map:(?<numbers>(\s+(\d+))+)' -AllMatches
| ForEach-Object { $_.Matches }
| ForEach-Object {
    $groups = $_.Groups
    $numbers = $groups['numbers']
    | Select-String -Pattern '\d+' -AllMatches
    | ForEach-Object{ $_.Matches.Value }
    | ForEach-Object { [long]$_ }
    [PSCustomObject]@{
        Source = $groups['source']
        Destination = $groups['destination']
        Ranges = 0..($numbers.Count / 3 - 1)
        | Foreach-Object { 
            $row = $numbers[($_*3)..($_*3+2)] 
            $src = $row[1]
            $dst = $row[0]
            $len = $row[2]
            [PSCustomObject]@{
                RangeStart = $src
                RangeEnd = $src + $len - 1
                Delta = $dst - $src
            }
        }
    }
}

# part 1

$seeds
| ForEach-Object {
    $number = $_
    $maps
    | ForEach-Object {
        $map = $_.Ranges
        | Where-Object {
            $_.RangeStart -le $number -and $number -le $_.RangeEnd
        }
        | Select-Object -First 1
        if ($map.Count -gt 0)
        {
            $number = $number + $map.Delta
        }

    }
    $number
}
| Measure-Object -Minimum
| Select-Object Minimum

# part 2

$seedRanges = 0..($seeds.Count/2-1)
| ForEach-Object {
    $start = $seeds[$_ * 2]
    $end = $start + $seeds[$_ * 2 + 1] - 1
    [PSCustomObject]@{
        RangeStart = $start
        RangeEnd = $end
        Delta = 0
    }
}

$seedRangesAcc = $seedRanges
$maps | ForEach-Object {
    $_.Ranges | ForEach-Object {
        $range = $_
        $groups = $seedRangesAcc | Group-Object -AsHashTable { $_.Delta -eq 0 }
        $mapped = $groups[$false]
        $notMapped = $groups[$true] | ForEach-Object {
            $seedRange = $_
            [PSCustomObject]@{
                RangeStart = $seedRange.RangeStart
                RangeEnd = [math]::Min($range.RangeStart - 1, $seedRange.RangeEnd)
                Delta = 0
            },
            [PSCustomObject]@{
                RangeStart = [math]::Max($range.RangeStart, $seedRange.RangeStart)
                RangeEnd = [math]::Min($range.RangeEnd, $seedRange.RangeEnd)
                Delta = $range.Delta
            },
            [PSCustomObject]@{
                RangeStart = [math]::Max($range.RangeEnd + 1, $seedRange.RangeStart)
                RangeEnd = $seedRange.RangeEnd
                Delta = 0
            }
            | Where-Object { $_.RangeStart -le $_.RangeEnd }
        }
        $seedRangesAcc = $mapped + $notMapped
    }
    $seedRangesAcc = $seedRangesAcc | ForEach-Object {
        [PSCustomObject]@{
            RangeStart = $_.RangeStart + $_.Delta
            RangeEnd = $_.RangeEnd + $_.Delta
            Delta = 0
        }
    }
}
$seedRangesAcc | Measure-Object RangeStart -Minimum | Select-Object Minimum
