$lines = Get-Content 'resources/day01.txt'

# part 1

$lines = '1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet' -split '\n'

$lines
| ForEach-Object {
    $digits = ($_ -replace '[^\d]').ToCharArray()
    $first = $digits | Select-Object -First 1
    $last  = $digits | Select-Object -Last  1
    [int]"$first$last"
}
| Measure-Object -Sum
| Select-Object Sum

# part 2

$lines = 
'two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen' -split '\r?\n'

$dict = @{
    '1'     = 1
    '2'     = 2
    '3'     = 3
    '4'     = 4
    '5'     = 5
    '6'     = 6
    '7'     = 7
    '8'     = 8
    '9'     = 9
    'one'   = 1
    'two'   = 2
    'three' = 3
    'four'  = 4
    'five'  = 5
    'six'   = 6
    'seven' = 7
    'eight' = 8
    'nine'  = 9
}

$lines
| ForEach-Object {
    $line = $_
    $idxs = $dict.Keys | ForEach-Object { [System.Tuple]::Create($line.indexOf($_), $line.LastIndexOf($_), $dict[$_]) }
    $first = $idxs | Where-Object Item1 -NE -1 | Sort-Object Item1             | Select-Object -First 1
    $last  = $idxs | Where-Object Item2 -NE -1 | Sort-Object Item2 -Descending | Select-Object -First 1
    $first.Item3 * 10 + $last.Item3
}
| Measure-Object -Sum
| Select-Object Sum
