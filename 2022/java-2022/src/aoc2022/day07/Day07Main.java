package aoc2022.day07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

public class Day07Main {

    public static void main(String[] args) throws IOException {
//        final String input = "2022/inputs/test07.txt";
        final String input = "2022/inputs/input07.txt";

        final MyDir root = new MyDir("/");
        Files.lines(Paths.get(input))
                .forEach(new Consumer<>() {

                             Stack<MyDir> stack = new Stack<>();
                             MyDir cwd;

                             @Override
                             public void accept(String s) {
                                 if (s.equals("$ cd /")) {
                                     stack.clear();
                                     cwd = root;
                                 } else if (s.equals("$ cd ..")) {
                                     cwd = stack.pop();
                                 } else if (s.startsWith("$ cd ")) {
                                     final String dirName = s.substring(5);
                                     stack.push(cwd);
                                     cwd = cwd.subDirs.get(dirName);
                                 } else if (s.equals("$ ls")) {
                                     return;
                                 } else if (s.startsWith("dir ")) {
                                     final String dirName = s.substring(4);
                                     MyDir dir = new MyDir(cwd.path + dirName + "/");
                                     cwd.subDirs.put(dirName, dir);
                                 } else {
                                     int fileSize = Integer.parseInt(s.substring(0, s.indexOf(' ')));
                                     cwd.fileSizes += fileSize;
                                 }

                             }
                         }

                );

        List<MyDir> allDirs = new ArrayList<>();
        root.collect(allDirs::add);

        long sub100ksize = allDirs.stream()
                .filter(d -> d.size() <= 100000)
                .mapToInt(MyDir::size)
                .sum();
        System.out.println(sub100ksize);

        int filesystemSize = 70000000;
        int requiredSpace = 30000000;
        int currentSpace = filesystemSize - root.size();
        int deleteSize = requiredSpace - currentSpace;

        MyDir toDelete = allDirs.stream()
                .filter(d -> d.size() >= deleteSize)
                .sorted(Comparator.comparingInt(MyDir::size))
                .findFirst()
                .orElseThrow();
        System.out.println(toDelete.size());

    }


    static class MyDir {

        final String path;
        final Map<String, MyDir> subDirs = new LinkedHashMap<>();
        int fileSizes;
        int dirSize;

        MyDir(String path) {
            this.path = path;
        }

        public void collect(Consumer<MyDir> collector) {
            collector.accept(this);
            subDirs.values().forEach(d -> d.collect(collector));
        }

        public int size() {
            if (dirSize == 0) {
                dirSize = fileSizes;
                subDirs.values().forEach(d -> dirSize += d.size());
            }
            return dirSize;
        }


    }

}