package com.lye.tdd;

import org.springframework.util.ObjectUtils;

public class FizzBuzz {
    public String display(int num) {
        StringBuilder result = new StringBuilder();

        if (num % 3 == 0) {
            result.append("Fizz");
        }

        if (num % 5 == 0) {
            result.append("Buzz");
        }

        return ObjectUtils.isEmpty(result) ? String.valueOf(num) : result.toString();
    }
}
