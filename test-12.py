import unittest
from aoc_12 import part_a, part_b

class TestDay12(unittest.TestCase):

    def test_part_a(self):
        self.assertEqual(10, part_a("data/test12.txt"))

    def test_part_b(self):
        self.assertEqual(36, part_b("data/test12.txt"))

    def test_part_a_2(self):
        self.assertEqual(19, part_a("data/test12b.txt"))

    def test_part_b_2(self):
        self.assertEqual(103, part_b("data/test12b.txt"))

    def test_part_a_3(self):
        self.assertEqual(226, part_a("data/test12c.txt"))

    def test_part_b_3(self):
        self.assertEqual(3509, part_b("data/test12c.txt"))

    def test_run_part_a(self):
        self.assertEqual(3410, part_a("data/input12.txt"))

    def test_run_part_b(self):
        self.assertEqual(98796, part_b("data/input12.txt"))

if __name__ == '__main__':
    unittest.main()
