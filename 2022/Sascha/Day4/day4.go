package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strconv"
	"strings"
)

func main() {
	lines, err := readInput("./input.txt")
	if err != nil {
		log.Fatalf("readLines: %s", err)
	}

	var scoreContained = 0
	var scoreOverlap = 0
	for _, line := range lines {
		var split = strings.Split(line, ",")

		var comp1 = strings.Split(split[0], "-")
		var comp2 = strings.Split(split[1], "-")

		comp1Left, err := strconv.Atoi(comp1[0])
		if err != nil {
			log.Fatal(err)
		}
		comp1Right, err := strconv.Atoi(comp1[1])
		if err != nil {
			log.Fatal(err)
		}
		comp2Left, err := strconv.Atoi(comp2[0])
		if err != nil {
			log.Fatal(err)
		}
		comp2Right, err := strconv.Atoi(comp2[1])
		if err != nil {
			log.Fatal(err)
		}

		if comp1Left <= comp2Left && comp1Right >= comp2Right || comp2Left <= comp1Left && comp2Right >= comp1Right {
			scoreContained++
		}
		if !(comp1Left < comp2Left && comp1Right < comp2Left || comp2Left < comp1Left && comp2Right < comp1Left) {
			scoreOverlap++
		}
	}
	fmt.Printf("%v assignment pairs fully contain the other", scoreContained)
	fmt.Println()
	fmt.Printf("%v assignment pairs overlap the other", scoreOverlap)

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
