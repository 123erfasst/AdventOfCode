$lines =
'32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483' -split '\r?\n'

$lines = Get-Content 'resources/day07.txt'

$cardVals = '23456789TJQKA'

function CardValue($card)
{
    $cardVals.IndexOf($card)
}

function HandStrength($hand)
{
    $groups = $hand.ToCharArray() | Group-Object { $_ }
    $counts = $groups | ForEach-Object { $_.Count }
    switch ($groups.Values.Count)
    {
        5 { 0 }
        4 { 1 }
        3 { if ($counts -contains 3) { 3 } else { 2 } }
        2 { if ($counts -contains 4) { 5 } else { 4 } }
        1 { 6 }
    }
}

#part 1

$lines
| ForEach-Object {
    $parts = $_ -split ' '
    $hand = $parts[0]
    $bid = [int]$parts[1]
    $strength = HandStrength $hand
    $handOrder = $hand.ToCharArray() | ForEach-Object { CardValue $_ }
    $order = @($strength) + $handOrder | ForEach-Object { [char](97 + $_) }
    [PSCustomObject]@{
        Hand = $hand
        Bid = $bid
        Order = $order -join ''
    }
}
| Sort-Object Order
| ForEach-Object { $rank = 0 } {
    $rank++;
    $rank * $_.Bid
}
| Measure-Object -Sum
| Select-Object Sum

# part 2

$cardVals = 'J23456789TQKA'

$upgradeMap = @{
    0 = 1
    1 = 3
    2 = 4
    3 = 5
    4 = 5
    5 = 6
    6 = 6
}

function HandStrength($hand)
{
    $groups = $hand.ToCharArray()
    | Group-Object { "$_" }
    | Where-Object { $_.Name -ne 'J' -and $_.Count -gt 1 }
    | Sort-Object Count
    | ForEach-Object { $_.Count }
    $strength = if ($groups -contains 5)
    { 6 } elseif ($groups -contains 4)
    { 5 } elseif ($groups -contains 3)
    {
        if ($groups -contains 2)
        { 4 } else
        { 3 }
    } elseif ($groups -contains 2)
    {
        if (($groups | Where-Object { $_ -eq 2 }).Length -eq 2)
        { 2 } else
        { 1 }
    } else
    { 0 }
    $jokers = ($hand -replace '[^J]', '').Length
    1..5 | Select-Object -First $jokers | ForEach-Object {
        $strength = $upgradeMap[$strength]
    }
    $strength
}

$lines
| ForEach-Object {
    $parts = $_ -split ' '
    $hand = $parts[0]
    $bid = [int]$parts[1]
    $strength = HandStrength $hand
    $handOrder = $hand.ToCharArray() | ForEach-Object { CardValue $_ }
    $order = @($strength) + $handOrder | ForEach-Object { [char](97 + $_) }
    [PSCustomObject]@{
        Hand = $hand
        Bid = $bid
        Order = $order -join ''
    }
}
| Sort-Object Order
| ForEach-Object { $rank = 0 } {
    $rank++;
    $rank * $_.Bid
}
| Measure-Object -Sum
| Select-Object Sum
