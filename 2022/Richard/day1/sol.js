function solution(inp) {
    const elvesWithCalories =
        inp
        .trim()
        .split('\n\n')
            .map(x =>
                x.split('\n')
                    .map(number => Number.parseInt(number))
                    .reduce((a, b) => a + b, 0))

    const caloriesSorted = elvesWithCalories.sort((a,b) => b - a);

    const mostCalories = caloriesSorted[0]
    console.log("Elf mit den meisten Kalorien hat: " + mostCalories)
    
    const topthree = caloriesSorted.slice(0, 3).reduce((a,b) => a + b,0)
    console.log("Top Drei hat: " + topthree)
}