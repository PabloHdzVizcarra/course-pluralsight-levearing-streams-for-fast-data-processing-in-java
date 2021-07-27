package module04;


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

import java.util.List;
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
public class SumParallel {

    private Random random = new Random();

    @Param({"1000000"})
    private int N;

    private List<Integer> intsN100;

    @Setup
    public void setup() {
        this.intsN100 = IntStream.range(0, N)
                .mapToObj(index -> random.nextInt(100))
                .collect(Collectors.toList());
    }

    @Benchmark
    public double sumNoParallel() {
        return this.intsN100.stream()
                .mapToInt(i -> i)
                .sum();
    }

    @Benchmark
    public double sumParallel() {
        return this.intsN100.stream()
                .mapToInt(i -> i)
                .parallel()
                .sum();
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(SumParallel.class.getName())
                .build();

        new Runner(options).run();
    }
}
