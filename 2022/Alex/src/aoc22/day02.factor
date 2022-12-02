USING: io.files io.encodings.utf8 assocs sequences ;
IN: day02

: input ( -- lines ) "../../resources/day02.txt" utf8 file-lines ;

: part1 ( -- result )
    input
    [ H{ { "B X" 1 }
         { "C Y" 2 }
         { "A Z" 3 }
         { "A X" 4 }
         { "B Y" 5 }
         { "C Z" 6 }
         { "C X" 7 }
         { "A Y" 8 }
         { "B Z" 9 } }
      at ]
    map sum
    ;

: part2 ( -- result )
    input
    [ H{ { "B X" 1 }
         { "C X" 2 }
         { "A X" 3 }
         { "A Y" 4 }
         { "B Y" 5 }
         { "C Y" 6 }
         { "C Z" 7 }
         { "A Z" 8 }
         { "B Z" 9 } }
      at ]
    map sum
    ;
