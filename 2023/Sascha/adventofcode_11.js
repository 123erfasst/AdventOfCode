const input = [];

const sampleInput = [
    "...#......",
    ".......#..",
    "#.........",
    "..........",
    "......#...",
    ".#........",
    ".........#",
    "..........",
    ".......#..",
    "#...#....."
]

function Universe(input) {
    this.grid = input.map((row) => row.split('').map(x => x === "." ? 1 : x));
}

Universe.prototype.expand = function(expandFactor) {
    const emptyRows = this.findEmptyRows().reverse();
    const emptyColumns = this.findEmptyColumns().reverse();

    for (const emptyRow of emptyRows) {
        this.grid[emptyRow] = this.grid[emptyRow].map(x => expandFactor);
    }

    for (const emptyColumn of emptyColumns) {
        for (const row of this.grid) {
            row[emptyColumn] = expandFactor;
        }
    }
}

Universe.prototype.findEmptyRows = function() {
    const emptyRows = [];
    for (let y = 0; y < this.grid.length; y++) {
        const row = this.grid[y];
        if (row.every((cell) => cell === 1)) {
            emptyRows.push(y);
        }
    }
    return emptyRows;
}

Universe.prototype.findEmptyColumns = function() {
    const emptyColumns = [];
    for (let x = 0; x < this.grid[0].length; x++) {
        const column = this.grid.map((row) => row[x]);
        if (column.every((cell) => cell === 1)) {
            emptyColumns.push(x);
        }
    }
    return emptyColumns;
}

Universe.prototype.findGalaxies = function() {
    const galaxies = [];
    for (let y = 0; y < this.grid.length; y++) {
        const row = this.grid[y];
        for (let x = 0; x < row.length; x++) {
            if (row[x] === '#') {
                galaxies.push(new Galaxy(x, y));
            }
        }
    }
    return galaxies;
}

function Galaxy(x, y) {
    this.x = x;
    this.y = y;
}

Galaxy.prototype.findShortestPath = function(universe, galaxy) {
    let stepsx = 0;
    if (this.x > galaxy.x) {
        let position = this.x;
        while (position > galaxy.x) {
            position--;
            stepsx += isNaN(universe.grid[this.y][position]) ? 1 : universe.grid[this.y][position];
        }
    } else if (this.x < galaxy.x) {
        let position = this.x;
        while (position < galaxy.x) {
            position++;
            stepsx += isNaN(universe.grid[this.y][position]) ? 1 : universe.grid[this.y][position];
        }
    }

    let stepsy = 0;
    if (this.y > galaxy.y) {
        let position = this.y;
        while (position > galaxy.y) {
            position--;
            stepsy += isNaN(universe.grid[position][this.x]) ? 1 : universe.grid[position][this.x];
        }
    } else if (this.y < galaxy.y) {
        let position = this.y;
        while (position < galaxy.y) {
            position++;
            stepsy += isNaN(universe.grid[position][this.x]) ? 1 : universe.grid[position][this.x];
        }
    }

    return stepsx + stepsy;

}

Universe.prototype.findGalaxyPermutations = function() {
    const galaxies = this.findGalaxies();
    const permutations = [];
    for (let i = 0; i < galaxies.length; i++) {
        const galaxy = galaxies[i];
        for (let j = i + 1; j < galaxies.length; j++) {
            const otherGalaxy = galaxies[j];
            permutations.push([galaxy, otherGalaxy]);
        }
    }
    return permutations;
}

const universe1 = new Universe(input);
universe1.expand(2);

const result1 = universe1.findGalaxyPermutations().map((permutation) => permutation[0].findShortestPath(universe1, permutation[1])).reduce((a, b) => a + b, 0);

const universe2 = new Universe(input);
universe2.expand(1000000);

const result2 = universe2.findGalaxyPermutations().map((permutation) => permutation[0].findShortestPath(universe2, permutation[1])).reduce((a, b) => a + b, 0);;

console.log("First Solution", result1)
console.log("Second Solution", result2)