package module05;


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
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Warmup(iterations = 10, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(value = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class M05SourceSplit {

    @Param("10000000")
    int N;

    Random random = new Random(314L);

    Set<String> lineSet;
    List<String> lineList;
    Set<Integer> intSet;
    List<Integer> intList;

    @Setup
    public void readLine() throws IOException {
        try (Stream<String> lines = Files.lines(Path.of(
                "./src/main/resources/words.txt"))) {
            this.lineSet = lines.collect(Collectors.toSet());
        }

        this.lineList = new ArrayList<>(this.lineSet);
    }

    @Setup
    public void intsList() {
        intList = IntStream.range(0, N)
                .map(i -> random.nextInt())
                .boxed()
                .collect(Collectors.toList());
    }

    @Setup
    public void intsSet() {
        intSet = IntStream.range(0, N)
                .map(i -> random.nextInt())
                .boxed()
                .collect(Collectors.toSet());
    }

    @Benchmark
    public Object process_string_set() {
        return this.lineSet.stream()
                .map(String::toUpperCase)
                .mapToInt(String::length)
                .sum();
    }

    @Benchmark
    public Object process_string_list() {
        return this.lineList.stream()
                .map(String::toUpperCase)
                .mapToInt(String::length)
                .sum();
    }

    @Benchmark
    public Object process_string_list_parallel() {
        return this.lineList.stream()
                .map(String::toUpperCase)
                .mapToInt(String::length)
                .parallel()
                .sum();
    }

    @Benchmark
    public Object process_string_set_parallel() {
        return this.lineSet.stream()
                .map(String::toUpperCase)
                .mapToInt(String::length)
                .parallel()
                .sum();
    }

    @Benchmark
    public Object process_int_set() {
        return this.intSet.stream()
                .mapToInt(i -> i * 3)
                .sum();
    }

    @Benchmark
    public Object process_int_list() {
        return this.intList.stream()
                .mapToInt(i -> i * 3)
                .sum();
    }

    @Benchmark
    public Object process_int_set_parallel() {
        return this.intSet.stream()
                .mapToInt(i -> i * 3)
                .parallel()
                .sum();
    }

    @Benchmark
    public Object process_int_list_parallel() {
        return this.intList.stream()
                .mapToInt(i -> i * 3)
                .parallel()
                .sum();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(M05SourceSplit.class.getName())
                .build();
        new Runner(opt).run();
    }
}
