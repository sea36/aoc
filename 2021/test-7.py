import unittest
from aoc_7 import part_a, part_b

class TestDay7(unittest.TestCase):

    def test_part_a(self):
        self.assertEqual(37, part_a("data/test7.txt"))

    def test_part_b(self):
        self.assertEqual(168, part_b("data/test7.txt"))


    def test_run_part_a(self):
        self.assertEqual(333755, part_a("data/input7.txt"))

    def test_run_part_b(self):
        self.assertEqual(94017638, part_b("data/input7.txt"))

if __name__ == '__main__':
    unittest.main()
