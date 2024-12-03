const fs = require("fs");

function day1() {
  const input = fs.readFileSync("./inputs/day1.txt", "utf8");
  const lines = input.split("\n");

  const firstList = [];
  const secondList = [];

  lines.forEach((line) => {
    const [first, second] = line.trim().split(/\s+/).map(Number);
    firstList.push(first);
    secondList.push(second);
  });

  let firstListSorted = firstList.sort((a, b) => a - b);
  let secondListSorted = secondList.sort((a, b) => a - b);

  const firstPart = firstListSorted.reduce(
    (pv, cv, i) => pv + Math.abs(secondListSorted[i] - cv),
    0
  );

  const secondPart = firstListSorted.reduce(
    (pv, cv) => pv + cv * secondListSorted.filter((a) => a === cv).length,
    0
  );
  console.log("first part:", firstPart);
  console.log("second part:", secondPart);
}

day1();
