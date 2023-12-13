import 'package:collection/collection.dart';
import 'dart:math';
import 'aoc/helper.dart';

String input = "";

bool isNumeric(String str) {
  if (str == null) {
    return false;
  }
  return double.tryParse(str) != null;
}

int firstPart() {
  var numbers = input.split('\n').map((e) => e.split(':')[1].split('|'));
  var winnigNumbers = numbers
      .map((e) => e.first.split(' ').where((e) => isNumeric(e)))
      .toList();
  return numbers
      .mapIndexed((i, e) => e.last
          .split(' ')
          .where((e) => isNumeric(e) && winnigNumbers[i].contains(e))
          .length)
      .map((e) => e > 2 ? pow(2, e - 1) : e)
      .reduce((x, y) => x + y)
      .toInt();
}

int getNumbers(List<List<int>> winning, int value, List<int> gameIds) {
  if (gameIds.isEmpty) {
    return value;
  }
  return getNumbers(winning, value + gameIds.length,
      gameIds.map((e) => winning[e - 1]).flattened.toList());
}

int secondPart() {
  var numbers =
      input.split('\n').map((e) => e.split(':')[1].split('|')).toList();
  var winnigNumbers = numbers
      .map((e) => e.first.split(' ').where((e) => isNumeric(e)).toList())
      .toList();

  var scratchcards = numbers
      .map((e) => e.last.split(' ').where((e) => isNumeric(e)).toList())
      .toList();

  var gameIds = scratchcards.mapIndexed((i, e) => i + 1);
  var games = gameIds.map((e) => scratchcards[e - 1]);
  var winning = games
      .mapIndexed((i, g) => g
          .where((e) => winnigNumbers[scratchcards.indexOf(g)].contains(e))
          .length)
      .mapIndexed<List<int>>(
          (i, e) => (e >= 1 ? [for (var n = 1; n <= e + 1; n++) i + n] : []))
      .map((e) => e.skip(1).toList())
      .toList();

  return getNumbers(winning, 0, gameIds.toList());
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
