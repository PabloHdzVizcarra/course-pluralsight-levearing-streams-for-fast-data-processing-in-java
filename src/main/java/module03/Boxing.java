package module03;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class Boxing {

    @Param({"100000"})
    private int N;

    private ArrayList<Integer> arrayList = new ArrayList<>();
    private LinkedList<Integer> linkedList = new LinkedList<>();
    private LinkedList<Integer> shuffleLinkedList = new LinkedList<>();
    private LinkedList<Integer> scatteredLinkedList = new LinkedList<>();
    private int[] arrayOfInts;
    private Integer[] arrayOfIntegers;

    @Setup
    public void createArrayList() {
        arrayList = IntStream.range(0, N)
                .map(i -> i * 3)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Setup
    public void createLinkedList() {
        linkedList = IntStream.range(0, N)
                .map(number -> number * 3)
                .boxed()
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Setup
    public void createShuffleLinkedList() {
        shuffleLinkedList = new LinkedList<>();
        for (int i = 0; i < N + 1; i++) {
            shuffleLinkedList.add(i * 3);
        }

        Collections.shuffle(shuffleLinkedList, new Random(314159L));
    }

    @Setup
    public void createScatteredLinkedList() {
        scatteredLinkedList = new LinkedList<>();
        for (int i = 0; i < N + 1; i++) {
            scatteredLinkedList.add(i * 3);
            for (int j = 0; j < 100; j++) {
                scatteredLinkedList.add(0);
            }
        }
        scatteredLinkedList.removeIf(i -> i == 0);
    }

    @Setup
    public void createArrayOfInts() {
        arrayOfInts = new int[N];
        for (int i = 0; i < N; i++) {
            arrayOfInts[i] = 3 * i;
        }
    }

    @Setup
    public void createArrayOfIntegers() {
        arrayOfIntegers = new Integer[N];
        for (int i = 0; i < N; i++) {
            arrayOfIntegers[i] = 3 * i;
        }
    }

    @Benchmark
    public int calculate_sum_of_ints() {
        int sum = 0;
        for (int i = 0; i < arrayOfInts.length; i++) {
            sum += i * 7;
        }
        return sum;
    }

    @Benchmark
    public int calculate_sum_of_integers() {
        Integer sum = 0;
        for (int i = 0; i < arrayOfIntegers.length; i++) {
            sum += i * 7;
        }
        return sum;
    }

    @Benchmark
    public int calculateSumOfArrayList() {
        return arrayList.stream()
                .mapToInt(i -> i)
                .map(i -> i * 5)
                .sum();
    }

    @Benchmark
    public int calculateSumOfLinkedList() {
        return linkedList.stream()
                .mapToInt(i -> i)
                .map(i -> i * 5)
                .sum();
    }

    @Benchmark
    public int calculateSumOfLinkedListShuffle() {
        return shuffleLinkedList.stream()
                .mapToInt(i -> i)
                .map(i -> i * 5)
                .sum();
    }

    @Benchmark
    public int calculateSumOfLinkedListScattered() {
        return scatteredLinkedList.stream()
                .mapToInt(i -> i)
                .map(i -> i * 5)
                .sum();
    }

    public static void main(String[] args) {
        Options build = new OptionsBuilder()
                .include(Boxing.class.getName())
                .build();

        new Runner(build);
    }

}
