import os
import shutil
import urllib.request

from cpl_core.console import Console


def _get_cookie_headers() -> dict[str, str]:
    """
    original code from https://github.com/anthonywritescode/aoc2022/blob/main/support/support.py
    """
    env = os.path.join(os.path.dirname(os.path.abspath(__file__)), '../../.env')
    if not os.path.exists(env):
        Console.error(f'Session key from https://adventofcode.com/ required')

    with open(env) as f:
        contents = f.read().strip()
    return {'Cookie': contents}


def get_input(year: int, day: int) -> str:
    """
    original code from https://github.com/anthonywritescode/aoc2022/blob/main/support/support.py
    """
    file = f'input/{year}/input_{day}.txt'
    if not os.path.exists(os.path.dirname(file)):
        os.makedirs(os.path.dirname(file))

    if not os.path.exists(file):
        url = f'https://adventofcode.com/{year}/day/{day}/input'
        req = urllib.request.Request(url, headers=_get_cookie_headers())
        txt = urllib.request.urlopen(req).read().decode()
        with open(file, 'w+') as f:
            f.write(txt)
            f.close()

    txt = ''
    with open(file, 'r') as f:
        txt = f.read()
        f.close()
    return txt
