USING: io.files io.encodings.utf8 splitting math.parser sequences sorting ;
IN: day01

: sorted-sums ( filepath -- sums )
    utf8 file-lines
    { "" } split
    [ [ string>number ] map sum ] map
    natural-sort
    reverse
    ;

: part1 ( -- result )
    "../../resources/day01.txt"
    sorted-sums
    first
    ;

: part2 ( -- result )
    "../../resources/day01.txt"
    sorted-sums
    3 head sum
    ;
