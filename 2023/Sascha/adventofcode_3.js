const input = [];

const sampleInput = [
    "467..114..",
    "...*......",
    "..35..633.",
    "......#...",
    "617*......",
    ".....+.58.",
    "..592.....",
    "......755.",
    "...$.*....",
    ".664.598.."
]

function EngineSchematic(inp) {
    this.config = inp;
    this.rows = inp.map((row, index) => new Row(row, index));
}

EngineSchematic.prototype.getValidParts = function() {
    const validParts = [];
    for (var i = 0; i < this.rows.length; i++) {
        const rowAbove = this.rows[i - 1];
        const row = this.rows[i];
        const rowBelow = this.rows[i + 1];
        for (var j = 0; j < row.parts.length; j++) {
            const part = row.parts[j];
            if (part.isValidPart(rowAbove, row, rowBelow)) validParts.push(part);
        }
    }
    return validParts;
}

function Row(input) {
    this.parts = [];
    this.config = input;
    const re = /\d+/g;
    while((match = re.exec(input)) != null) {
        this.parts.push(new Part(match[0], match.index));
    }
}

function Part(partNumber, index) {
    this.partNumber = partNumber;
    this.index = index;
}

Part.prototype.isValidPart = function(rowAbove, row, rowBelow) {
    const len = this.partNumber.length;
    if (rowAbove) {
        for (var i = this.index - 1; i < len + this.index + 1; i++) {
            if (this.isValidAdjacent(i, rowAbove.config)) return true;
        }
    }
    if (this.index > 0 && this.isValidAdjacent(this.index - 1, row.config)) return true;
    if (this.index + len < row.config.length && this.isValidAdjacent(this.index + len, row.config)) return true;
    if (rowBelow) {
        for (var i = this.index - 1; i < len + this.index + 1; i++) {
            if (this.isValidAdjacent(i, rowBelow.config)) return true;
        }
    }
    return false;
}

Part.prototype.isValidAdjacent = function(index, row) {
    return index >= 0 && row.length > index && row[index] !== '.' && isNaN(row[index]);
}

const es = new EngineSchematic(input);

const result1 = es.getValidParts().map(x => x.partNumber).map(Number).reduce((a, b) => a + b, 0);

EngineSchematic.prototype.getGearRatios = function() {
    const gearRatios = [];
    for (var i = 0; i < this.rows.length; i++) {
        const rowAbove = this.rows[i - 1];
        const row = this.rows[i];
        const rowBelow = this.rows[i + 1];

        const possibleGears = row.getPossibleGears();

        for (var possibleGear of possibleGears) {
            const partsNextToPossibleGear = [];
            if (rowAbove) {
                partsNextToPossibleGear.push(...rowAbove.getPartsNextToPossibleGear(possibleGear));
            }
            partsNextToPossibleGear.push(...row.getPartsNextToPossibleGear(possibleGear));
            if (rowBelow) {
                partsNextToPossibleGear.push(...rowBelow.getPartsNextToPossibleGear(possibleGear));
            }
            if (partsNextToPossibleGear.length === 2) gearRatios.push(parseInt(partsNextToPossibleGear[0].partNumber) * parseInt(partsNextToPossibleGear[1].partNumber));
        }
    }
    return gearRatios;
}

Row.prototype.getPossibleGears = function() {
    const result = [];
    const re = /[*]/g;
    while((match = re.exec(this.config)) != null) {
        result.push(match.index);
    }
    return result;
}

Row.prototype.getPartsNextToPossibleGear = function(possibleGearIndex) {
    const result = [];
    for (var i = 0; i < this.parts.length; i++) {
        const part = this.parts[i];
        if (part.isAdjacentToPossibleGear(possibleGearIndex)) result.push(part);
    }
    return result;

}

Part.prototype.isAdjacentToPossibleGear = function(possibleGearIndex) {
    return possibleGearIndex >= this.index -1 && possibleGearIndex <= this.index + this.partNumber.length;
}

const result2 = es.getGearRatios().reduce((a, b) => a + b, 0);

console.log("First Solution", result1)
console.log("Second Solution", result2)