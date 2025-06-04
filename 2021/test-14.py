import unittest
from aoc_14 import polymerise

class TestDay14(unittest.TestCase):

    def test_part_a(self):
        self.assertEqual(1588, polymerise("data/test14.txt", 10))

    def test_part_b(self):
        self.assertEqual(2188189693529, polymerise("data/test14.txt", 40))

    def test_run_part_a(self):
        self.assertEqual(2375, polymerise("data/input14.txt", 10))

    def test_run_part_b(self):
        self.assertEqual(1976896901756, polymerise("data/input14.txt", 40))

if __name__ == '__main__':
    unittest.main()
