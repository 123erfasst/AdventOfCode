import os
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
    url = f'https://adventofcode.com/{year}/day/{day}/input'
    req = urllib.request.Request(url, headers=_get_cookie_headers())
    return urllib.request.urlopen(req).read().decode()
