import unittest
from aoc_17 import run_simulation

TEST_DATA = {"x": [20,30], "y": [-10,-5]}
PROD_DATA = {"x": [14,50], "y": [-267,-225]}

class TestDay17(unittest.TestCase):

    def test_simulation(self):
        result = run_simulation(TEST_DATA["x"], TEST_DATA["y"])
        self.assertEqual(45, result["ymax"])
        self.assertEqual(112, result["result_count"])

    def test_real_simulation(self):
        result = run_simulation(PROD_DATA["x"], PROD_DATA["y"])
        self.assertEqual(35511, result["ymax"])
        self.assertEqual(3282, result["result_count"])

if __name__ == '__main__':
    unittest.main()
