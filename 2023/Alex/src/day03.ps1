$lines = 
'467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..' -split '\r?\n'

$lines = Get-Content 'resources/day03.txt'

$nums = 0..($lines.Length - 1)
| ForEach-Object {
    $index = $_
    $lines[$index]
    | Select-String -Pattern '(\d+)' -AllMatches
    | Foreach-Object { $_.Matches }
    | ForEach-Object { $_.Groups[1] }
    | ForEach-Object {
        $num = $_.Value
        @{
            Number=[int]$num
            Col=$_.Index
            Row=$index
            Len=$num.Length
        }
    }
    | Select-Object Number, Col, Row, Len
}

$syms = 0..($lines.Length - 1)
| ForEach-Object {
    $index = $_
    $lines[$index]
    | Select-String -Pattern '([^\d\.])' -AllMatches
    | Foreach-Object { $_.Matches }
    | ForEach-Object { $_.Groups[1] }
    | ForEach-Object {
        @{
            Symbol=$_.Value
            Col=$_.Index
            Row=$index
        } 
    }
    | Select-Object Symbol, Col, Row
}

# part 1

$nums
| Where-Object {
    $num = $_
    $adjacentToSymbol = $syms
    | Where-Object {
        $sym = $_
        ($num.Row - 1) -le $sym.Row -and $sym.Row -le ($num.Row + 1) -and
        ($num.Col - 1) -le $sym.Col -and $sym.Col -le ($num.Col + $num.Len)
    }
    $adjacentToSymbol.Length -gt 0
}
| Measure-Object -Property Number -Sum
| Select-Object Sum

# part 2

$syms
| Where-Object { $_.Symbol -eq '*' }
| ForEach-Object {
    $sym = $_
    $adjacentToSymbol = $nums
    | Where-Object {
        $num = $_
        ($num.Row - 1) -le $sym.Row -and $sym.Row -le ($num.Row + 1) -and
        ($num.Col - 1) -le $sym.Col -and $sym.Col -le ($num.Col + $num.Len)
    }
    if ($adjacentToSymbol.Length -eq 2)
    {
        $adjacentToSymbol[0].Number * $adjacentToSymbol[1].Number
    } else
    {
        0
    }
}
| Measure-Object -Sum
| Select-Object Sum
