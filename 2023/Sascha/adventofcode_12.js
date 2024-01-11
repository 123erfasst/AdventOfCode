const input = [];

const sampleInput = [
    "???.### 1,1,3",
    ".??..??...?##. 1,1,3",
    "?#?#?#?#?#?#?#? 1,3,1,6",
    "????.#...#... 4,1,1",
    "????.######..#####. 1,6,5",
    "?###???????? 3,2,1"
]

const cache = new Map();

function Record(input) {
    const [pattern, check] = input.split(" ");
    this.pattern = pattern;
    this.check = check.split(",").map(Number);
}

Record.prototype.countPossibleArrangements = function(toCheck = this.pattern, check = this.check) {
    if (toCheck === "") {
        return check.length === 0 ? 1 : 0;
    }

    if (check.length === 0) {
        return toCheck.includes("#") ? 0 : 1;
    }

    const key = toCheck + "_" + check.join("_");

    if (cache.has(key)) {
        return cache.get(key);
    }

    let result = 0;

    if (toCheck.charAt(0) === '.' || toCheck.charAt(0) === '?') {
        result += this.countPossibleArrangements(toCheck.substring(1), check);
    }

    if (toCheck.charAt(0) === '#' || toCheck.charAt(0) === '?') {
        if (check[0] <= toCheck.length && !toCheck.substring(0, check[0]).includes('.') && (check[0] === toCheck.length || toCheck.charAt(check[0]) !== '#')) {
            result += this.countPossibleArrangements(toCheck.substring(check[0] + 1), check.slice(1));
        }
    }

    cache.set(key, result);

    return result;
}

const result1 = input.map(inp => new Record(inp)).map(record => record.countPossibleArrangements()).reduce((a, b) => a + b, 0);

function unfoldInput(input) {
    return input.map(x => {
        const [pattern, check] = x.split(" ");
        return Array(5).fill(pattern).join("?") + " " + Array(5).fill(check).join(",");
    })
}

const result2 = unfoldInput(input).map((inp) => new Record(inp)).map((record) => record.countPossibleArrangements()).reduce((a, b) => a + b, 0);

console.log("First Solution", result1)
console.log("Second Solution", result2)