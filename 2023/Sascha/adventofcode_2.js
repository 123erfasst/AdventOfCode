const input = [];

const sampleInput = [
    "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
    "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
    "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
    "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
    "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green",
]

function Game(input) {
    this.id = parseInt(input.split(":")[0].split(" ")[1]);
    const rollInput = input.split(":")[1].split(";");
    this.rolls = rollInput.map(x => new Roll(x));
}

function Roll(input) {
    const colors = input.trim().split(", ");
    this.red = 0;
    this.green = 0;
    this.blue = 0;
    for (let color of colors) {
        if (color.indexOf("red") > -1) {
            this.red = parseInt(color.split(" ")[0]);
        }
        if (color.indexOf("green") > -1) {
            this.green = parseInt(color.split(" ")[0]);
        }
        if (color.indexOf("blue") > -1) {
            this.blue = parseInt(color.split(" ")[0]);
        }
    }
}

Game.prototype.isPossible = function(r, g, b) {
    for (let roll of this.rolls) {
        if (!roll.isPossible(r, g, b)) {
            return false;
        }
    }
    return true;
}

Roll.prototype.isPossible = function(r, g, b) {
    return this.red <= r && this.green <= g && this.blue <= b;
}

const result1 = input.reduce((acc, val) => {
    const game = new Game(val);
    return game.isPossible(12, 13, 14) ? acc + game.id : acc;
}, 0)

Game.prototype.getFewestCubes = function() {
    return {
        red: Math.max(...this.rolls.map(x => x.red)),
        green: Math.max(...this.rolls.map(x => x.green)),
        blue: Math.max(...this.rolls.map(x => x.blue)),
    }
}

const result2 = input.reduce((acc, val) => {
    const game = new Game(val);
    const fewestRolls = game.getFewestCubes();
    return acc + fewestRolls.red * fewestRolls.green * fewestRolls.blue;
}, 0);

console.log("First Solution", result1)
console.log("Second Solution", result2)