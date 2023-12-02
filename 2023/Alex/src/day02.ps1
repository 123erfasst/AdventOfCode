$lines = Get-Content "resources/day02.txt"

# part 1

$lines = 'Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green' -split '\n'

function MaxCountForColor ($color, $string)
{
    $string
    | Select-String -Pattern "(\d+) $color" -AllMatches
    | Foreach-Object { $_.Matches }
    | ForEach-Object { $_.Groups[1].Value }
    | Measure-Object -Maximum
    | ForEach-Object { $_.Maximum }
}

$lines
| Where-Object {
    $string = $_ -replace 'Game \d+:', ''
    (MaxCountForColor 'red'   $string) -le 12 -and
    (MaxCountForColor 'green' $string) -le 13 -and
    (MaxCountForColor 'blue'  $string) -le 14
}
| ForEach-Object {
    [int](Select-String -InputObject $_ -Pattern 'Game (\d+)').Matches.Groups[1].Value
}
| Measure-Object -Sum
| Select-Object Sum

# part 2

$lines
| ForEach-Object {
    $string = $_ -replace 'Game \d+:', ''
    $r = MaxCountForColor 'red'   $string
    $g = MaxCountForColor 'green' $string
    $b = MaxCountForColor 'blue'  $string
    $r * $g * $b
}
| Measure-Object -Sum
| Select-Object Sum