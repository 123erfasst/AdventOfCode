package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strings"
)

func main() {
	lines, err := readInput("./input.txt")
	if err != nil {
		log.Fatalf("readLines: %s", err)
	}

	var score = 0
	for _, line := range lines {
		var left = line[:len(line)/2]
		var right = line[len(line)/2:]
		for _, letter := range left {
			if strings.Index(right, string(letter)) >= 0 {
				score += getRuneScore(letter)
				break
			}
		}
	}
	var groupedScore = 0
	for i := 0; i < len(lines); i += 3 {

		for _, letter := range lines[i] {
			if strings.Index(lines[i+1], string(letter)) >= 0 && strings.Index(lines[i+2], string(letter)) >= 0 {
				groupedScore += getRuneScore(letter);
				break;
			}
		}

	}
	fmt.Printf("Sum of the priorities is %v", score)
	fmt.Println();
	fmt.Printf("Sum of the priorities of groups of three is %v", groupedScore)
}

func getRuneScore(r rune) int {
	var score = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
	return strings.Index(score, string(r))
}

func readInput(path string) ([]string, error) {
	file, err := os.Open(path)
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	var lines []string
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		lines = append(lines, scanner.Text())
	}

	return lines, scanner.Err()
}
