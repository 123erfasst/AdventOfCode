const input = [];

const sampleInput1 = [
    "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53",
    "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19",
    "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1",
    "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83",
    "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36",
    "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"
]

let result1 = 0;
for (let card of input) {
    let cardValue = 0;
    const numbers = card.split(":")[1]
    const [first, second] = numbers.split("|")
    const winningNumbers = first.trim().split(" ").filter(x => x.length > 0).map(x => parseInt(x.trim()))
    const myNumbers = second.trim().split(" ").filter(x => x.length > 0).map(x => parseInt(x.trim()))
    const myWinningNumbers = winningNumbers.filter(x => myNumbers.includes(x));
    for (var i = 0; i < myWinningNumbers.length; i++) {
        if (cardValue === 0) {
            cardValue = 1;
        } else {
            cardValue = cardValue * 2;
        }
    }
    result1 += cardValue;
}

const sampleInput2 = [
    "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53",
    "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19",
    "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1",
    "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83",
    "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36",
    "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"
]

var mappedInput = input.map(function (input, index) {
    return {
        index: index,
        input: input
    }
});

let totalCards = input.length;
(function traverse(cards) {
    for (var i = 0; i < cards.length; i++) {
        const card = cards[i].input;
        const numbers = card.split(":")[1]
        const [first, second] = numbers.split("|")
        const winningNumbers = first.trim().split(" ").filter(x => x.length > 0).map(x => parseInt(x.trim()))
        const myNumbers = second.trim().split(" ").filter(x => x.length > 0).map(x => parseInt(x.trim()))
        const winCount = winningNumbers.filter(x => myNumbers.includes(x)).length;
        const additionalCards = [];
        for (var j = 1; j <= winCount; j++) {
            var nextCard = mappedInput[cards[i].index + j];
            additionalCards.push(nextCard);
            totalCards++;
        }
        traverse(additionalCards)
    }
})(mappedInput);
const result2 = totalCards;


console.log("First Solution", result1)
console.log("Second Solution", result2)