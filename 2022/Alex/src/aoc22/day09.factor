USING: io.files io.encodings.utf8 regexp sequences math.parser locals math sequences.extras kernel splitting arrays math.ranges sequences.repeating strings unicode sets assocs combinators io vectors hashtables prettyprint sequences.deep math.order hash-sets ;
IN: day09

: input ( -- lines ) "../../resources/day09.txt" utf8 file-lines ;

:: step ( h tt -- tt' )
    h first :> hx
    h second :> hy
    h tt [ - ] 2map :> d
    d first :> dx
    d second :> dy
    {
        { [ dx  2 = dy  2 = and ] [ hx 1 - hy 1 - 2array ] }
        { [ dx -2 = dy  2 = and ] [ hx 1 + hy 1 - 2array ] }
        { [ dx  2 = dy -2 = and ] [ hx 1 - hy 1 + 2array ] }
        { [ dx -2 = dy -2 = and ] [ hx 1 + hy 1 + 2array ] }
        { [ dx  1 > ] [ hx 1 - hy 2array ] }
        { [ dx -1 < ] [ hx 1 + hy 2array ] }
        { [ dy  1 > ] [ hx hy 1 - 2array ] }
        { [ dy -1 < ] [ hx hy 1 + 2array ] }
        [ tt ]
    } cond
    ;

:: rfn ( state dir -- state' )
    state first :> h
    state second :> tt
    h dir [ + ] 2map :> h'
    h' h' tt step 2array
    ;

:: part1 ( -- result )
    input [
        [ 2 tail string>number ]
        [ 1 head {
              { [ dup "L" = ] [ drop { -1  0 } ] }
              { [ dup "R" = ] [ drop {  1  0 } ] }
              { [ dup "U" = ] [ drop {  0  1 } ] }
              { [ dup "D" = ] [ drop {  0 -1 } ] }
          } cond ]
        bi <repetition>
    ] map concat { { 0 0 } { 0 0 } } [
        rfn
    ] accumulate swap suffix [
        second
    ] map >hash-set cardinality
    ;

:: rfn2 ( points dir -- points' )
    points rest
    points first dir [ + ] 2map [
        step
    ] accumulate swap suffix
    ;

:: part2 ( -- result )
    input [
        [ 2 tail string>number ]
        [ 1 head {
              { [ dup "L" = ] [ drop { -1  0 } ] }
              { [ dup "R" = ] [ drop {  1  0 } ] }
              { [ dup "U" = ] [ drop {  0  1 } ] }
              { [ dup "D" = ] [ drop {  0 -1 } ] }
          } cond ]
        bi <repetition>
    ] map concat 10 { 0 0 } <repetition> [
        rfn2
    ] accumulate swap suffix [
        last
    ] map >hash-set cardinality
    ;
