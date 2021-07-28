package module05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class M05Sets {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Set<String> linesSet = new HashSet<>();

        try (Stream<String> lines =
                     Files.lines(Path.of("./src/main/resources/words.txt"))) {

            linesSet.addAll(lines.collect(Collectors.toSet()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> linesList = new ArrayList<>(linesSet);
        ConcurrentHashMap<String, Long> threads = new ConcurrentHashMap<>();

        Callable<Integer> task = () ->
                linesSet.stream()
                        .parallel()
                        .mapToInt(String::length)
                        .peek(i -> threads.merge(
                                Thread.currentThread().getName(),
                                1L,
                                Long::sum
                        ))
                        .sum();

        ForkJoinPool forkJoinPool = new ForkJoinPool(8);
        ForkJoinTask<Integer> submit = forkJoinPool.submit(task);
        submit.get();

        threads.forEach((key, value) -> System.out.println(key + " ->" + value));
        forkJoinPool.shutdown();

        Set<Integer> hashes = linesList.stream()
                .map(M05Sets::hash)
                .collect(Collectors.toSet());

        System.out.println("# hashes = " + hashes.size());
    }

    static int hash(Object key) {
        int h;
        return (key == null)
                ? 0
                : (h = key.hashCode()) ^ (h >>> 16);
    }
}
