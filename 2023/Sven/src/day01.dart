import 'aoc/helper.dart';

String input = "";

bool isNumeric(String str) {
  if (str == null) {
    return false;
  }
  return double.tryParse(str) != null;
}

int firstPart(List<String> lines) {
  return lines
      .map((e) => e.split('').where((x) => isNumeric(x)).toList())
      .where((e) => e.isNotEmpty)
      .map((e) => int.parse(([e.first, e.last]).join("")))
      .reduce((a, b) => a + b);
}

int secondPart() {
  final numbers = {
    "one": "o1e",
    "two": "t2o",
    "three": "t3e",
    "four": "f4r",
    "five": "f5e",
    "six": "s6x",
    "seven": "s7n",
    "eight": "e8t",
    "nine": "n9e"
  };

  var lines = input
      .split('\n')
      .map((line) => numbers.keys.fold(
          line,
          (previousValue, element) => !previousValue.contains(element)
              ? previousValue
              : previousValue.replaceAll(element, numbers[element].toString())))
      .toList();
  return firstPart(lines);
}

Future<void> main(List<String> args) async {
  if (args.isEmpty) {
    print("Expected year");
    return;
  } else if (args.length < 2) {
    print("Expected day");
    return;
  }

  int year = int.parse(args[0]);
  int day = int.parse(args[1]);

  input = await getInput(year, day);

  print('Advent of code $year day $day');
  print('First part result: ${firstPart(input.split('\n'))}');
  print('Second part result: ${secondPart()}');
}
