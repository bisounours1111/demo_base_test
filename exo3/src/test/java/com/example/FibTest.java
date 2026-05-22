package com.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FibTest {

    @Test
    public void shouldReturnOnlyZeroWhenRangeIs1() {
        Fib fib = new Fib(1);

        List<Integer> result = fib.getFibSeries();

        assertFalse(result.isEmpty());
        assertEquals(Arrays.asList(0), result);
    }

    @Test
    public void shouldReturnCorrectSeriesWhenRangeIs6() {
        Fib fib = new Fib(6);

        List<Integer> result = fib.getFibSeries();

        assertTrue(result.contains(3));

        assertEquals(6, result.size());

        assertFalse(result.isEmpty());

        List<Integer> sorted = result.stream()
                .sorted()
                .toList();

        assertEquals(sorted, result);
    }
}
