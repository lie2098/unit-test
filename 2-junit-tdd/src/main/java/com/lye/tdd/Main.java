package com.lye.tdd;

import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        FizzBuzz fizzBuzz = new FizzBuzz();
        int num = 100;

        IntStream.range(0, num + 1).forEach(index -> System.out.println(fizzBuzz.display(index)));
    }
}
