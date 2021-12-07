import 'dart:convert';
import 'dart:io';

void main() {
  int? prev = null;
  File('day01input.txt')
    .openRead()
    .transform(utf8.decoder)
    .transform(new LineSplitter())
    .map((event) => int.parse(event))
    .where((event) => (prev ?? event) < (prev = event))
    .length
    .then((value) => print(value));
}