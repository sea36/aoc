import unittest
from aoc_22 import generate_core

class TestDay22(unittest.TestCase):

    def test_part_1a(self):
        self.assertEqual(39, generate_core("data/test_22a.txt", initialization = True))

    def test_part_1b(self):
        self.assertEqual(590784, generate_core("data/test_22b.txt", initialization = True))

    def test_part_1c(self):
        self.assertEqual(474140, generate_core("data/test_22c.txt", initialization = True))

    def test_run_part_1(self):
        self.assertEqual(564654, generate_core("data/input_22.txt", initialization = True))

    def test_part_2c(self):
        self.assertEqual(2758514936282235, generate_core("data/test_22c.txt", initialization = False))

    def test_run_part_2(self):
        self.assertEqual(1214193181891104, generate_core("data/input_22.txt", initialization = False))


if __name__ == '__main__':
    unittest.main()
