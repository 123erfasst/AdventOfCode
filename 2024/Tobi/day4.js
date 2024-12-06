const fs = require("fs");

const input = fs.readFileSync("./inputs/day4.txt", "utf8");
const directions = [
  [0, 1],
  [1, 0],
  [1, 1],
  [1, -1],
  [0, -1],
  [-1, 0],
  [-1, -1],
  [-1, 1],
];

function day4() {
  const xmasGrid = input
    .trim()
    .split("\n")
    .map((line) => line.split(""));
  const xmas = "XMAS";

  const numRows = xmasGrid.length;
  const numCols = xmasGrid[0].length;
  const wordLength = xmas.length;

  const isWithinBounds = (row, col) =>
    row >= 0 && row < numRows && col >= 0 && col < numCols;

  const searchWordInDirection = (
    startRow,
    startCol,
    rowIncrement,
    colIncrement
  ) => {
    for (let index = 0; index < wordLength; index++) {
      const currentRow = startRow + rowIncrement * index;
      const currentCol = startCol + colIncrement * index;
      if (
        !isWithinBounds(currentRow, currentCol) ||
        xmasGrid[currentRow][currentCol] !== xmas[index]
      ) {
        return false;
      }
    }
    return true;
  };

  const result = xmasGrid.reduce(
    (gridTotal, currentRow, rowIndex) =>
      gridTotal +
      currentRow.reduce(
        (rowTotal, cell, colIndex) =>
          rowTotal +
          (cell === xmas[0]
            ? directions.filter(([rowIncrement, colIncrement]) =>
                searchWordInDirection(
                  rowIndex,
                  colIndex,
                  rowIncrement,
                  colIncrement
                )
              ).length
            : 0),
        0
      ),
    0
  );

  console.log("Result part 1:", result);
}

day4();
