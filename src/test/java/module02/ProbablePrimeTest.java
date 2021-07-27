package module02;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ProbablePrimeTest {

    @Test
    void sumNPrimes() {
        List<BigInteger> bigIntegerList = new ArrayList<>();

        for (int i = 0; i < 64; i++) {
            BigInteger prime = BigInteger.probablePrime(
                    10,
                    ThreadLocalRandom.current()
            );

            bigIntegerList.add(prime);
        }

        System.out.println(bigIntegerList);
    }

    @Test
    void parallel() {
        List<BigInteger> bigIntegers = IntStream.range(0, 100)
                .parallel()
                .mapToObj(number -> BigInteger.probablePrime(
                        64,
                        ThreadLocalRandom.current()
                ))
                .collect(Collectors.toList());

        System.out.println(bigIntegers);
    }
}