USING: io.files io.encodings.utf8 regexp sequences math.parser locals math sequences.extras kernel splitting arrays math.ranges sequences.repeating strings unicode sets assocs combinators io vectors hashtables prettyprint sequences.deep math.order ;
IN: day07

: input ( -- lines ) "../../resources/day07.txt" utf8 file-lines ;

:: parse-line ( line stack -- )
    {
        { [ line "$ cd .." = ]
          [ stack pop "children" stack last at push ] }
        { [ line 4 head "$ cd" = ]
          [ { "name" "children" "size" } line 5 tail 0 <vector> 0 3array zip stack push ] }
        { [ line R/ ^\d+/ re-contains? ]
          [ "size" stack last [ line R/ ^\d+/ first-match string>number + ] change-at ] }
        [ ]
    } cond
    ; inline

:: compute-size ( dir -- dir' )
    "size" dir [
        "children" dir at
        [ compute-size "size" swap at ] map
        sum +
    ] change-at
    dir
    ;

: part1 ( -- result )
    input V{ } [ over parse-line ] reduce
    dup length 1 - "$ cd .." <repetition> swap [ over parse-line ] reduce
    first
    compute-size
    [ dup number? [ 100000 <= ] [ drop f ] if ] deep-filter
    sum
    ;

:: min-size-larger-than ( dir size -- min-size )
    dir [ dup number? [ size > ] [ drop f ] if ] deep-filter
    "size" dir at [ min ] reduce
    ; inline

: part2 ( -- result )
    input V{ } [ over parse-line ] reduce
    dup length 1 - "$ cd .." <repetition> swap [ over parse-line ] reduce
    first
    compute-size
    "size" over at
    40000000 -
    min-size-larger-than
    ;
