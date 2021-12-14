import unittest
from aoc_5 import part_a, part_b

class TestDay5(unittest.TestCase):

    def test_part_a(self):
        self.assertEqual(5, part_a("data/test5.txt"))

    def test_part_b(self):
        self.assertEqual(12, part_b("data/test5.txt"))


    def test_run_part_a(self):
        self.assertEqual(6005, part_a("data/input5.txt"))

    def test_run_part_b(self):
        self.assertEqual(23864, part_b("data/input5.txt"))

if __name__ == '__main__':
    unittest.main()
