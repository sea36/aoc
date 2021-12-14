import unittest
from aoc_1 import part_a, part_b

class TestDay1(unittest.TestCase):

    def test_part_a(self):
        self.assertEqual(7, part_a("data/test1.txt"))

    def test_part_b(self):
        self.assertEqual(5, part_b("data/test1.txt"))


    def test_run_part_a(self):
        self.assertEqual(1446, part_a("data/input1.txt"))

    def test_run_part_b(self):
        self.assertEqual(1486, part_b("data/input1.txt"))

if __name__ == '__main__':
    unittest.main()
