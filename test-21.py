import unittest
from aoc_21 import part_a, find_most_wins

class TestDay20(unittest.TestCase):

    def test_part_a(self):
        self.assertEqual(739785, part_a(4, 8))

    def test_run_part_a(self):
        self.assertEqual(671580, part_a(7, 6))

    def test_part_b(self):
        self.assertEqual(444356092776315, find_most_wins(4, 8))

    def test_run_part_b(self):
        self.assertEqual(912857726749764, find_most_wins(7, 6))


if __name__ == '__main__':
    unittest.main()
