const input = [];

const sampleInput1 = [
    "1abc2",
    "pqr3stu8vwx",
    "a1b2c3d4e5f",
    "treb7uchet"
]
var result1 = 0;
for (var i = 0; i < input.length; i++) {
    var split = input[i].split('');
    var num1 = null;
    var num2 = null;
    for (var j = 0; j < split.length; j++) {
        if (!num1 && !isNaN(split[j])) {
            num1 = parseInt(split[j]);
        } else if (!isNaN(split[j])) {
            num2 = parseInt(split[j]);
        }
    }
    if (!num2) num2 = num1;
    result1 += parseInt("" + num1 + num2)
}

const sampleInput2 = [
    "two1nine",
    "eightwothree",
    "abcone2threexyz",
    "xtwone3four",
    "4nineeightseven2",
    "zoneight234",
    "7pqrstsixteen",
]
let result2 = 0;
const valids = [
    "one",
    "two",
    "three",
    "four",
    "five",
    "six",
    "seven",
    "eight",
    "nine",
    "1",
    "2",
    "3",
    "4",
    "5",
    "6",
    "7",
    "8",
    "9"
];

const numberMap = {
    "one": "1",
    "two": "2",
    "three": "3",
    "four": "4",
    "five": "5",
    "six": "6",
    "seven": "7",
    "eight": "8",
    "nine": "9",
}

const getFirstNumber = (input) => {
    let currentIdx = -1;
    let currentValid;
    for (let valid of valids) {
        const idx = input.indexOf(valid);
        if (idx > -1 && (idx < currentIdx || currentIdx === -1)) {
            currentIdx = idx;
            let res = valid;
            if (isNaN(valid)) {
                res = numberMap[valid];
            }
            currentValid = res;
        }
    }
    return currentValid;
}

const getLastNumber = (input) => {
    let currentIdx = -1;
    let currentValid;
    for (let valid of valids) {
        const idx = input.lastIndexOf(valid);
        if (idx > -1 && (idx > currentIdx || currentIdx === -1)) {
            currentIdx = idx;
            let res = valid;
            if (isNaN(valid)) {
                res = numberMap[valid];
            }
            currentValid = res;
        }
    }
    return currentValid;
}

for (let val of input) {
    let num1 = getFirstNumber(val);
    let num2 = getLastNumber(val);
    if (num1 && num2) {
        result2 += parseInt(num1 + num2);
    }
}

console.log("First Solution", result1)
console.log("Second Solution", result2)