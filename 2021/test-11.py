import unittest
from aoc_11 import count_flashes, find_sync_step

class TestDay11(unittest.TestCase):

    def test_part_a(self):
        self.assertEqual(1656, count_flashes("data/test11.txt", 100))

    def test_part_b(self):
        self.assertEqual(195, find_sync_step("data/test11.txt"))

    def test_run_part_a(self):
        self.assertEqual(1694, count_flashes("data/input11.txt", 100))

    def test_run_part_b(self):
        self.assertEqual(346, find_sync_step("data/input11.txt"))

if __name__ == '__main__':
    unittest.main()

