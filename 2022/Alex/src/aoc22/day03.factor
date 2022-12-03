USING: io.files io.encodings.utf8 sets sequences kernel math arrays unicode locals math.ranges splitting ;
IN: day03

: input ( -- lines ) "../../resources/day03.txt" utf8 file-lines ;

: upper-case ( -- int ) 0 "A" nth 27 - ;
: lower-case ( -- int ) 0 "a" nth 1 - ;

:: priority ( char -- int )
    0 char nth
    char upper? [ upper-case ] [ lower-case ] if
    -
    ;

: part1 ( -- result )
    input
    [ [ dup length 2 / head ]
      [ dup length 2 / tail ]
      bi 2array
      intersection
      priority ]
    map sum
    ;

:: partition ( seq n -- seqs )
    seq length n / [1,b)
    [ n * ] map
    seq swap split-indices
    ;

: part2 ( -- result )
    input
    3 partition
    [ intersection priority ] map
    sum
    ;
