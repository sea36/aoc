import unittest
from aoc_13 import part_a, part_b

class TestDay13(unittest.TestCase):

    def test_part_a(self):
        self.assertEqual(17, part_a("data/test13.txt"))

    def test_part_b(self):
        self.assertEqual(16, part_b("data/test13.txt"))

    def test_run_part_a(self):
        self.assertEqual(664, part_a("data/input13.txt"))

    def test_run_part_b(self):
        self.assertEqual(91, part_b("data/input13.txt"))

if __name__ == '__main__':
    unittest.main()
