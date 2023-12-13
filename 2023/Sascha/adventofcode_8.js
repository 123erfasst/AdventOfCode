const sampleInput1 = [
    "LLR",
    "",
    "AAA = (BBB, BBB)",
    "BBB = (AAA, ZZZ)",
    "ZZZ = (ZZZ, ZZZ)"
];

function Instructions(input) {
    const steps = input.slice(2).map(x => new Step(x));
    this.Steps = steps.reduce((obj, item) => Object.assign(obj, { [item.Key]: item }), {});
    this.Moves = input[0].split("");
}

function Step(input) {
    const [k, v] = input.split(" = ");
    this.Key = k;
    this.L = v.substring(1, 4);
    this.R = v.substring(6, 9);
}

Step.prototype.getNextStep = function(direction, map) {
    return map[this[direction]];
}

Instructions.prototype.calculateTotalSteps = function() {
    let totalSteps = 0;
    let currentStep = this.Steps["AAA"];
    while(currentStep.Key !== "ZZZ") {
        for (const move of this.Moves) {
            currentStep = currentStep.getNextStep(move, this.Steps);
            totalSteps++;
            if (currentStep.Key === "ZZZ") return totalSteps;
        }
    }
    return -1;
}

const instructions = new Instructions(input);

const sampleInput2 = [
    "LR",
    "",
    "11A = (11B, XXX)",
    "11B = (XXX, 11Z)",
    "11Z = (11B, XXX)",
    "22A = (22B, XXX)",
    "22B = (22C, 22C)",
    "22C = (22Z, 22Z)",
    "22Z = (22B, 22B)",
    "XXX = (XXX, XXX)",
]

Instructions.prototype.getGhostStartingSteps = function() {
    return Object.keys(this.Steps).filter(x => x.endsWith("A")).map(x => this.Steps[x]);
}

Instructions.prototype.calculateTotalGhostSteps = function() {
    let totalGhostSteps = [];
    let ghostSteps = this.getGhostStartingSteps();

    for (const ghostStep of ghostSteps) {
        let totalSteps = 0;
        let currentStep = ghostStep;
        let stop = false;
        while(!currentStep.Key.endsWith("Z")) {
            for (const move of this.Moves) {
                currentStep = currentStep.getNextStep(move, this.Steps);
                totalSteps++;
                if (currentStep.Key.endsWith("Z")) {
                    stop = true;
                    totalGhostSteps.push(totalSteps)
                    break;
                };
            }
            if (stop) break;
        }
    }

    const greatestCommonDivisor = (a, b) => {
        if (b === 0) return a;
        return greatestCommonDivisor(b, a % b);
    }

    const leastCommonMultiple = (input) => input.reduce((a, b) => a * b / greatestCommonDivisor(a, b));

    return leastCommonMultiple(totalGhostSteps);
}

const result1 = instructions.calculateTotalSteps();
const result2 = instructions.calculateTotalGhostSteps();

console.log("First Solution", result1)
console.log("Second Solution", result2)