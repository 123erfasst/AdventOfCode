USING: io.files io.encodings.utf8 regexp sequences math.parser locals math sequences.extras kernel splitting arrays math.ranges sequences.repeating strings unicode ;
IN: day05

: input ( -- lines ) "../../resources/day05.txt" utf8 file-lines ;

:: parse-stacks ( lines -- stacks )
    1 lines first length 4 <range>
    lines
    [ nth ]
    cartesian-map
    [ [ blank? ] trim-head reverse ] map
    ;

: parse-op ( lines -- ops )
    R/ \d+/ all-matching-subseqs [ string>number ] map
    ;

: parse ( header body -- ops stacks )
    [ parse-op ] map
    swap
    parse-stacks
    ;

:: apply-op ( stacks op -- new-stacks )
    0 op nth :> n
    1 op nth 1 - :> from
    2 op nth 1 - :> to
    from stacks nth :> source
    to stacks nth :> target
    
    target
    source reverse n head
    2array concat
    to stacks set-nth

    0 source length n - source subseq
    from stacks set-nth

    stacks
    ;

: part1 ( -- result )
    input { "" } split
    [ first but-last ] [ second ] bi parse
    [ apply-op ] reduce
    [ last ] map
    >string
    ;

:: apply-op2 ( stacks op -- new-stacks )
    0 op nth :> n
    1 op nth 1 - :> from
    2 op nth 1 - :> to
    from stacks nth :> source
    to stacks nth :> target
    
    target
    source dup length n - tail
    2array concat
    to stacks set-nth

    0 source length n - source subseq
    from stacks set-nth

    stacks
    ;

: part2 ( -- result )
    input { "" } split
    [ first but-last ] [ second ] bi parse
    [ apply-op2 ] reduce
    [ last ] map
    >string
    ;
