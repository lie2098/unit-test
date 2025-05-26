package com.lye.test;

import com.lye.component.MvcTestingExampleApplication;
import com.lye.component.dao.ApplicationDao;
import com.lye.component.models.CollegeStudent;
import com.lye.component.models.StudentGrades;
import com.lye.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class MockAnnotationTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CollegeStudent student;

    @Autowired
    private StudentGrades studentGrades;

    @Autowired
    private ApplicationService applicationService;

    @MockitoBean
    private ApplicationDao applicationDao;

    @BeforeEach
    void setUp() {
        student.setFirstname("Lye");
        student.setLastname("Soul");
        student.setEmailAddress(student.getFirstname() + "." + student.getLastname() + "@demo.com");
        student.setStudentGrades(studentGrades);
    }

    @DisplayName("When & Verify")
    @Test
    void assertEqualsTestAddressGrades() {
        when(applicationDao.addGradeResultsForSingleClass(any())).thenReturn(100.0);

        assertEquals(100, applicationService.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults()));
        verify(applicationDao, times(1)).addGradeResultsForSingleClass(any());
    }

    @DisplayName("Find Gpa")
    @Test
    public void assertEqualsTestFindGpa() {
        when(applicationDao.findGradePointAverage(any())).thenReturn(69.90);

        assertEquals(69.90, applicationService.findGradePointAverage(student.getStudentGrades().getMathGradeResults()));
        verify(applicationDao, times(1)).findGradePointAverage(any());
    }

    @DisplayName("Not Null")
    @Test
    void testAssertNotNull() {
        when(applicationDao.checkNull(any())).thenReturn(true);

        assertNotNull(applicationService.checkNull(student.getStudentGrades().getMathGradeResults()));
        verify(applicationDao, times(1)).checkNull(any());
    }

    @DisplayName("Throw an Exception")
    @Test
    void throwAnException() {
        when(applicationDao.checkNull(any())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> applicationService.checkNull(any()));
    }

    @DisplayName("Multiple Stubbing")
    @Test
    void stubbingConsecutiveCalls() {
        when(applicationDao.checkNull(any()))
                .thenThrow(new RuntimeException())
                .thenReturn("Do not Throw another exception");

        assertThrows(RuntimeException.class, () -> applicationService.checkNull(any()));
        assertEquals("Do not Throw another exception", applicationService.checkNull(any()));

        verify(applicationDao, times(2)).checkNull(any());
    }
}
