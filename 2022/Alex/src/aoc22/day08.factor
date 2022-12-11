USING: io.files io.encodings.utf8 regexp sequences math.parser locals math sequences.extras kernel splitting arrays math.ranges sequences.repeating strings unicode sets assocs combinators io vectors hashtables prettyprint sequences.deep math.order hash-sets ;
IN: day08

: input ( -- lines ) "../../resources/day08.txt" utf8 file-lines ;

:: visible ( idxs chars -- seq )
    idxs [ chars nth ] map :> trees
    idxs idxs -1 [ chars nth max ] accumulate swap suffix
    [ over chars nth < [ ] [ drop f ] if ] 2map
    [ ] filter
    ;

:: part1 ( -- result )
    input length :> line-len
    line-len line-len * :> len
    input concat [ "0" first - ] map >array :> chars
    len <iota> line-len len 1 - line-len <range> split-indices :> left
    left [ <reversed> ] map :> right
    left flip :> top
    top [ <reversed> ] map :> bottom

    { left right top bottom } concat
    [ chars visible ] map
    concat >hash-set cardinality
    ;

:: step ( acc n -- acc' )
    n 1 + :> k
    k 0 <repetition>
    k acc length acc subseq
    2array concat
    [ 1 + ] map
    ;

:: compute-visibility ( idxs chars -- seq )
    10 0 <repetition> :> init
    idxs init [ chars nth step ] accumulate swap suffix rest
    ;

:: visible-neighbors ( i chars ll rr tt bb len -- n )
    i chars nth :> char
    i len rem :> x
    i len /i :> y
    char x 1 - y ll ?nth ?nth ?nth :> ln
    char len x - 2 - y rr ?nth ?nth ?nth :> rn
    char y 1 - x tt ?nth ?nth ?nth :> tn
    char len y - 2 - x bb ?nth ?nth ?nth :> bn
    { ln rn tn bn } [ ] filter product
    ;

:: part2 ( -- result )
    input length :> line-len
    line-len line-len * :> len
    input concat [ "0" first - ] map >array :> chars
    len <iota> line-len len 1 - line-len <range> split-indices :> left
    left [ <reversed> ] map :> right
    left flip :> top
    top [ <reversed> ] map :> bottom
    left [ chars compute-visibility ] map :> ll
    right [ chars compute-visibility ] map :> rr
    top [ chars compute-visibility ] map :> tt
    bottom [ chars compute-visibility ] map :> bb
    len <iota> [
        chars ll rr tt bb line-len visible-neighbors
    ] [ max ] map-reduce
    ;
