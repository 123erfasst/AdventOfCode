from __future__ import annotations
from typing import Iterator, Optional, Any
from dataclasses import dataclass, field
from queue import PriorityQueue

from . import helper

@dataclass(order=True)
class PrioritizedItem:
    priority: int
    item: Any=field(compare=False)

@dataclass
class Location:
    x: int
    y: int
    value: str

    @property
    def height(self: Location) -> int:
        if self.value == 'S':
            return 0
        elif self.value == 'E':
            return 27

        return ord(self.value) - 96

    def get_edges(self: Location, grid: Graph) -> list[Location]:
        result = []
        offsets = [(-1, 0), (0, -1), (1, 0), (0, 1)]

        for i in range(len(offsets)):
            x: int = self.x + offsets[i][0]
            y: int = self.y + offsets[i][1]

            if x >= 0 and x < len(grid[0]) and y >= 0 and y < len(grid):
                result.append(Location(x, y, grid[y][x]))

        return list(filter(lambda item: self.height >= item.height - 1, result))

    def __hash__(self: Location):
        return hash((self.x, self.y))

@dataclass
class Graph:
    start: Location = None
    end: Location = None
    edges: dict[Location, list[Location]] = field(default_factory=dict)

def parse_grid(file: Iterator[str]) -> Graph:
    grid: list[list[int]] = []

    for line in file:
        grid.append(list(line))

    result: Graph = Graph()

    for y in range(len(grid)):
        for x in range(len(grid[0])):
            location: Location = Location(x, y, grid[y][x])

            if location.value == 'S':
                result.start = location
            elif location.value == 'E':
                result.end = location

            result.edges[location] = location.get_edges(grid)

    return result

def get_lowest_points(graph: Graph) -> list[Location]:
    return list(filter(lambda x: x.value == 'a', graph.edges))

def dijkstra_search(graph: Graph, start: Location, goal: Location) -> tuple[dict[Location, Optional[Location]], dict[Location, int]]:
    frontier = PriorityQueue()
    frontier.put(PrioritizedItem(0, start))
    came_from: dict[Location, Optional[Location]] = {}
    cost_so_far: dict[Location, int] = {}
    came_from[start] = None
    cost_so_far[start] = 0
    
    while not frontier.empty():
        current = frontier.get().item
        
        if current == goal:
            break
        
        for next in graph.edges[current]:
            new_cost = cost_so_far[current] + 1
            if next not in cost_so_far or new_cost < cost_so_far[next]:
                cost_so_far[next] = new_cost
                priority = new_cost
                frontier.put(PrioritizedItem(priority, next))
                came_from[next] = current
    
    return came_from, cost_so_far

def reconstruct_path(came_from: dict[Location, Location], start: Location, goal: Location) -> list[Location]:
    current: Location = goal
    path: list[Location] = []
    if goal not in came_from: # no path was found
        return []
    while current != start:
        path.append(current)
        current = came_from[current]
    path.append(start) # optional
    path.reverse() # optional
    return path

def run():
    # Round 1
    print("Round 1:")

    file: Iterator[str] = helper.load_file("input/day12.txt")

    graph: Graph = parse_grid(file)

    came_from, cost_so_far = dijkstra_search(graph, graph.start, graph.end)

    path = reconstruct_path(came_from, graph.start, graph.end)

    print(f"Path length: {len(path) - 1}")
    
    # Round 2
    print("Round 2:")

    shortest_path: int = -1

    for item in get_lowest_points(graph):
        came_from, cost_so_far = dijkstra_search(graph, item, graph.end)

        path = reconstruct_path(came_from, item, graph.end)
        if len(path) == 0:
            continue

        length = len(path) - 1

        if (length < shortest_path or shortest_path == -1):
            shortest_path = length

    print(f"Shortest path length: {shortest_path}")

