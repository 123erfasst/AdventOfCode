const input = [];

const sampleInput = [
    "..F7.",
    ".FJ|.",
    "SJ.L7",
    "|F--J",
    "LJ..."
];

function Grid(input) {
    this.grid = input.map((row, indexRow) => row.split('').map((cell, indexCell) => new Tile(indexCell, indexRow, cell)))
}

function Tile(x, y, type, isStart) {
    this.x = x;
    this.y = y;
    this.type = type;
    this.isStart = isStart || false;
}

Grid.prototype.getStartTile = function() {
    for (let y = 0; y < this.grid.length; y++) {
        const row = this.grid[y];
        for (let x = 0; x < row.length; x++) {
            if (row[x].type === 'S' || row[x].isStart) {
                row[x] = new Tile(row[x].x, row[x].y, row[x].guessType(this.grid), true)
                return row[x];
            }
        }
    }
}

Tile.prototype.getNeighbours = function(grid) {
    switch (this.type) {
        case '|': return [this.y > 0 ? grid[this.y - 1][this.x] : null, grid[this.y + 1][this.x]];
        case '-': return [this.x > 0 ? grid[this.y][this.x - 1] : null, grid[this.y][this.x + 1]];
        case 'L': return [this.y > 0 ? grid[this.y - 1][this.x] : null, grid[this.y][this.x + 1]];
        case 'J': return [this.y > 0 ? grid[this.y - 1][this.x] : null, grid[this.y][this.x - 1]];
        case '7': return [grid[this.y + 1][this.x], grid[this.y][this.x - 1]];
        case 'F': return [grid[this.y + 1][this.x], grid[this.y][this.x + 1]];
        default: return [];
    }
}

Tile.prototype.getNextTile = function(previous, grid) {
    const neighbours = this.getNeighbours(grid);
    return neighbours.find(tile => tile !== previous);
}

Tile.prototype.guessType = function(grid) {
    const possibleTypesMap = [
        {
            type: '|',
            allowedFirst: ['|', 'L', 'J'],
            allowedSecond: ['|', '7', 'F']
        },
        {
            type: '-',
            allowedFirst: ['-', 'L', 'F'],
            allowedSecond: ['-', 'J', '7']
        },
        {
            type: 'L',
            allowedFirst: ['|', '7', 'F'],
            allowedSecond: ['-', 'J', '7']
        },
        {
            type: 'J',
            allowedFirst: ['|', '7', 'F'],
            allowedSecond: ['-', 'L', 'F']
        },
        {
            type: '7',
            allowedFirst: ['|', 'L', 'J'],
            allowedSecond: ['-', 'L', 'F']
        },
        {
            type: 'F',
            allowedFirst: ['|', 'L', 'J'],
            allowedSecond: ['-', 'J', '7']
        }
    ]
    for (let i = 0; i < possibleTypesMap.length; i++) {
        const newTile = new Tile(this.x, this.y, possibleTypesMap[i].type);
        const [first, second] = newTile.getNeighbours(grid);
        if (first && possibleTypesMap[i].allowedFirst.includes(first.type) && second && possibleTypesMap[i].allowedSecond.includes(second.type)) {
            return possibleTypesMap[i].type;
        }
    }
    return '.';
}

Grid.prototype.createCircle = function(startTile) {
    let [previous] = startTile.getNeighbours(this.grid);
    
    const result = [startTile];
    let current = startTile;
    do {
        const oldPrevious = previous;
        previous = current;
        current = current.getNextTile(oldPrevious, this.grid);
        if (!result.includes(current)) result.push(current);
    } while (current.x !== startTile.x || current.y !== startTile.y);

    return result;
}

const grid = new Grid(input);
const result1 = grid.createCircle(grid.getStartTile()).length / 2;

Tile.prototype.isBorder = function() {
    return this.type === '|' || this.type === 'F' || this.type === '7';
}

Grid.prototype.getEnclosedTilesInCircle = function(circle) {
    let count = 0;
    let open = false;
    for (row of this.grid) {
        for (tile of row) {
            if (circle.includes(tile)) {
                if (tile.isBorder()) {
                    open = !open;
                }
            } else if (open) {
                count++;
            }
        }
    }
    return count;
}

const result2 = grid.getEnclosedTilesInCircle(grid.createCircle(grid.getStartTile()));

console.log("First Solution", result1)
console.log("Second Solution", result2)