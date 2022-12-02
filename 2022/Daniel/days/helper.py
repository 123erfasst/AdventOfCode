from typing import Iterator

def load_file(filename: str) -> Iterator[str]:
    with open(filename, "r") as txt_file:
        for line in txt_file:
            yield line.strip()
