import 'package:collection/collection.dart';
import 'package:test/expect.dart';

import 'aoc/helper.dart';

String input = "";

int firstPart() {
  var lines = input.split('\n');

  return lines
      .map((e) => e.split(":")[1].split(';').map((g) => g.split(",").map((e) =>
          e.endsWith(" red") && int.parse(e.split(' ')[1]) <= 12 ||
          e.endsWith(" green") && int.parse(e.split(' ')[1]) <= 13 ||
          e.endsWith(" blue") && int.parse(e.split(' ')[1]) <= 14)))
      .mapIndexed((i, e) =>
          ![...e.map((e) => !e.contains(false))].contains(false) ? i + 1 : 0)
      .reduce((value, element) => value + element);
}

int secondPart() {
  var lines = input.split('\n');

  return lines
      .map((e) => e.split(":")[1].split(';').map((g) =>
          g.split(",").map((e) => e.split(' ').toList().skip(1).join(" "))))
      .map((e) => ["red", "green", "blue"]
          .map((c) => e
              .map((e) => e
                  .map((e) => e.endsWith(c) ? int.parse(e.split(" ")[0]) : 0)
                  .reduce((x, y) => x + y))
              .max)
          .reduce((value, element) => value * element))
      .reduce((value, element) => value + element);
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
  print('First part result: ${firstPart()}');
  print('Second part result: ${secondPart()}');
}
