package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
)

func main() {
	line, err := readInput("./input.txt")
	if err != nil {
		log.Fatalf("readLines: %s", err)
	}

	fmt.Printf("Start of PackageMarker is at %v", getStartOfPackageMarker(line))
	fmt.Println()
	fmt.Printf("Start of Message is at %v", getStartOfMessageMarker(line))
}

func getStartOfPackageMarker(message string) int {
	var lastEntries []int32

	for idx, char := range message {
		if len(lastEntries) < 4 {
			lastEntries = append(lastEntries, char)
		} else {
			lastEntries = append(lastEntries[1:], char)
			if isStartOfInput(lastEntries, 4) {
				return idx + 1
			}
		}
	}
	return -1
}

func getStartOfMessageMarker(message string) int {
	var lastEntries []int32

	for idx, char := range message {
		if len(lastEntries) < 14 {
			lastEntries = append(lastEntries, char)
		} else {
			lastEntries = append(lastEntries[1:], char)
			if isStartOfInput(lastEntries, 14) {
				return idx + 1
			}
		}
	}
	return -1
}

func isStartOfInput(input []int32, distinctCount int) bool {
	if len(input) != distinctCount {
		return false
	}
	var checked []int32
	for _, entry := range input {
		if contains(checked, entry) {
			return false
		}
		checked = append(checked, entry)
	}
	return true
}

func contains(s []int32, e int32) bool {
	for _, a := range s {
		if a == e {
			return true
		}
	}
	return false
}

func readInput(path string) (string, error) {
	file, err := os.Open(path)
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	var line string
	scanner := bufio.NewScanner(file)
	if scanner.Scan() {
		line = scanner.Text()
	}

	return line, scanner.Err()
}
