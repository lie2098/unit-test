package com.lye.demojunit.utils;

import lombok.Getter;

import java.util.List;

@Getter
public class DemoUtil {

    private final String academy = "Academia";
    private final String academyDuplicate = academy;
    private final String[] first3AlphabetLetters = {"A", "B", "C"};
    private final List<String> academyList = List.of("Academia A", "Academia B", "Academia C");

    public int add(int num1, int num2) {
        return num1 + num2;
    }

    public int multiple(int num1, int num2) {
        return num1 * num2;
    }

    public Object checkNull(Object object) {
        return object;
    }

    public Boolean isGreater(int n1, int n2) {
        return n1 > n2;
    }

    public String throwException(int n) throws Exception {
        if( n < 0 ) {
            throw new Exception("Value should not be a negative number");
        }

        return "Your Number is  " + n;
    }

    public void checkTimeout() throws InterruptedException {
        System.out.println("Checking timeout...");
        Thread.sleep(2000);
        System.out.println("Done checking timeout.");
    }
}
