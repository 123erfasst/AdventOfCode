const fs = require("fs");

const input = fs.readFileSync("./inputs/day3.txt", "utf8");

function day3part1() {
  let match;
  const numbers = [];
  const mulRegex = /mul\((\d+),\s*(\d+)\)/g;

  while ((match = mulRegex.exec(input)) !== null) {
    const num1 = parseInt(match[1]);
    const num2 = parseInt(match[2]);
    numbers.push(num1 * num2);
  }

  console.log(
    "Result part 1:",
    numbers.reduce((pv, cv) => pv + cv, 0)
  );
}

function day3part2() {
  const tokenRegex = /mul\((\d+),\s*(\d+)\)|do\(\)|don't\(\)/g; // Danke chatgpt :>

  const tokens = input.match(tokenRegex) || [];

  let enabled = true;
  let total = 0;

  tokens.forEach((token) => {
    if (token === "do()") {
      enabled = true;
    } else if (token === "don't()") {
      enabled = false;
    } else if (token.startsWith("mul") && enabled) {
      const match = /mul\((\d+),\s*(\d+)\)/.exec(token);
      if (match) {
        const num1 = parseInt(match[1], 10);
        const num2 = parseInt(match[2], 10);
        total += num1 * num2;
      }
    }
  });

  console.log("Result part 2:", total);
}

day3part1();
day3part2();
