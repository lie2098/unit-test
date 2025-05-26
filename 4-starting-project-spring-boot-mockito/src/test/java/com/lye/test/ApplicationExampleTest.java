package com.lye.test;

import com.lye.component.MvcTestingExampleApplication;
import com.lye.component.models.CollegeStudent;
import com.lye.component.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class ApplicationExampleTest {

    private static int count = 0;

    @Value("${info.app.name}")
    private String appInfo;

    @Value("${info.school.name}")
    private String schoolName;

    @Value("${info.app.description}")
    private String appDescription;

    @Value("${info.app.version}")
    private String appVersion;

    @Autowired
    CollegeStudent student;

    @Autowired
    StudentGrades grades;

    @Autowired
    ApplicationContext context;

    @BeforeEach
    void setUp() {
        count++;

        System.out.println("Testing: " + appInfo + " which is " + appDescription + " version " + appVersion
                + ". Execution of test method " + count);

        student.setFirstname("Lye");
        student.setLastname("Lie");
        student.setEmailAddress(student.getFirstname() + "." + student.getLastname() + "@demo.com");
        grades.setMathGradeResults(new ArrayList<>(Arrays.asList(100.0, 85.0, 75.0, 83.75)));
        student.setStudentGrades(grades);
    }

    @DisplayName("Add grade results for student grades")
    @Test
    void addGradeResultsForStudentGrades() {
        assertEquals(343.75, grades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults()));
    }

    @DisplayName("Add grade results for student grades not equal")
    @Test
    void addGradeResultsForStudentGradesNotEqual() {
        assertNotEquals(0, grades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults()));
    }

    @DisplayName("Is grade greater")
    @Test
    void isGradeGreater() {
        assertTrue(grades.isGradeGreater(90, 75), "failure - should be true");
    }

    @DisplayName("Is grade greater false")
    @Test
    void isGradeGreaterFalse() {
        assertFalse(grades.isGradeGreater(89, 92), "failure - should be false");
    }

    @DisplayName("Check Null for student grades")
    @Test
    void checkNullForStudentGrades() {
        assertNotNull(grades.checkNull(student.getStudentGrades()), "failure - grade should not be null");
    }

    @DisplayName("Create student without grade in it")
    @Test
    void createStudentWithoutGrade() {
        CollegeStudent student2 = context.getBean("collegeStudent", CollegeStudent.class);
        student2.setFirstname("L");
        student2.setLastname("Lie");
        student2.setEmailAddress(student.getFirstname() + "." + student.getLastname() + "@demo.com");

        assertNotNull(student2.getFirstname());
        assertNotNull(student2.getLastname());
        assertNotNull(student2.getEmailAddress());
        assertNull(grades.checkNull(student2.getStudentGrades()));
    }

    @DisplayName("Verify Student are prototypes")
    @Test
    void verifyStudentArePrototypes() {
        CollegeStudent student2 = context.getBean("collegeStudent", CollegeStudent.class);

        assertNotSame(student, student2);
    }

    @DisplayName("Find Grade Point Average")
    @Test
    void findGradePointAverage() {
        assertAll("Testing all assertEquals",
                () -> assertEquals(343.75, grades.addGradeResultsForSingleClass(
                        student.getStudentGrades().getMathGradeResults())),
                () -> assertEquals(85.94, grades.findGradePointAverage(
                        student.getStudentGrades().getMathGradeResults())));
    }
}
