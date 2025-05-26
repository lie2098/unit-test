package com.lye.test;

import com.lye.component.MvcTestingExampleApplication;
import com.lye.component.models.CollegeStudent;
import com.lye.component.models.StudentGrades;
import javafx.scene.effect.Reflection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class ReflectionTestUtilsTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CollegeStudent student;

    @Autowired
    private StudentGrades studentGrades;

    @BeforeEach
    void setUp() {
        student.setFirstname("Lye");
        student.setLastname("Roar");
        student.setEmailAddress(student.getFirstname() + "." + student.getLastname() + "@demo.com");
        student.setStudentGrades(studentGrades);

        ReflectionTestUtils.setField(student, "id", 1);
        ReflectionTestUtils.setField(student, "studentGrades", new StudentGrades(Arrays.asList(100.0, 90.75, 80.5, 76.83)));
    }

    @Test
    void getPrivateField() {
        assertEquals(1, ReflectionTestUtils.getField(student, "id"));
    }

    @Test
    void invokePrivateMethod() {
        assertEquals("Lye 1", ReflectionTestUtils.invokeMethod(student, "getFirstNameAndId"));
    }
}
