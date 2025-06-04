import unittest
from aoc_10 import part_a, part_b

class TestDay10(unittest.TestCase):

    def test_part_a(self):
        self.assertEqual(26397, part_a("data/test10.txt"))

    def test_part_b(self):
        self.assertEqual(288957, part_b("data/test10.txt"))


    def test_run_part_a(self):
        self.assertEqual(436497, part_a("data/input10.txt"))

    def test_run_part_b(self):
        self.assertEqual(2377613374, part_b("data/input10.txt"))


if __name__ == '__main__':
    unittest.main()

