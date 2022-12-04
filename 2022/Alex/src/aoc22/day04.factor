USING: io.files io.encodings.utf8 regexp sequences math.parser locals math sequences.extras kernel ;
IN: day04

: input ( -- lines ) "../../resources/day04.txt" utf8 file-lines ;

:: contains ( seq -- ? )
    0 seq nth 2 seq nth <=
    3 seq nth 1 seq nth <=
    and
    2 seq nth 0 seq nth <=
    1 seq nth 3 seq nth <=
    and
    or
    ;

: part1 ( -- result )
    input
    [ R/ \d+/ all-matching-subseqs [ string>number ] map ]
    [ contains ]
    map-filter
    length
    ;

:: overlaps ( seq -- ? )
    1 seq nth 2 seq nth <
    3 seq nth 0 seq nth <
    or
    not
    ;

: part2 ( -- result )
    input
    [ R/ \d+/ all-matching-subseqs [ string>number ] map ]
    [ overlaps ]
    map-filter
    length
    ;
