package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
)

func main() {
	lines, err := readInput("./input.txt")
	if err != nil {
		log.Fatalf("readLines: %s", err)
	}

	var scoreWrongAssumption = 0
	var scoreRightAssumption = 0
	for _, line := range lines {
		var inputOpponent = line[0]
		var inputPlayer = line[2]

		scoreWrongAssumption += getScore(inputPlayer, inputOpponent)
		scoreRightAssumption += getScore(getPlayerInput(inputOpponent, inputPlayer), inputOpponent)
	}

	fmt.Printf("You scored %v points with the wrong assumption", scoreWrongAssumption)
	fmt.Println()
	fmt.Printf("You scored %v points with the right assumption", scoreRightAssumption)
}

func getPlayerInput(inputOpponent uint8, playerCommand uint8) uint8 {
	var playerMove uint8
	if playerCommand == 'X' { // lose
		if inputOpponent == 'A' {
			playerMove = 'Z'
		} else if inputOpponent == 'B' {
			playerMove = 'X'
		} else {
			playerMove = 'Y'
		}
	} else if playerCommand == 'Y' { // draw
		if inputOpponent == 'A' {
			playerMove = 'X'
		} else if inputOpponent == 'B' {
			playerMove = 'Y'
		} else {
			playerMove = 'Z'
		}
	} else if playerCommand == 'Z' { // win
		if inputOpponent == 'A' {
			playerMove = 'Y'
		} else if inputOpponent == 'B' {
			playerMove = 'Z'
		} else {
			playerMove = 'X'
		}
	}
	return playerMove
}

func getScore(inputPlayer uint8, inputOpponent uint8) int {
	var score = 0
	if inputPlayer == 'X' {
		score += 1
		if inputOpponent == 'A' {
			score += 3
		} else if inputOpponent == 'C' {
			score += 6
		}
	} else if inputPlayer == 'Y' {
		score += 2
		if inputOpponent == 'B' {
			score += 3
		} else if inputOpponent == 'A' {
			score += 6
		}
	} else if inputPlayer == 'Z' {
		score += 3
		if inputOpponent == 'C' {
			score += 3
		} else if inputOpponent == 'B' {
			score += 6
		}
	}
	return score
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
