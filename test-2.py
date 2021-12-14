import unittest
from aoc_2 import part_a, part_b

class TestDay2(unittest.TestCase):

    def test_part_a(self):
        self.assertEqual(150, part_a("data/test2.txt"))

    def test_part_b(self):
        self.assertEqual(900, part_b("data/test2.txt"))


    def test_run_part_a(self):
        self.assertEqual(1488669, part_a("data/input2.txt"))

    def test_run_part_b(self):
        self.assertEqual(1176514794, part_b("data/input2.txt"))

if __name__ == '__main__':
    unittest.main()
