package aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day25 {

    public static final int MAX_DIST = 100000;

    record Node(int id, int[] connections) {
    }

    record Pair(int i0, int i1, int dist) {
    }

    public static void main(String[] args) throws IOException {

//        final String path = "2023/inputs/example25.txt";
        final String path = "2023/inputs/input25.txt";

        String[] rows = Files.readAllLines(Path.of(path)).toArray(String[]::new);

        int idCount = 0;
        Map<String, Integer> idMap = new HashMap<>();
        Map<String, List<String>> connectionMap = new HashMap<>();
        for (var row : rows) {
            var s = row.split(":? +");
            if (!idMap.containsKey(s[0])) {
                idMap.put(s[0], idCount++);
            }
            List<String> list = connectionMap.computeIfAbsent(s[0], i -> new ArrayList<>());
            for (int i = 1; i < s.length; i++) {
                list.add(s[i]);
                if (!idMap.containsKey(s[i])) {
                    idMap.put(s[i], idCount++);
                }
            }
        }

        int size = idCount + 1;

        Node[] nodes = new Node[size];
        for (var e : connectionMap.entrySet()) {
            int nodeId = idMap.get(e.getKey());
            int[] connections = e.getValue().stream().mapToInt(idMap::get).toArray();
            nodes[nodeId] = new Node(nodeId, connections);
        }
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] == null) {
                nodes[i] = new Node(i, new int[0]);
            }
        }

        int[][] dist = new int[size][size];
        for (int[] row : dist) {
            Arrays.fill(row, MAX_DIST);
        }
        for (int i = 0; i < nodes.length; i++) {
            dist[i][i] = 0;
            for (int j : nodes[i].connections) {
                dist[i][j] = 1;
            }
        }

        for (int k = 0; k < nodes.length; k++) {
            for (int i = 0; i < nodes.length; i++) {
                for (int j = 0; j < nodes.length; j++) {
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                }
            }
        }

        List<Pair> allPairs = new ArrayList<>();
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes.length; j++) {
                if (dist[i][j] < MAX_DIST) {
                    allPairs.add(new Pair(i, j, dist[i][j]));
                }
            }
        }

        allPairs.sort((o1, o2) -> o2.dist - o1.dist);
        for (int i = 0; i < 10; i++) {
            System.out.println(allPairs.get(i));
        }

        
    }
}
