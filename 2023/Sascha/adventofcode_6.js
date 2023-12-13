const sampleInput1 = [
    "Time:      7  15   30",
    "Distance:  9  40  200"
]

const [time, distance] = input;
const times = time.split(":")[1].trim().split(/[ ]+/).map(Number);
const distances = distance.split(":")[1].trim().split(/[ ]+/).map(Number);
const races = times.map((time, index) => {
    return {
        time: time,
        record: distances[index]
    }
});

let result1 = 1;
for (const race of races) {
    let possibleWins = 0;
    for (let i = 1; i < race.time; i++) {
        if ((race.time - i) * i > race.record) possibleWins++;
    }
    result1 *= possibleWins;
}

const bigRaceTime = parseInt(times.join(""));
const bigRaceRecord = parseInt(distances.join(""));
let possibleBigWins = 0;
for (let i = 1; i < bigRaceTime; i++) {
    if ((bigRaceTime - i) * i > bigRaceRecord) possibleBigWins++;
}
const result2 = possibleBigWins;

console.log("First Solution", result1)
console.log("Second Solution", result2)