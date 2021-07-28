package module04;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.IntStream;

/**
 * Example who execute one task in a specific {@link ForkJoinPool}
 */
public class M04ForkJoinPool {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Set<String> threadNames = ConcurrentHashMap.newKeySet();
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        Map<String, Long> threadMap = new ConcurrentHashMap<>();

        Callable<Integer> task = () -> {
            int sum = IntStream.range(0, 1_000_000)
                    .map(i -> i * 3)
                    .parallel()
                    .peek(i -> threadMap.merge(
                            Thread.currentThread().getName(), 1L, Long::sum))
                    .sum();

            return sum;
        };

        ForkJoinTask<Integer> submit = forkJoinPool.submit(task);
        Integer sumAll = submit.get();
        System.out.println("The sum is: " + sumAll);

        threadNames
                .forEach(System.out::println);
        threadMap.forEach((name, n) -> System.out.println(name + " -> " + n));

        forkJoinPool.shutdown();
    }
}
