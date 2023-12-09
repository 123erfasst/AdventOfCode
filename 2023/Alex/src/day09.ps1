$lines =
'0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45' -split '\r?\n'

$lines = Get-Content 'resources/day09.txt'

# part 1

function Succ($numbers)
{
    $result = ($numbers.Count - 1)..1
    | ForEach-Object {
        $nums = $numbers
    } {
        $deltas = 1..$_ | ForEach-Object {
            $nums[$_] - $nums[$_ - 1]
        }
        $nums = $deltas
        $deltas[-1]
    }
    | Measure-Object -Sum
    $numbers[-1] + $result.Sum
}

$lines | ForEach-Object {
    $nums = ($_ | Select-String -Pattern '-?\d+' -AllMatches).Matches.Value
    | ForEach-Object { [int]$_ }
    Succ $nums
}
| Measure-Object -Sum
| Select-Object Sum

# part 2

function Prev($numbers)
{
    $ds = ($numbers.Count - 1)..1
    | ForEach-Object {
        $nums = $numbers
    } {
        $deltas = 1..$_ | ForEach-Object {
            $nums[$_] - $nums[$_ - 1]
        }
        $nums = $deltas
        $deltas[0]
    }
    $d = ($ds.Count-1)..0
    | ForEach-Object { $acc = 0 } { $acc = $ds[$_] - $acc; $acc }
    | Select-Object -Last 1
    $numbers[0] - $d
}

$lines | ForEach-Object {
    $nums = ($_ | Select-String -Pattern '-?\d+' -AllMatches).Matches.Value
    | ForEach-Object { [int]$_ }
    Prev $nums
}
| Measure-Object -Sum
| Select-Object Sum
