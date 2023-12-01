import 'aoc/helper.dart';

String input = "";

bool isNumeric(String str) {
  if (str == null) {
    return false;
  }
  return double.tryParse(str) != null;
}

int firstPart() {
  var lines = input.split('\n');
  var digits = lines
      .map((e) => e.split('').where((x) => isNumeric(x)).toList())
      .where((e) => e.isNotEmpty)
      .map((e) => ([e.first, e.last]).join(""));
  return digits.fold(0, (p, c) => int.parse(p.toString()) + int.parse(c));
}

String secondPart() {
  return "unsolved";
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
