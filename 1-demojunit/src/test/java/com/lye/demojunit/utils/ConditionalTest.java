package com.lye.demojunit.utils;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ConditionalTest {

    @Disabled("Don't run until JIRA #123 is resolved")
    @Test
    void basicTest() {
    }

    @EnabledOnOs(OS.WINDOWS)
    @Test
    void testForWindowsOnly() {
    }

    @EnabledOnOs(OS.MAC)
    @Test
    void testForMacOnly() {
    }

    @EnabledOnOs(OS.LINUX)
    @Test
    void testForLinuxOnly() {
    }

    @EnabledOnOs({OS.MAC, OS.WINDOWS})
    @Test
    void testForMACAndWindowsOnly() {
    }

    @EnabledOnJre(JRE.JAVA_17)
    @Test
    void testForOnlyJava17() {
    }

    @EnabledOnJre(JRE.JAVA_11)
    @Test
    void testForOnlyJava11() {
    }

    @EnabledForJreRange(min = JRE.JAVA_11, max = JRE.JAVA_21)
    @Test
    void testForOnlyJava11To21() {
    }

    @EnabledForJreRange(min = JRE.JAVA_11)
    @Test
    void testForOnlyJavaRangeMin() {
    }
    
    @EnabledIfEnvironmentVariable(named = "DEMO_ENV", matches = "DEV")
    @Test
    void testOnlyForDevEnvironment() {
    }

    @EnabledIfSystemProperty(named = "DEMO_SYS_PRO", matches = "DEPLOY")
    @Test
    void testOnlyForSystemProperty() {
    }
}
