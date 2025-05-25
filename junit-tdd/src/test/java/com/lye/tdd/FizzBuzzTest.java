package com.lye.tdd;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class FizzBuzzTest {

    private FizzBuzz fizzBuzz;

    private int num = 100;

    @BeforeEach
    void setUp() {
        fizzBuzz = new FizzBuzz();
    }

    @Test
    @Order(1)
    @DisplayName("Not Divisible By 3 or 5, Print Number")
    void test_number() {
        assertEquals("1", fizzBuzz.display(1));
    }

    @Test
    @Order(2)
    @DisplayName("Divisible By 3, Print Fizz")
    void test_fizz() {
        assertEquals("Fizz", fizzBuzz.display(3));
    }

    @Test
    @Order(3)
    @DisplayName("Divisible By 5, Print Buzz")
    void test_buzz() {
        assertEquals("Buzz", fizzBuzz.display(5));
    }

    @Test
    @Order(4)
    @DisplayName("Divisible By 3 and 5, Print FizzBuzz")
    void test_fizz_buzz() {
        assertEquals("FizzBuzz", fizzBuzz.display(15));
    }

    @ParameterizedTest(name="value={0}, expected={1}")
    @CsvSource({
            "1, 1",
            "2, 2",
            "3, Fizz",
            "4, 4",
            "5, Buzz",
            "6,6",
            "7, 7",
            "8, 8",
            "9, Fizz",
            "10, Buzz",
            "11, 11",
            "12, Fizz",
            "13, 13",
            "14, 14",
            "15, FizzBuzz"
    })
    @DisplayName("Testing with csv data")
    void test_parameterized_csv_source(int value, String expected) {
        assertEquals(expected, fizzBuzz.display(value));
    }

    @ParameterizedTest(name="value={0}, expected={1}") // index
    @CsvFileSource(resources = "/test-data.csv")
    @DisplayName("Testing with test-data.csv")
    void test_parameterized_csv_file_source(int value, String expected) {
        assertEquals(expected, fizzBuzz.display(value));
    }

    @Test
    @Order(5)
    @DisplayName("First 100 Divisible By 3, Print Fizz")
    void test_first_100_divisible_by_3_iterate() {
        List<Integer> numbers = getDivisible(num, 3);
        List<String> results = getResult(numbers);
        int totalNum = 27;

        assertTrue(results.stream().allMatch("Fizz"::equals), "All Result are Fizz");
        assertEquals(totalNum, results.size(), totalNum + " Total First 100 Number that is divisible by 3");
    }

    @Test
    @Order(6)
    @DisplayName("First 100 Divisible By 5, Print Buzz")
    void test_first_100_divisible_by_5_iterate() {
        List<Integer> numbers = getDivisible(num, 5);
        List<String> results = getResult(numbers);
        int totalNum = 14;

        assertTrue(results.stream().allMatch("Buzz"::equals), "All Result are Buzz");
        assertEquals(totalNum, results.size(), totalNum + " Total First 100 Number that is divisible by 3");
    }

    @Test
    @Order(7)
    @DisplayName("First 100 Divisible By 3 and 5, Print FizzBuzz")
    void test_first_100_divisible_by_3_and_5_iterate() {
        List<Integer> numbers = getDivisible(num, 15);
        List<String> results = getResult(numbers);
        int totalNum = 6;

        assertTrue(results.stream().allMatch("FizzBuzz"::equals), "All Result are FizzBuzz");
        assertEquals(totalNum, results.size(), totalNum + " Total First 100 Number that is divisible by 3");
    }

    @Test
    @Order(8)
    @DisplayName("First 100 Not Divisible By 3 or 5, Print number")
    void test_first_100_not_divisible_by_3_or_5_iterate() {
        List<Integer> numbers = getDivisible(num, 0);
        List<String> results = getResult(numbers);
        int totalNum = 53;

        boolean allAreNumbers = results.stream().allMatch(val -> {
            try {
                Integer.parseInt(val);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        });

        assertTrue(allAreNumbers, "All Result are number");
        assertEquals(totalNum, results.size(), totalNum + " Total First 100 Number that is divisible by 3");
    }


    private List<Integer> getDivisible(int maxNum, int divisor) {
        List<Integer> result = new ArrayList<>();

        IntStream.range(1, maxNum + 1).forEach(num -> {
            if ((divisor == 0 && (num % 3 != 0 && num % 5 != 0)) || (divisor != 0 && isFizzBuzz(divisor, num))) {
                result.add(num);
            }
        });

        return result;
    }

    private boolean isFizzBuzz(int divisor, int num) {
        return num % divisor == 0 && !isDivisibleBy3And5(num) || (divisor == 15 && isDivisibleBy3And5(num));
    }

    private List<String> getResult(List<Integer> numbers) {
        return numbers.stream().map(number -> fizzBuzz.display(number)).collect(Collectors.toList());
    }

    private boolean isDivisibleBy3And5(int num) {
        return num % 3 == 0 && num % 5 == 0;
    }
}