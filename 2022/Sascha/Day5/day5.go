package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"regexp"
	"strconv"
	"strings"
)

func main() {
	lines, err := readInput("./input.txt")
	if err != nil {
		log.Fatalf("readLines: %s", err)
	}

	stack9000, commands := generateData(lines)
	stack9001, commands := generateData(lines)

	for _, command := range commands {
		stack9000 = moveWithCrateMover9000(stack9000, command)
		stack9001 = moveWithCrateMover9001(stack9001, command)
	}

	var res9000 = ""
	for _, crates := range stack9000 {
		var crate = crates[len(crates)-1]
		res9000 += crate[1 : len(crate)-1]
	}
	var res9001 = ""
	for _, crates := range stack9001 {
		var crate = crates[len(crates)-1]
		res9001 += crate[1 : len(crate)-1]
	}

	fmt.Printf("The top creates with CreateMover9000 are %v", res9000)
	fmt.Println()
	fmt.Printf("The top creates with CreateMover9001 are %v", res9001)
}

func moveWithCrateMover9000(stack [][]string, command string) [][]string {
	howMany, startPosition, endPosition := parseCommand(command)

	for i := 0; i < howMany; i++ {
		newList, removed := removeTop(stack[startPosition-1])
		stack[startPosition-1] = newList
		stack[endPosition-1] = append(stack[endPosition-1], removed)
	}

	return stack
}

func moveWithCrateMover9001(stack [][]string, command string) [][]string {
	howMany, startPosition, endPosition := parseCommand(command)

	var removedCrates []string
	for i := 0; i < howMany; i++ {
		newList, removed := removeTop(stack[startPosition-1])
		stack[startPosition-1] = newList
		removedCrates = append(removedCrates, removed)
	}

	for i := len(removedCrates) - 1; i >= 0; i-- {
		stack[endPosition-1] = append(stack[endPosition-1], removedCrates[i])
	}

	return stack
}

func removeTop(slice []string) ([]string, string) {
	return append(slice[:len(slice)-1], slice[len(slice):]...), slice[len(slice)-1]
}

func parseCommand(command string) (int, int, int) {
	regex := *regexp.MustCompile(`move (\d+) from (\d+) to (\d+)`)
	res := regex.FindAllStringSubmatch(command, -1)

	res1, err := strconv.Atoi(res[0][1])
	if err != nil {
		log.Fatal(err)
	}
	res2, err := strconv.Atoi(res[0][2])
	if err != nil {
		log.Fatal(err)
	}
	res3, err := strconv.Atoi(res[0][3])
	if err != nil {
		log.Fatal(err)
	}

	return res1, res2, res3
}

func generateData(lines []string) ([][]string, []string) {
	var helperStack [][]string
	var stack [][]string
	var commands []string

	for _, line := range lines {
		if strings.Index(line, "[") >= 0 {
			var createLine = line
			for strings.Index(createLine, "]    ") >= 0 {
				createLine = strings.ReplaceAll(createLine, "]    ", "] [_]")
			}
			createLine = strings.ReplaceAll(createLine, "    ", "[_] ")
			var crates = strings.Split(createLine, " ")
			helperStack = append(helperStack, crates)
		} else if strings.Index(line, "move") >= 0 {
			commands = append(commands, line)
		}
	}

	for j, crates := range helperStack {
		for i, crate := range crates {
			if j == 0 {
				stack = append(stack, []string{})
			}
			if crate != "[_]" {
				stack[i] = append(stack[i], crate)
			}
		}
	}

	for i, crates := range stack {
		var s = crates
		for i, j := 0, len(s)-1; i < j; i, j = i+1, j-1 {
			s[i], s[j] = s[j], s[i]
		}
		stack[i] = crates
	}

	return stack, commands
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
