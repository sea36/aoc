package aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Day24 {


    record Hailstone(long x, long y, long z, long vx, long vy, long vz) {
    }

    //    static final String path = "2023/inputs/example24.txt";
    static final String path = "2023/inputs/input24.txt";

    //    static final double min = 7, max = 27;
    static final double min = 200000000000000.0, max = 400000000000000.0;


    public static void main(String[] args) throws IOException {

        Hailstone[] hailstones = Files.lines(Path.of(path))
                .map(s -> s.split(" *[,@] *"))
                .map(s -> Arrays.stream(s).mapToLong(Long::parseLong).toArray())
                .map(s -> new Hailstone(s[0], s[1], s[2], s[3], s[4], s[5]))
                .toArray(Hailstone[]::new);

        part1(hailstones);
        part2(hailstones);
//        part2z3(hailstones);
    }

    private static void part1(Hailstone[] hailstones) {
        int intersects = 0;

        for (int i = 0; i < hailstones.length; i++) {
            Hailstone h1 = hailstones[i];
            for (int j = i + 1; j < hailstones.length; j++) {
                Hailstone h2 = hailstones[j];

                // simultaneous linear equation:
                // H1:  x = A + at   y = B + bt
                // H2:  x = C + cu   y = D + du
                //
                //  t = [ d ( C - A ) - c ( D - B ) ] / ( a * d - b * c )

                double A = h1.x, a = h1.vx;
                double B = h1.y, b = h1.vy;
                double C = h2.x, c = h2.vx;
                double D = h2.y, d = h2.vy;

                double t = (d * (C - A) - c * (D - B)) / ((a * d) - (b * c));
                double x = A + a * t;
                double y = B + b * t;
                double u = (A + (a * t) - C) / c;
                if (t >= 0 && Double.isFinite(t) && u >= 0 && Double.isFinite(u)) {

                    if (x >= min && x <= max && y >= min && y <= max) {
                        intersects++;
                    }
                }
            }
        }


        System.out.println(intersects);
    }

    private static void part2(Hailstone[] hailstones) {

        long t0 = System.nanoTime();

        Hailstone h1 = hailstones[0];
        Hailstone h2 = hailstones[1];

        int range = 500;
        for (int vx = -range; vx <= range; vx++) {
            for (int vy = -range; vy <= range; vy++) {
                if (vx == 0 || vy == 0) {
                    continue;
                }

                // Find starting point for rock that will intercept first two hailstones (x,y) on this trajectory

                // simultaneous linear equation (from part 1):
                // H1:  x = A + a*t   y = B + b*t
                // H2:  x = C + c*u   y = D + d*u
                //
                //  t = [ d ( C - A ) - c ( D - B ) ] / ( a * d - b * c )
                //
                // Solve for origin of rock intercepting both hailstones in x,y:
                //     x = A + a*t - vx*t   y = B + b*t - vy*t
                //     x = C + c*u - vx*u   y = D + d*u - vy*u

                long A = h1.x, a = h1.vx - vx;
                long B = h1.y, b = h1.vy - vy;
                long C = h2.x, c = h2.vx - vx;
                long D = h2.y, d = h2.vy - vy;

                // skip if division by 0
                if (c == 0 || (a * d) - (b * c) == 0) {
                    continue;
                }

                // Rock intercepts H1 at time t
                long t = (d * (C - A) - c * (D - B)) / ((a * d) - (b * c));

                // Calculate starting position of rock from intercept point
                long x = h1.x + h1.vx * t - vx * t;
                long y = h1.y + h1.vy * t - vy * t;

                for (int vz = -range; vz <= range; vz++) {

                    if (vz == 0) {
                        continue;
                    }

                    long z = h1.z + h1.vz * t - vz * t;


                    // check if this rock throw will hit all hailstones

                    boolean hitall = true;
                    for (int i = 0; i < hailstones.length; i++) {

                        Hailstone h = hailstones[i];
                        long u;
                        if (h.vx != vx) {
                            u = (x - h.x) / (h.vx - vx);
                        } else if (h.vy != vy) {
                            u = (y - h.y) / (h.vy - vy);
                        } else if (h.vz != vz) {
                            u = (z - h.z) / (h.vz - vz);
                        } else {
                            throw new RuntimeException();
                        }

                        if ((x + u * vx != h.x + u * h.vx) || (y + u * vy != h.y + u * h.vy) || (z + u * vz != h.z + u * h.vz)) {
                            hitall = false;
                            break;
                        }
                    }

                    if (hitall) {
                        System.out.printf("%d %d %d   %d %d %d   %d %n", x, y, z, vx, vy, vz, x + y + z);
                    }
                }
            }
        }

        long t1 = System.nanoTime();

        System.out.println((t1 - t0) / 1000000);
    }

    private static void part2z3(Hailstone[] hailstones) {

        String[] tuv = {"t1", "t2", "t3"};

        for (String s : new String[]{"x", "y", "z", "vx", "vy", "vz"}) {
            System.out.printf("(declare-const %s Int)%n", s);
        }
        for (String t : tuv) {
            System.out.printf("(declare-const %s Int)%n", t);
        }
        for (int i = 0; i < tuv.length; i++) {
            System.out.printf("(assert (= (+ x (* vx %s)) (+ %d (* %d %s)) )) %n", tuv[i], hailstones[i].x, hailstones[i].vx, tuv[i]);
            System.out.printf("(assert (= (+ y (* vy %s)) (+ %d (* %d %s)) )) %n", tuv[i], hailstones[i].y, hailstones[i].vy, tuv[i]);
            System.out.printf("(assert (= (+ z (* vz %s)) (+ %d (* %d %s)) )) %n", tuv[i], hailstones[i].z, hailstones[i].vz, tuv[i]);

        }

        System.out.println("(check-sat)");
        System.out.println("(get-model)");
        System.out.println("(eval (+ x (+ y z)))");
    }
}
