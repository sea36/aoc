package aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day23 {

    static final class Node {
        private final Xy location;
        private final List<Edge> edges;
        private final int id;
        private final long mask;

        Node(int id, Xy location, List<Edge> edges) {
            this.id = id;
            this.mask = 1L << id;

            this.location = location;
            this.edges = edges;
        }

        @Override
        public String toString() {
            return "Node[" +
                    "location=" + location + ", " +
                    "edges=" + edges + ']';
        }
    }

    static final class Edge {
        private final Node target;
        private final int length;

        Edge(Node target, int length) {
            this.target = target;
            this.length = length;
        }

        public int length() {
            return length;
        }

        @Override
        public String toString() {
            return "Edge[" +
                    "target=" + target + ", " +
                    "length=" + length + ']';
        }

    }

    enum CardinalPoint {
        N(0, -1),
        S(0, 1),
        E(1, 0),
        W(-1, 0);

        final int x, y;

        CardinalPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    record Xy(int x, int y) {

        Xy move(CardinalPoint d) {
            return new Xy(x + d.x, y + d.y);
        }
    }

    static class Hiker {

        private final char[][] map;
        private final Xy start;
        private final Xy target;

        private final List<Xy> path = new ArrayList<>();
        private final Set<Xy> visited = new HashSet<>();

        private int longestWalk = 0;


        Hiker(String[] map, Xy start, Xy target) {
            this.map = Arrays.stream(map).map(String::toCharArray).toArray(char[][]::new);
            this.start = start;
            this.target = target;
        }

        public void explore() {
            path.clear();
            visited.clear();

            visited.add(start);

            explore(start.move(CardinalPoint.S), start);
        }

        private void explore(Xy p, Xy last) {
            if (visited.add(p)) {
                path.add(p);

                if (p.equals(target)) {
                    if (path.size() > longestWalk) {
                        longestWalk = path.size();
                    }
                } else {

                    char ch = map[p.y][p.x];
                    switch (ch) {
                        case '>':
                            explore(p.move(CardinalPoint.E), p);
                            break;
                        case '<':
                            explore(p.move(CardinalPoint.W), p);
                            break;
                        case '^':
                            explore(p.move(CardinalPoint.N), p);
                            break;
                        case 'v':
                            explore(p.move(CardinalPoint.S), p);
                            break;

                        default:
                            for (CardinalPoint dir : CardinalPoint.values()) {
                                Xy q = p.move(dir);
                                if (!last.equals(q) && map[q.y][q.x] != '#') {
                                    explore(q, p);
                                }
                            }
                    }
                }

                path.remove(path.size() - 1);
                visited.remove(p);
            }
        }
    }

    static class Explorer {

        private final Map<Xy, Node> map;
        private final Xy start;
        private final int targetId;

        private int pathLength = 0;
        private long visitedMask;

        private int longestWalk = 0;


        Explorer(Map<Xy, Node> map, Xy start, Xy target) {
            this.map = map;
            this.start = start;
            this.targetId = map.get(target).id;
        }

        public void explore() {
            long t0 = System.nanoTime();

            visitedMask = 0;

            Node startNode = map.get(start);
            visitedMask |= startNode.mask;

            follow(startNode.edges.get(0));

            long t1 = System.nanoTime();

            System.out.printf("%.3f ms%n", (t1 - t0) / 1000000.0);
        }

        private void follow(Edge e) {
            long oldMask = visitedMask;
            visitedMask |= e.target.mask;
            pathLength += e.length;

            if (targetId == e.target.id) {
                longestWalk = Math.max(longestWalk, pathLength);
            } else {
                for (final Edge next : e.target.edges) {
                    if ((visitedMask & next.target.mask) == 0) {
                        follow(next);
                    }
                }
            }

            pathLength -= e.length;
            visitedMask = oldMask;
        }
    }

    static class GraphTracer {

        private final char[][] map;
        private final boolean part1;
        private final int xmax, ymax;

        private final Map<Xy, Node> nodeMap = new HashMap<>();

        public GraphTracer(String[] rows, boolean part1) {
            this.map = Arrays.stream(rows).map(String::toCharArray).toArray(char[][]::new);
            this.part1 = part1;
            this.xmax = map[0].length;
            this.ymax = map.length;
        }

        public Map<Xy, Node> traceGraph() {
            findNodes();
            findEdges();
            return nodeMap;
        }

        private void findNodes() {
            int i = 0;
            for (int y = 0; y < ymax; y++) {
                for (int x = 0; x < xmax; x++) {
                    if (map[y][x] == '#') {
                        continue;
                    }

                    Xy p = new Xy(x, y);

                    int neighbourCount = 0;
                    for (CardinalPoint dir : CardinalPoint.values()) {
                        Xy q = p.move(dir);
                        if (q.x >= 0 && q.y >= 0 && q.x < xmax && q.y < ymax && map[q.y][q.x] != '#') {
                            neighbourCount++;
                        }
                    }

                    if (neighbourCount != 2) {
                        nodeMap.put(p, new Node(i++, p, new ArrayList<>()));
                    }
                }
            }
        }

        private void findEdges() {
            for (Node node : nodeMap.values()) {
                Xy p = node.location;

                for (CardinalPoint dir : CardinalPoint.values()) {
                    Xy q = p.move(dir);
                    if (q.x >= 0 && q.y >= 0 && q.x < xmax && q.y < ymax) {
                        if (map[q.y][q.x] != '#') {
                            Edge edge = traceEdge(q, p, 1);
                            if (edge != null) {
                                node.edges.add(edge);
                            }
                        }
                    }
                }

            }
        }

        private Edge traceEdge(Xy p, Xy last, int length) {
            Xy next = null;
            char c0 = map[p.y][p.x];
            CardinalPoint[] dirs = CardinalPoint.values();
            if (part1) {
                if (c0 == '^') {
                    dirs = new CardinalPoint[]{CardinalPoint.N};
                } else if (c0 == '>') {
                    dirs = new CardinalPoint[]{CardinalPoint.E};
                } else if (c0 == '<') {
                    dirs = new CardinalPoint[]{CardinalPoint.W};
                } else if (c0 == 'v') {
                    dirs = new CardinalPoint[]{CardinalPoint.S};
                }
            }
            for (CardinalPoint dir : dirs) {
                Xy q = p.move(dir);
                if (q.equals(last)) {
                    continue;
                }
                if (q.x < 0 || q.y < 0 || q.x >= xmax || q.y >= ymax) {
                    continue;
                }
                char c = map[q.y][q.x];
                if (c != '#') {
                    if (next != null) {
                        Node node = nodeMap.get(p);
                        return new Edge(node, length);
                    }
                    next = q;
                }
            }
            if (next == null) {
                if (dirs.length == 1) {
                    return null;
                }
                Node node = nodeMap.get(p);
                return new Edge(node, length);
            }
            return traceEdge(next, p, length + 1);
        }
    }


    public static void main(String[] args) throws IOException {
//        final String path = "2023/inputs/example23.txt";
        final String path = "2023/inputs/input23.txt";

        String[] rows = Files.readAllLines(Path.of(path)).toArray(String[]::new);

        int ymax = rows.length;
        Xy start = new Xy(rows[0].indexOf('.'), 0);
        Xy target = new Xy(rows[ymax - 1].indexOf('.'), ymax - 1);

        for (boolean part1 : new boolean[]{true, false}) {

            GraphTracer graphTracer = new GraphTracer(rows, part1);
            Map<Xy, Node> nodeMap = graphTracer.traceGraph();

//            String dir = part1 ? " -> " : " -- ";
//            for (Node node : nodeMap.values()) {
//                for (Edge edge : node.edges) {
//                    String p1 = String.format("x%dy%d", node.location.x, node.location.y);
//                    String p2 = String.format("x%dy%d", edge.target.location.x, edge.target.location.y);
//
//                    if (part1 || p1.compareTo(p2) < 0) {
//                        System.out.println("  " + p1 + dir + p2 + ";");
//                    }
//                }
//            }

            Explorer explorer = new Explorer(nodeMap, start, target);
            explorer.explore();

            System.out.println(explorer.longestWalk);
        }
    }
}