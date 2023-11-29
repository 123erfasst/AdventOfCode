import 'aoc/helper.dart';

String input = "";

String firstPart() {
  return "unsolved";
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
