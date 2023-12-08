$lines =
'RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)' -split '\r?\n'

$lines =
'LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)' -split '\r?\n'

$lines = Get-Content 'resources/day08.txt'

$ops = $lines[0]

$dirs = $lines | Select-Object -Skip 2
| ForEach-Object {
    $dict = @{}
} {
    $vals = $_ | Select-String -Pattern '([A-Z]{3})' -AllMatches
    | ForEach-Object { $_.Matches }
    | ForEach-Object { $_.Groups[1].Value }
    $dict[$vals[0]] = [PSCustomObject]@{
        Left = $vals[1]
        Right = $vals[2]
    }
    $dict
}
| Select-Object -Last 1

function Transition($idx)
{
    switch ($ops[$idx % $ops.Length])
    {
        'L' { $dirs[$current].Left }
        'R' { $dirs[$current].Right }
    }
}

# part 1

try
{
    0..[int]::MaxValue
    | ForEach-Object -begin {
        $current = 'AAA'
        $idx = -1
    } { 
        $idx = $_
        $current = Transition $idx
        if ($current -eq 'ZZZ')
        {
            throw $idx + 1
        }
    }
} catch { $_.TargetObject }

# part 2

function GCD($a, $b)
{
    if ($a -eq 0) { $b }
    elseif ($b -eq 0) { $a }
    elseif ($a -lt $b) { GCD $b $a }
    else { GCD $b ($a % $b) }
}

function LCM($a, $b)
{
    [math]::Abs($a * $b) / (GCD $a $b)
}

$lines 
| Select-Object -Skip 2
| Where-Object { $_[2] -eq 'A' }
| ForEach-Object { $result = 1 } {
    $start = $_.Substring(0, 3)
    $steps = try
    {
        0..[int]::MaxValue
        | ForEach-Object -begin {
            $current = $start
            $idx = -1
        } {
            $idx = $_
            $current = Transition $idx
            if ($current[2] -eq 'Z')
            {
                throw $idx + 1
            }
        }
    } catch { $_.TargetObject }
    $result = LCM $result $steps
    $result
}
| Select-Object -Last 1
