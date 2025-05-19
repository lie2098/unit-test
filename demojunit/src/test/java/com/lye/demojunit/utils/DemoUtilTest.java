package com.lye.demojunit.utils;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;



//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//@TestMethodOrder(MethodOrderer.MethodName.class) // remove display name
//@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class DemoUtilTest {
    private DemoUtil demoUtil;

    @BeforeEach
    void setUp() {
        demoUtil = new DemoUtil();
        System.out.println("===> @BeforeEach executes before the execution of each test method");
    }

    /*@AfterEach
    void tearDown() {
        System.out.println("===> @AfterEach executes after the execution of each test method");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("===> @BeforeAll executes before all test methods");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("===> @AfterAll executes after all test methods");
    }*/

    @DisplayName("Equal and Not Equals")
    @Order(1)
    @Test
    void testEqualsAndNotEquals() {
        assertEquals(10, demoUtil.add(4, 6), "4+6 must be 10");
        assertNotEquals(10, demoUtil.add(5, 6), "5+6 must not be 10");
    }

    @DisplayName("Null and Not Null")
    @Order(2)
    @Test
    void testNullAndNotNull() {
        String str1 = null;
        String str2 = "Tehehe";

        assertNull(demoUtil.checkNull(str1));
        assertNotNull(demoUtil.checkNull(str2));
    }

    @DisplayName("Same and Not Same")
    @Order(-1)
    @Test
    void testSameAndNotSame() {
        String str = new String("Academia");

        assertSame(demoUtil.getAcademy(), demoUtil.getAcademyDuplicate(), "Object Should refer to same object");
        assertNotSame(str, demoUtil.getAcademy(), "Object Should not refer to same object");
    }

    @DisplayName("True and False")
    @Test
    void testTrueAndFalse() {
        int n1 = 9;
        int n2 = 6;

        assertTrue(demoUtil.isGreater(n1, n2));
        assertFalse(demoUtil.isGreater(n2, n1));
    }

    @DisplayName("Array Equals")
    @Test
    void testArrayEquals() {
        String[] stringArray = {"A", "B", "C"};
        assertArrayEquals(stringArray, demoUtil.getFirst3AlphabetLetters(), "Array should be equals");
    }


    @DisplayName("Iterable Equals")
    @Test
    void testIterableEquals() {
        List<String> list = List.of("Academia A", "Academia B", "Academia C");

        assertIterableEquals(list, demoUtil.getAcademyList(), "Expected list should be same as actual list");
    }

    @DisplayName("Lines Match")
    @Test
    void testLinesMatch() {
        List<String> list = List.of("Academia A", "Academia B", "Academia C");

        assertLinesMatch(list, demoUtil.getAcademyList(), "Lines should be match");
    }

    @DisplayName("Throws and Does Not Throw")
    @Test
    void testThrowsAndNotThrow() {
        assertThrows(Exception.class, () -> demoUtil.throwException(-69), "Should throw Exception");
        assertDoesNotThrow(() -> demoUtil.throwException(1), "Should not throw exception");
    }

    @DisplayName("Timeout")
    @Test
    void testTimeout() {
        assertTimeout(Duration.ofSeconds(3), () -> demoUtil.checkTimeout(), "Method should execute within 3 seconds");
    }
}