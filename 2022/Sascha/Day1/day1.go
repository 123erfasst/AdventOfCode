package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"sort"
	"strconv"
)

func main() {
	lines, err := readInput("./input.txt")
	if err != nil {
		log.Fatalf("readLines: %s", err)
	}

	calorieList, err := countCalories(lines)
	if err != nil {
		log.Fatalf("countCalories: %s", err)
	}
	sort.Sort(sort.Reverse(sort.IntSlice(calorieList)))
	fmt.Printf("Top Elf Calories are %v", calorieList[0])
	fmt.Println()
	fmt.Printf("Top Three Elf Calories are %v", calorieList[0]+calorieList[1]+calorieList[2])
}

func countCalories(lines []string) ([]int, error) {
	var currentElfNumber = 0
	var currentElfCalorieCount = 0
	var calorieList []int
	for i, line := range lines {
		if line == "" || i == len(lines)-1 {
			if i == len(lines)-1 {
				calorie, err := strconv.Atoi(line)
				if err != nil {
					return nil, err
				}
				currentElfCalorieCount += calorie
			}
			calorieList = append(calorieList, currentElfCalorieCount)
			currentElfNumber++
			currentElfCalorieCount = 0
		} else {
			calorie, err := strconv.Atoi(line)
			if err != nil {
				return nil, err
			}
			currentElfCalorieCount += calorie
		}
	}
	return calorieList, nil
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
