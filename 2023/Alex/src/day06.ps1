$lines =
'Time:      7  15   30
Distance:  9  40  200'

$lines = Get-Content 'resources/day06.txt'

# part 1

$nums = ($lines | Select-String -Pattern '\d+' -AllMatches).Matches.Value
| ForEach-Object { [int]$_ }

$records = 0..($nums.Count / 2 - 1)
| ForEach-Object {
    [PSCustomObject]@{
        Time = $nums[$_]
        Distance = $nums[$_ + $nums.Count / 2]
    }
}

$records
| ForEach-Object -Begin { $result = 1 } -Process {
    $record = $_
    $better = 0..$record.Time | ForEach-Object {
        ($record.Time - $_) * $_
    }
    | Where-Object { $_ -gt $record.Distance }
    $result = $result * $better.Count
    $result
}
| Select-Object -Last 1

# part 2

$nums = ($lines | Select-String -Pattern '[\d\s]+' -AllMatches).Matches.Value
| ForEach-Object { [long]($_.Trim() -replace '\s+', '') }

$records = 0..($nums.Count / 2 - 1)
| ForEach-Object {
    [PSCustomObject]@{
        Time = $nums[$_]
        Distance = $nums[$_ + $nums.Count / 2]
    }
}

function PQ($p, $q)
{
    $x = [math]::Sqrt([math]::Pow($p / 2, 2) - $q)
    [PSCustomObject]@{
        Low = [math]::Floor(-$p/2 - $x) + 1
        High = [math]::Ceiling(-$p/2 + $x) - 1
    }
}

$records
| ForEach-Object { PQ (-$_.Time) $_.Distance }
| ForEach-Object { $_.High + 1 - $_.Low }
