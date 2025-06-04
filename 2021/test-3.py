import unittest
from aoc_3 import part_a, part_b

class TestDay3(unittest.TestCase):

    def test_part_a(self):
        self.assertEqual(198, part_a("data/test3.txt"))

    def test_part_b(self):
        self.assertEqual(230, part_b("data/test3.txt"))


    def test_run_part_a(self):
        self.assertEqual(2743844, part_a("data/input3.txt"))

    def test_run_part_b(self):
        self.assertEqual(6677951, part_b("data/input3.txt"))

if __name__ == '__main__':
    unittest.main()
