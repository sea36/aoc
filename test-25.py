import unittest
from aoc_25 import part_a

class TestDay25(unittest.TestCase):

    def test_part_a(self):
        self.assertEqual(58, part_a("data/test25.txt"))

    def test_run_part_a(self):
        self.assertEqual(384, part_a("data/input25.txt"))

if __name__ == '__main__':
    unittest.main()
