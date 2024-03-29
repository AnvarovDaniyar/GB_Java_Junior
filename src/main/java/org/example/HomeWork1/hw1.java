package org.example.HomeWork1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class hw1 {
    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        numbers.stream()
                .filter(n -> n % 2 == 0)
                .mapToDouble(Integer::intValue)
                .average()
                .ifPresent(System.out::println);
    }
}