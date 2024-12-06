const fs = require("fs");

const input = fs.readFileSync("./inputs/day6.txt", "utf8");

const grid = input
  .trim()
  .split("\n")
  .map((line) => line.split(""));

const directions = [
  [-1, 0],
  [0, 1],
  [1, 0],
  [0, -1],
];

function findGuardStart(grid) {
  for (let i = 0; i < grid.length; i++) {
    for (let j = 0; j < grid[i].length; j++) {
      if (grid[i][j] === "^") return [i, j];
    }
  }
  throw new Error("Guard starting position not found");
}

function simulatePatrolUniquePositions(grid, startRow, startCol) {
  let visited = new Set();
  let currentRow = startRow;
  let currentCol = startCol;
  let currentDir = 0;

  visited.add(`${currentRow},${currentCol}`);

  while (true) {
    const [dRow, dCol] = directions[currentDir];
    const nextRow = currentRow + dRow;
    const nextCol = currentCol + dCol;

    if (
      nextRow < 0 ||
      nextRow >= grid.length ||
      nextCol < 0 ||
      nextCol >= grid[0].length
    ) {
      break;
    }

    if (grid[nextRow][nextCol] === "#") {
      currentDir = (currentDir + 1) % 4;
    } else {
      currentRow = nextRow;
      currentCol = nextCol;
      visited.add(`${currentRow},${currentCol}`);
    }
  }

  return visited.size;
}

function simulatePatrolForLoop(grid, startRow, startCol) {
  let visited = new Set();
  let currentRow = startRow;
  let currentCol = startCol;
  let currentDir = 0;

  while (true) {
    const [dRow, dCol] = directions[currentDir];
    const nextRow = currentRow + dRow;
    const nextCol = currentCol + dCol;

    if (
      nextRow < 0 ||
      nextRow >= grid.length ||
      nextCol < 0 ||
      nextCol >= grid[0].length
    ) {
      return false;
    }

    if (grid[nextRow][nextCol] === "#") {
      currentDir = (currentDir + 1) % 4;
    } else {
      currentRow = nextRow;
      currentCol = nextCol;

      const key = `${currentRow},${currentCol},${currentDir}`;
      if (visited.has(key)) return true;
      visited.add(key);
    }
  }
}

function findLoopObstructionPositions(grid) {
  const [startRow, startCol] = findGuardStart(grid);
  const loopPositions = [];

  for (let i = 0; i < grid.length; i++) {
    for (let j = 0; j < grid[i].length; j++) {
      if (grid[i][j] !== "." || (i === startRow && j === startCol)) continue;

      grid[i][j] = "#";

      if (simulatePatrolForLoop(grid, startRow, startCol)) {
        loopPositions.push([i, j]);
      }

      grid[i][j] = ".";
    }
  }

  return loopPositions;
}

function day6() {
  const [startRow, startCol] = findGuardStart(grid);
  const part1Result = simulatePatrolUniquePositions(grid, startRow, startCol);

  console.log("Result part 1:", part1Result);

  const loopPositions = findLoopObstructionPositions(grid);
  console.log("Result part 2:", loopPositions.length);
}

day6();
