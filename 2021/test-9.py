import unittest
from aoc_9 import part_a, part_b

class TestDay9(unittest.TestCase):

    def test_part_a(self):
        self.assertEqual(15, part_a("data/test9.txt"))

    def test_part_b(self):
        self.assertEqual(1134, part_b("data/test9.txt"))


    def test_run_part_a(self):
        self.assertEqual(480, part_a("data/input9.txt"))

    def test_run_part_b(self):
        self.assertEqual(1045660, part_b("data/input9.txt"))

if __name__ == '__main__':
    unittest.main()
