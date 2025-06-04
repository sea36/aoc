package aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;

import static java.lang.String.join;

public class Day12 {

    record SearchKey(int cursor, int curBlock, int curBlockPos) {
    }

    public static void main(String[] args) throws IOException {
//        final String path = "2023/inputs/example12.txt";
        final String path = "2023/inputs/input12.txt";

        var lines = Files.readAllLines(Path.of(path));

        long sum = 0;
        for (var line : lines) {
            var split = line.split(" ");

            var map = split[0];
            var blockList = split[1];

            map = join("?", map, map, map, map, map);
            blockList = join(",", blockList, blockList, blockList, blockList, blockList);

            var blocks = Arrays.stream(blockList.split(",")).mapToInt(Integer::parseInt).toArray();
            var cache = new HashMap<SearchKey, Long>();

//            long ret = search2(map, blocks);
            long ret = search(0, 0, 0, map + ".", blocks, cache);
            sum += ret;

            System.out.println(map + "\t" + ret + "\t" + cache.size());
        }

        System.out.println(sum);

    }

    private static long search(int cursor, int curBlock, int curBlockPos, String map, int[] blocks, HashMap<SearchKey, Long> cache) {

        var key = new SearchKey(cursor, curBlock, curBlockPos);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        if (cursor == map.length()) {
            return (curBlock == blocks.length && curBlockPos == 0) ? 1 : 0;
        }

        var ch = map.charAt(cursor);
        long count = 0;

//        if (ch == '#' || ch == '?') {
//            count += search(cursor + 1, curBlock, curBlockPos + 1, map, blocks, cache);
//        }
//
//        if ((ch == '.' || ch == '?') && (curBlockPos == 0 || (curBlock < blocks.length && curBlockPos == blocks[curBlock]))) {
//            int nextBlock = curBlockPos == 0 ? curBlock : curBlock + 1;
//            count += search(cursor + 1, nextBlock, 0, map, blocks, cache);
//        }


        // not in block
        if (curBlockPos == 0) {
            // still not in block
            if (ch == '.' || ch == '?') {
                count += search(cursor + 1, curBlock, 0, map, blocks, cache);
            }
            // start of block
            if ((ch == '#' || ch == '?') && curBlock < blocks.length) {
                count += search(cursor + 1, curBlock, 1, map, blocks, cache);
            }
        }
        // in block
        else {
            // block ends too early
            if (ch == '.' && curBlockPos < blocks[curBlock]) {
                return 0;
            }
            // block too long
            if (ch == '#' && curBlockPos >= blocks[curBlock]) {
                return 0;
            }

            if (ch == '.' || (ch == '?' && curBlockPos == blocks[curBlock])) {
                count += search(cursor + 1, curBlock + 1, 0, map, blocks, cache);
            }
            if (ch == '#' || (ch == '?' && curBlockPos < blocks[curBlock])) {
                count += search(cursor + 1, curBlock, curBlockPos + 1, map, blocks, cache);
            }
        }

        cache.put(key, count);
        return count;
    }

    private static long search2(String spring, int[] counts) {

        spring = "." + spring;
        while (spring.endsWith(".")) {
            spring = spring.substring(0, spring.length() - 1);
        }

        var dp = new int[spring.length() + 1];
        dp[0] = 1;

        for (int i = 0; i < spring.length(); i++) {
            if (spring.charAt(i) != '#') {
                dp[i + 1] = 1;
            } else {
                break;
            }
        }

        for (var count : counts) {
            var n_dp = new int[spring.length() + 1];
            var chunk = 0;

//            System.out.println(spring+ " " + Arrays.toString(counts));
//            System.out.println(count + "   ( ) " + Arrays.toString(dp));

            for (int i = 0; i < spring.length(); i++) {
                var c = spring.charAt(i);
                if (c != '.') {
                    chunk += 1;
                } else {
                    chunk = 0;
                }

                if (c != '#') {
                    n_dp[i + 1] += n_dp[i];
                }

                if (chunk >= count && spring.charAt(i - count) != '#') {
                    n_dp[i + 1] += dp[i - count];
                }

//                System.out.println(count + " " + i + " (" +chunk + ") " + Arrays.toString(n_dp));
            }

            dp = n_dp;

//            System.out.println();
        }

        return dp[dp.length - 1];

    }
}
