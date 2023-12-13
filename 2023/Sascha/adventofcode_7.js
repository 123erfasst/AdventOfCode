const sampleInput1 = [
  "32T3K 765",
  "T55J5 684",
  "KK677 28",
  "KTJJT 220",
  "QQQJA 483",
];

function Hand(input) {
  this.cards = input.split("");
}

Hand.prototype.hasFiveOfAKind = function (withJoker) {
  if (withJoker) {
    return this.getBestHand().hasFiveOfAKind();
  }
  return this.cards.every((card) => card === this.cards[0]);
};
Hand.prototype.hasFourOfAKind = function (withJoker) {
  if (withJoker) {
    return this.getBestHand().hasFourOfAKind();
  }
  return this.cards.some(
    (card) => this.cards.filter((c) => c === card).length === 4
  );
};
Hand.prototype.hasFullHouse = function (withJoker) {
  if (withJoker) {
    return this.getBestHand().hasFullHouse();
  }
  return this.hasThreeOfAKind() && this.hasOnePair();
};
Hand.prototype.hasThreeOfAKind = function (withJoker) {
  if (withJoker) {
    return this.getBestHand().hasThreeOfAKind();
  }
  return this.cards.some(
    (card) => this.cards.filter((c) => c === card).length === 3
  );
};
Hand.prototype.hasTwoPairs = function (withJoker) {
  if (withJoker) {
    return this.getBestHand().hasTwoPairs();
  }
  let firstPair = this.cards.filter(
    (card) => this.cards.filter((c) => c === card).length === 2
  )[0];
  if (firstPair === undefined) return false;
  let secondPair = this.cards.filter(
    (card) =>
      card !== firstPair && this.cards.filter((c) => c === card).length === 2
  )[0];
  return secondPair !== undefined;
};
Hand.prototype.hasOnePair = function (withJoker) {
  if (withJoker) {
    return this.getBestHand().hasOnePair();
  }
  return this.cards.some(
    (card) => this.cards.filter((c) => c === card).length === 2
  );
};
Hand.prototype.hasHighCard = function (withJoker) {
  if (withJoker) {
    return this.getBestHand().hasHighCard();
  }
  return this.cards.every(
    (card) => this.cards.filter((c) => c === card).length === 1
  );
};
Hand.prototype.compareTo = function (otherHand, withJoker) {
  if (this.hasFiveOfAKind(withJoker) && !otherHand.hasFiveOfAKind(withJoker))
    return 1;
  if (!this.hasFiveOfAKind(withJoker) && otherHand.hasFiveOfAKind(withJoker))
    return -1;
  if (this.hasFourOfAKind(withJoker) && !otherHand.hasFourOfAKind(withJoker))
    return 1;
  if (!this.hasFourOfAKind(withJoker) && otherHand.hasFourOfAKind(withJoker))
    return -1;
  if (this.hasFullHouse(withJoker) && !otherHand.hasFullHouse(withJoker))
    return 1;
  if (!this.hasFullHouse(withJoker) && otherHand.hasFullHouse(withJoker))
    return -1;
  if (this.hasThreeOfAKind(withJoker) && !otherHand.hasThreeOfAKind(withJoker))
    return 1;
  if (!this.hasThreeOfAKind(withJoker) && otherHand.hasThreeOfAKind(withJoker))
    return -1;
  if (this.hasTwoPairs(withJoker) && !otherHand.hasTwoPairs(withJoker))
    return 1;
  if (!this.hasTwoPairs(withJoker) && otherHand.hasTwoPairs(withJoker))
    return -1;
  if (this.hasOnePair(withJoker) && !otherHand.hasOnePair(withJoker)) return 1;
  if (!this.hasOnePair(withJoker) && otherHand.hasOnePair(withJoker)) return -1;

  for (let i = 0; i < this.cards.length; i++) {
    if (this.cards[i] === "A" && otherHand.cards[i] !== "A") return 1;
    if (this.cards[i] !== "A" && otherHand.cards[i] === "A") return -1;
    if (this.cards[i] === "K" && otherHand.cards[i] !== "K") return 1;
    if (this.cards[i] !== "K" && otherHand.cards[i] === "K") return -1;
    if (this.cards[i] === "Q" && otherHand.cards[i] !== "Q") return 1;
    if (this.cards[i] !== "Q" && otherHand.cards[i] === "Q") return -1;
    if (!withJoker) {
      if (this.cards[i] === "J" && otherHand.cards[i] !== "J") return 1;
      if (this.cards[i] !== "J" && otherHand.cards[i] === "J") return -1;
    }
    if (this.cards[i] === "T" && otherHand.cards[i] !== "T") return 1;
    if (this.cards[i] !== "T" && otherHand.cards[i] === "T") return -1;
    for (let j = 9; j >= 2; j--) {
      if (this.cards[i] === j.toString() && otherHand.cards[i] !== j.toString())
        return 1;
      if (this.cards[i] !== j.toString() && otherHand.cards[i] === j.toString())
        return -1;
    }
    if (withJoker) {
      if (this.cards[i] === "J" && otherHand.cards[i] !== "J") return 1;
      if (this.cards[i] !== "J" && otherHand.cards[i] === "J") return -1;
    }
  }

  return 0;
};

function Play(input) {
  const [cards, bidStr] = input.split(" ");
  this.Hand = new Hand(cards);
  this.Bid = parseInt(bidStr);
}

const plays = input
  .map((input) => new Play(input))
  .sort((a, b) => a.Hand.compareTo(b.Hand));

let result1 = 0;
for (let i = 1; i <= plays.length; i++) {
  result1 += plays[i - 1].Bid * i;
}

Hand.prototype.getBestHand = function () {
  const getMostFrequentChar = function (str) {
    let max = 0;
    let maxChar = "";
    str.split("").forEach(function (char) {
      if (str.split(char).length > max) {
        max = str.split(char).length;
        maxChar = char;
      }
    });
    return maxChar;
  };
  return new Hand(
    this.cards
      .join("")
      .replaceAll(
        "J",
        getMostFrequentChar(this.cards.filter((x) => x !== "J").join(""))
      )
  );
};

const plays2 = input
  .map((inp) => new Play(inp))
  .sort((a, b) => a.Hand.compareTo(b.Hand, true));

let result2 = 0;
for (let i = 1; i <= plays2.length; i++) {
  result2 += plays2[i - 1].Bid * i;
}

console.log("First Solution", result1);
console.log("Second Solution", result2);
