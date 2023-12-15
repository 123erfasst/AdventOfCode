const input = []

const sampleInput1 = [
    "0 3 6 9 12 15",
    "1 3 6 10 15 21",
    "10 13 16 21 30 45"
];

function Oasis(input) {
    this.values = input.split(" ").map(Number);
}

Oasis.prototype.getSteps = function() {
    const areAllZeroes = (arr) => arr.every((v) => v === 0);
    const steps = [this.values];
    let currentStep = this.values;
    do {
        const nextStep = [];
        for (let i = 0; i < currentStep.length - 1; i++) {
            const left = currentStep[i];
            const right = currentStep[i + 1];
            nextStep.push(right - left);
        }
        steps.push(nextStep);
        currentStep = nextStep;
    } while(!areAllZeroes(currentStep));

    return steps;
}

Oasis.prototype.predict = function() {
    const steps = this.getSteps();

    let newVal = 0;
    for (var i = steps.length - 2; i >= 0; i--) {
        newVal = steps[i][steps[i].length - 1] + newVal;
    }
    return newVal;
}

var oas = input.map((inp) => new Oasis(inp));

const result1 = oas.map((oasis) => oasis.predict()).reduce((a, b) => a + b, 0);

Oasis.prototype.predictPrevious = function() {
    const steps = this.getSteps();

    let newVal = 0;
    for (var i = steps.length - 2; i >= 0; i--) {
        newVal = steps[i][0] - newVal;
    }
    return newVal;
}

const result2 = oas.map((oasis => oasis.predictPrevious())).reduce((a, b) => a + b, 0);

console.log("First Solution", result1)
console.log("Second Solution", result2)