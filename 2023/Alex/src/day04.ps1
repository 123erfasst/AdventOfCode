$lines = Get-Content 'resources/day04.txt'

$lines =
'Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11' -split '\r?\n'

# part 1

$lines
| Foreach-Object {
    $parts = ($_ -split ':')[1] -split '\|'
    $winning = $parts[0].Trim() -split '\s+'
    $numbers = $parts[1].Trim() -split '\s+'
    ($numbers | Where-Object { $winning -contains $_ }).Count
}
| Where-Object { $_ -gt 0 }
| ForEach-Object { 1 -shl $_ - 1 }
| Measure-Object -Sum
| Select-Object Sum

# part 2

$lines
| Foreach-Object {
    $parts = ($_ -split ':')[1] -split '\|'
    $winning = $parts[0].Trim() -split '\s+'
    $numbers = $parts[1].Trim() -split '\s+'
    $intersection = ($numbers | Where-Object { $winning -contains $_ }).Count
    $card = [int]($_ -replace 'Card\s+(\d+).*', '$1')
    @{
        Card = $card
        Copies = if ($intersection -gt 0) { ($card + 1)..($card + $intersection) }
    }
}
| ForEach-Object -Begin { $dict = @{} } -Process {
    $cards = 1 + ($dict[$_.Card] ?? 0)
    $_.Copies | ForEach-Object {
        if ($dict.ContainsKey($_))
        {
            $dict[$_] = $dict[$_] + $cards
        } else
        {
            $dict[$_] = $cards
        }
    }
    @{ Cards = $cards }
}
| Measure-Object -Sum -Property Cards
| Select-Object Sum
