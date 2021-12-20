import unittest
from aoc_19 import resolve_grid

class TestDay19(unittest.TestCase):

    def test_data(self):
        grid = resolve_grid("data/test19.txt")

        self.assertEqual(79, grid.beacon_count())
        self.assertEqual(3621, grid.max_scanner_distance())

    def test_real_input(self):
        grid = resolve_grid("data/input19.txt")

        self.assertEqual(381, grid.beacon_count())
        self.assertEqual(12201, grid.max_scanner_distance())

if __name__ == '__main__':
    unittest.main()
