##Part 1

def GetNumber(line):
    res = [x for x in line if x.isdigit()]
    return int(res[0]) * 10 + int(res[- 1])

f = open("./input.txt", "r")

Lines = f.readlines()
result = 0

resultHuebsch = sum([GetNumber(line) for line in Lines])

for line in Lines:
    numbers = ""
    for i, c in enumerate(line):
        if c.isdigit():
            numbers += c
    lineResult = numbers[0] + numbers[len(numbers) - 1]
    result += int(lineResult)

print("Part1result")
print(resultHuebsch)
print(result)



def GetNumberWithString(line):
    res = [(mappingDict[key], line.find(key), line.rfind(key)) for key in mappingDict if line.find(key) != -1 and line.rfind(key) != -1 ]
    low = sorted(res, key=lambda x: x[1])[0][0]
    high = sorted(res, key=lambda x: x[2])[-1][0]
    return low * 10 + high

##Part 2
f = open("./input.txt", "r")

Lines = f.readlines()

result = 0
mappingDict = {
    "one": 1,
    "1": 1,
    "two": 2,
    "2": 2,
    "three": 3,
    "3": 3,
    "four": 4,
    "4": 4,
    "five": 5,
    "5": 5,
    "six": 6,
    "6": 6,
    "seven": 7,
    "7": 7,
    "eight": 8,
    "8": 8,
    "nine": 9,
    "9": 9
}

resultHuebsch2 = sum([GetNumberWithString(line) for line in Lines])

for line in Lines:
    numbers = ""
    lowestNumber = 0
    highestNumber = 0
    startLowValue = 10000000
    startHighValue = -1
    lowestIndex = startLowValue
    highestIndex = startHighValue

    for number in mappingDict.keys():
        foundIndex = line.find(number)
        if foundIndex < lowestIndex and foundIndex != -1 and foundIndex != startLowValue:
            lowestIndex = foundIndex
            lowestNumber = mappingDict[number]

        foundIndexHigh = line.rfind(number)
        if foundIndexHigh > highestIndex and foundIndexHigh != -1 and foundIndexHigh != startHighValue:
            highestIndex = foundIndexHigh
            highestNumber = mappingDict[number]

    lineResult = lowestNumber * 10 + highestNumber
    result += int(lineResult)

print("Part2result")
print(resultHuebsch2)
print(result)
