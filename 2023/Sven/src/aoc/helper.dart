import 'dart:io';
import 'package:http/http.dart' as http;

Map<String, String> getCookieHeaders() {
  var env = File('.env');
  if (!env.existsSync()) {
    throw Exception('Session key from https://adventofcode.com/ required');
  }
  var contents = env.readAsStringSync().trim();
  return {'Cookie': contents};
}

Future<String> getInput(int year, int day) async {
  String filePath = 'input/$year/input_$day.txt';

  if (!File(filePath).existsSync()) {
    File(filePath).create(recursive: true);

    var url = Uri.parse('https://adventofcode.com/$year/day/$day/input');
    var response = await http.get(url, headers: getCookieHeaders());
    File(filePath).writeAsStringSync(response.body);
  }

  String result = File(filePath).readAsStringSync();
  return result;
}
