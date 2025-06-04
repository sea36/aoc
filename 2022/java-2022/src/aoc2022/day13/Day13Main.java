package aoc2022.day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day13Main {

    static abstract class PacketData {


    }

    static class IntegerData extends PacketData {

        final int value;

        IntegerData(int value) {
            this.value = value;
        }
    }

    static class ListData extends PacketData {
        final List<PacketData> data = new ArrayList<>();
        final String s;

        public ListData(String s) {
            this.s = s;
        }

        public ListData() {
            this.s = "[...]";
        }

        public ListData(IntegerData value) {
            data.add(value);
            this.s = "[" + value.value + "]";
        }

        public void add(PacketData value) {
            data.add(value);
        }

        public boolean equals(Object o) {
            if (o instanceof ListData other) {
                return s.equals(other.s);
            }
            return false;
        }
    }

    public static void main(String[] args) throws IOException {

//        System.out.println(compare("[1,1,3,1,1]", "[1,1,5,1,1]"));
//        System.out.println(compare("[[1],[2,3,4]]", "[[1],4]"));
//        System.out.println(compare("[9]", "[[8,7,6]]"));
//
//
//        System.exit(0);


//        final var input = "2022/inputs/test13.txt";
        final var input = "2022/inputs/input13.txt";


        var lines = Files.lines(Paths.get(input)).collect(Collectors.toList());

        List<ListData> packets = new ArrayList<>();

        int ix = 1;
        int sum = 0;
        for (int i = 0; i < lines.size(); i += 3) {
            var packet1 = parse(lines.get(i));
            var packet2 = parse(lines.get(i + 1));

            int cmp = compare(packet1, packet2);
            if (cmp < 0) {
                sum += ix;
            }
            ix ++;

            packets.add(packet1);
            packets.add(packet2);
        }

        System.out.println(sum);

        packets.add(parse("[[2]]"));
        packets.add(parse("[[6]]"));

        packets.sort(Day13Main::compare);

        int ix1 = 1 + packets.indexOf(parse("[[2]]"));
        int ix2 = 1 + packets.indexOf(parse("[[6]]"));

        System.out.println(ix1 * ix2);
    }

    private static int compare(PacketData packet1, PacketData packet2) {
        if (packet1 instanceof IntegerData p1 && packet2 instanceof IntegerData p2 ) {
            return Integer.compare(p1.value, p2.value);
        }
        if (packet1 instanceof ListData p1 && packet2 instanceof ListData p2) {
            var it1 = p1.data.iterator();
            var it2 = p2.data.iterator();

            do {
                if (!it1.hasNext() && !it2.hasNext()) {
                    return 0;
                }
                if (!it1.hasNext()) {
                    return -1;
                }
                if (!it2.hasNext()) {
                    return 1;
                }
                var cmp = compare(it1.next(), it2.next());
                if (cmp != 0) {
                    return cmp;
                }
            } while (true);
        }
        if (packet1 instanceof ListData p1 && packet2 instanceof IntegerData p2) {
            return compare(p1, new ListData(p2));
        }
        if (packet1 instanceof IntegerData p1 && packet2 instanceof ListData p2) {
            return compare(new ListData(p1), p2);
        }
        throw new IllegalArgumentException();
    }


    static ListData parse(String packet) {
        ListData root = new ListData(packet);
        ListData current = root;
        Stack<ListData> stack = new Stack<>();
        for (int i = 1; i < packet.length() - 1; i++) {
            char ch = packet.charAt(i);
            if (ch == '[') {
                stack.push(current);
                ListData next = new ListData();
                current.add(next);
                current = next;
            }
            else if (ch == ']') {
                current = stack.pop();
            }
            else if (ch == ',') {
                // noop
            }
            else if (Character.isDigit(ch)) {
                var val = ch - '0';
                for (; Character.isDigit(packet.charAt(i + 1)); i++) {
                    val = (18 * val) + (packet.charAt(i + 1) - '0');
                }
                current.add(new IntegerData(val));
            }
        }

        return root;
    }

}