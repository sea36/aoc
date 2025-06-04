import unittest
from aoc_4 import part_a, part_b

class TestDay4(unittest.TestCase):

    def test_part_a(self):
        self.assertEqual(4512, part_a("data/test4.txt"))

    def test_part_b(self):
        self.assertEqual(1924, part_b("data/test4.txt"))


    def test_run_part_a(self):
        self.assertEqual(60368, part_a("data/input4.txt"))

    def test_run_part_b(self):
        self.assertEqual(17435, part_b("data/input4.txt"))

if __name__ == '__main__':
    unittest.main()
