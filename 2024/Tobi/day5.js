const fs = require("fs");

const input = fs.readFileSync("./inputs/day5.txt", "utf8");

function day5() {
  const normalizedInput = input.replace(/\r\n/g, "\n");
  const sections = normalizedInput.split("\n\n");

  const rules = sections[0].split("\n").map((rule) => {
    const [before, after] = rule.split("|").map(Number);
    return { before, after };
  });

  const updates = sections[1]
    .split("\n")
    .map((update) => update.split(",").map(Number));

  const isUpdateValid = (update) => {
    const indexMap = new Map();
    update.forEach((page, index) => indexMap.set(page, index));
    return rules.every(
      ({ before, after }) =>
        !(
          indexMap.has(before) &&
          indexMap.has(after) &&
          indexMap.get(before) > indexMap.get(after)
        )
    );
  };

  const getMiddlePage = (update) => update[Math.floor(update.length / 2)];

  const fixUpdate = (update) => {
    const dependencyGraph = new Map();
    const inDegree = new Map();

    update.forEach((page) => {
      dependencyGraph.set(page, []);
      inDegree.set(page, 0);
    });

    rules.forEach(({ before, after }) => {
      if (update.includes(before) && update.includes(after)) {
        dependencyGraph.get(before).push(after);
        inDegree.set(after, inDegree.get(after) + 1);
      }
    });

    const sorted = [];
    const queue = Array.from(inDegree.keys()).filter(
      (page) => inDegree.get(page) === 0
    );

    while (queue.length > 0) {
      const current = queue.shift();
      sorted.push(current);

      dependencyGraph.get(current).forEach((neighbor) => {
        inDegree.set(neighbor, inDegree.get(neighbor) - 1);
        if (inDegree.get(neighbor) === 0) {
          queue.push(neighbor);
        }
      });
    }

    return sorted;
  };

  let resPart1 = 0;
  let resPart2 = 0;

  for (const update of updates) {
    if (isUpdateValid(update)) {
      resPart1 += getMiddlePage(update);
    } else {
      const fixedUpdate = fixUpdate(update);
      resPart2 += getMiddlePage(fixedUpdate);
    }
  }

  console.log("Result part 1:", resPart1);
  console.log("Result part 2:", resPart2);
}

day5();
