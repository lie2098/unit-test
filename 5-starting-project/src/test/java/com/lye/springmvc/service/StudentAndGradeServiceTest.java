package com.lye.springmvc.service;

import com.lye.springmvc.models.*;
import com.lye.springmvc.repository.HistoryGradeDao;
import com.lye.springmvc.repository.MathGradeDao;
import com.lye.springmvc.repository.ScienceGradeDao;
import com.lye.springmvc.repository.StudentDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentAndGradeServiceTest {

    @Autowired
    private StudentAndGradeService studentAndGradeService;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MathGradeDao mathGradeDao;

    @Autowired
    private ScienceGradeDao scienceGradeDao;

    @Autowired
    private HistoryGradeDao historyGradeDao;

    @Value("${sql.script.create.student}")
    private String sqlCreateStudent;

    @Value("${sql.scripts.create.math.grade}")
    private String sqlCreateMathGrade;

    @Value("${sql.scripts.create.science.grade}")
    private String sqlCreateScienceGrade;

    @Value("${sql.scripts.create.history.grade}")
    private String sqlCreateHistoryGrade;

    @Value("${sql.script.delete.student}")
    private String sqlDeleteStudent;

    @Value("${sql.script.delete.math.grade}")
    private String sqlDeleteMathGrade;

    @Value("${sql.script.delete.science.grade}")
    private String sqlDeleteScienceGrade;

    @Value("${sql.script.delete.history.grade}")
    private String sqlDeleteHistoryGrade;

    @BeforeEach
    void setUpDatabase() {
        jdbcTemplate.execute(sqlCreateStudent);
        jdbcTemplate.execute(sqlCreateMathGrade);
        jdbcTemplate.execute(sqlCreateScienceGrade);
        jdbcTemplate.execute(sqlCreateHistoryGrade);
    }

    @AfterEach
    void setupAfterTransaction() {
        jdbcTemplate.execute(sqlDeleteStudent);
        jdbcTemplate.execute(sqlDeleteMathGrade);
        jdbcTemplate.execute(sqlDeleteScienceGrade);
        jdbcTemplate.execute(sqlDeleteHistoryGrade);
    }

    @Test
    void createStudentService() {
        String email = "lye.lie@demo.com";

        studentAndGradeService.createStudent("Lye", "Lie", email);

        CollegeStudent student = studentDao.findByEmailAddress(email);

        assertEquals(email, student.getEmailAddress(), "Find by email");
    }

    @Test
    void isStudentNullCheck() {
        assertFalse(studentAndGradeService.checkIfStudentIsNull(1));
        assertTrue(studentAndGradeService.checkIfStudentIsNull(0));
    }

    @Test
    void deleteStudentService() {
        Optional<CollegeStudent> deletedCollegeStudent = studentDao.findById(1);
        Optional<MathGrade> deletedMathGrade = mathGradeDao.findById(1);
        Optional<ScienceGrade> deletedScienceGrade = scienceGradeDao.findById(1);
        Optional<HistoryGrade> deletedHistoryGrade = historyGradeDao.findById(1);

        assertTrue(deletedCollegeStudent.isPresent());
        assertTrue(deletedMathGrade.isPresent());
        assertTrue(deletedScienceGrade.isPresent());
        assertTrue(deletedHistoryGrade.isPresent());

        studentAndGradeService.deleteStudent(1);

        deletedCollegeStudent = studentDao.findById(1);
        deletedMathGrade = mathGradeDao.findById(1);
        deletedScienceGrade = scienceGradeDao.findById(1);
        deletedHistoryGrade = historyGradeDao.findById(1);

        assertFalse(deletedCollegeStudent.isPresent());
        assertFalse(deletedMathGrade.isPresent());
        assertFalse(deletedScienceGrade.isPresent());
        assertFalse(deletedHistoryGrade.isPresent());
    }

    @Sql("/insertData.sql")
    @Test
    void getGradebookService() {
        Iterable<CollegeStudent> iterableCollegeStudents = studentAndGradeService.getGradebook();

        List<CollegeStudent> collegeStudents = new ArrayList<>();
        for (CollegeStudent student : iterableCollegeStudents) {
            collegeStudents.add(student);
        }

        assertEquals(5, collegeStudents.size());
    }

    @Test
    void createGradeService() {
        assertTrue(studentAndGradeService.createGrade(80.69, 1, "math"));
        assertTrue(studentAndGradeService.createGrade(80.69, 1, "science"));
        assertTrue(studentAndGradeService.createGrade(80.69, 1, "history"));

        Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradeByStudentId(1);
        Iterable<HistoryGrade> historyGrades = historyGradeDao.findGradeByStudentId(1);

        assertTrue(((List<MathGrade>) mathGrades).size() == 2, "Student has 2 math grades");
        assertTrue(((List<ScienceGrade>) scienceGrades).size() == 2, "Student has 2 science grades");
        assertTrue(((List<HistoryGrade>) historyGrades).size() == 2, "Student has 2 history grades");
    }

    @Test
    void createGradeServiceReturnFalse() {
        assertFalse(studentAndGradeService.createGrade(100.69, 1, "math"));
        assertFalse(studentAndGradeService.createGrade(-100.69, 1, "science"));
        assertFalse(studentAndGradeService.createGrade(100, 1, "literature"));
    }

    @Test
    void deleteGradeService() {
        assertEquals(1, studentAndGradeService.getIdAndDeleteGrade(1, "math"), "Returns student id after delete");
        assertEquals(1, studentAndGradeService.getIdAndDeleteGrade(1, "science"), "Returns student id after delete");
        assertEquals(1, studentAndGradeService.getIdAndDeleteGrade(1, "history"), "Returns student id after delete");
    }

    @Test
    void deleteGradeServiceAndReturnStudentIdOfZero() {
        assertEquals(0, studentAndGradeService.getIdAndDeleteGrade(0, "math"), "No student should have 0 id");
        assertEquals(0, studentAndGradeService.getIdAndDeleteGrade(1, "literature"), "No student should have literature class");
    }

    @Test
    void studentInformation() {
        GradebookCollegeStudent gradebookCollegeStudent = studentAndGradeService.studentInformation(1);

        assertNotNull(gradebookCollegeStudent);
        assertEquals(1, gradebookCollegeStudent.getId());
        assertEquals("Lie", gradebookCollegeStudent.getFirstname());
        assertEquals("Lye", gradebookCollegeStudent.getLastname());
        assertEquals("lie.lye@demo.com", gradebookCollegeStudent.getEmailAddress());
        assertTrue(gradebookCollegeStudent.getStudentGrades().getMathGradeResults().size() == 1);
        assertTrue(gradebookCollegeStudent.getStudentGrades().getScienceGradeResults().size() == 1);
        assertTrue(gradebookCollegeStudent.getStudentGrades().getHistoryGradeResults().size() == 1);
    }

    @Test
    void studentInformationReturnNull() {
        GradebookCollegeStudent gradebookCollegeStudent = studentAndGradeService.studentInformation(0);

        assertNull(gradebookCollegeStudent);
    }
}
