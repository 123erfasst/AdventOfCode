const fs = require("fs");

function day2() {
  const input = fs.readFileSync("./inputs/day2.txt", "utf8");
  const reports = input.split("\n").filter((line) => line.trim() !== "");

  function isSafePart1(level) {
    const levelArr = level.split(" ").map(Number);

    const isIncreasing = levelArr.every(
      (val, i, arr) => i === 0 || val > arr[i - 1]
    );
    const isDecreasing = levelArr.every(
      (val, i, arr) => i === 0 || val < arr[i - 1]
    );

    if (!isIncreasing && !isDecreasing) return false;

    for (let i = 0; i < levelArr.length - 1; i++) {
      const diff = Math.abs(levelArr[i] - levelArr[i + 1]);
      if (diff < 1 || diff > 3) return false;
    }

    return true;
  }

  function canBeMadeSafe(level) {
    const levelArr = level.split(" ").map(Number);

    for (let i = 0; i < levelArr.length; i++) {
      const modifiedArr = [...levelArr.slice(0, i), ...levelArr.slice(i + 1)];
      if (isSafePart1(modifiedArr.join(" "))) return true;
    }
    return false;
  }

  const part1SafeCount = reports.reduce(
    (safeCount, report) => (isSafePart1(report) ? safeCount + 1 : safeCount),
    0
  );

  const part2SafeCount = reports.reduce(
    (safeCount, report) =>
      isSafePart1(report) || canBeMadeSafe(report) ? safeCount + 1 : safeCount,
    0
  );

  console.log("Part 1:", part1SafeCount);
  console.log("Part 2:", part2SafeCount);
}

day2();
