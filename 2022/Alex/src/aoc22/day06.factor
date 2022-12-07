USING: io.files io.encodings.utf8 regexp sequences math.parser locals math sequences.extras kernel splitting arrays math.ranges sequences.repeating strings unicode sets ;
IN: day06

: input ( -- lines ) "../../resources/day06.txt" utf8 file-lines ;

:: first-unique-subseq ( str window-size -- n )
    str length window-size -
    [
        dup window-size + str <slice> all-unique?
    ] find-integer
    window-size +
    ;

: part1 ( -- result )
    input first 4 first-unique-subseq
    ;

: part2 ( -- result )
    input first 14 first-unique-subseq
    ;
