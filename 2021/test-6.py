import unittest
from aoc_6 import simulate

class TestDay6(unittest.TestCase):

    def test_part_a_18(self):
        self.assertEqual(26, simulate("data/test6.txt", 18))

    def test_part_a(self):
        self.assertEqual(5934, simulate("data/test6.txt", 80))

    def test_part_b(self):
        self.assertEqual(26984457539, simulate("data/test6.txt", 256))


    def test_run_part_a(self):
        self.assertEqual(390923, simulate("data/input6.txt", 80))

    def test_run_part_b(self):
        self.assertEqual(1749945484935, simulate("data/input6.txt", 256))

if __name__ == '__main__':
    unittest.main()
