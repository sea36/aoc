import unittest
from aoc_15 import part_a, part_b

class TestDay15(unittest.TestCase):

    def test_part_a(self):
        self.assertEqual(40, part_a("data/test15.txt"))

    def test_part_b(self):
        self.assertEqual(315, part_b("data/test15.txt"))

    def test_run_part_a(self):
        self.assertEqual(456, part_a("data/input15.txt"))

    def test_run_part_b(self):
        self.assertEqual(2831, part_b("data/input15.txt"))

if __name__ == '__main__':
    unittest.main()
