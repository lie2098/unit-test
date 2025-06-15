package com.lye.springmvc.controller;

import com.lye.springmvc.models.CollegeStudent;
import com.lye.springmvc.models.GradebookCollegeStudent;
import com.lye.springmvc.repository.StudentDao;
import com.lye.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest
class GradebookControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StudentAndGradeService studentAndGradeServiceMock;

    @Autowired
    private static MockHttpServletRequest request;

    @Autowired
    private StudentDao studentDao;

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

    @BeforeAll
    static void beforeAll() {
        request = new MockHttpServletRequest();
        request.setParameter("firstname", "yum");
        request.setParameter("lastname", "mete");
        request.setParameter("emailAddress", "yum.mete@demo.com");
    }

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
    void getStudentHttpsRequest() throws Exception {
        CollegeStudent studentOne = new GradebookCollegeStudent("lie", "la", "lie.la@demo.com");
        CollegeStudent studentTwo = new GradebookCollegeStudent("You", "Ten", "you.ten@demo.com");

        List<CollegeStudent> studentList = new ArrayList<>(Arrays.asList(studentOne, studentTwo));

        when(studentAndGradeServiceMock.getGradebook()).thenReturn(studentList);

        assertEquals(studentList, studentAndGradeServiceMock.getGradebook());

        MvcResult mvcResult = mockMvc.perform(get("/"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "index");
    }

    @Test
    void createStudentHttpRequest() throws Exception {
        CollegeStudent studentOne = new CollegeStudent("Wata", "Mob", "wata.mob@demo.com");

        List<CollegeStudent> studentList = new ArrayList<>(List.of(studentOne));

        when(studentAndGradeServiceMock.getGradebook()).thenReturn(studentList);

        assertIterableEquals(studentList, studentAndGradeServiceMock.getGradebook());

        MvcResult mvcResult = mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("firstname", Objects.requireNonNull(request.getParameterValues("firstname")))
                        .param("lastname", Objects.requireNonNull(request.getParameterValues("lastname")))
                        .param("emailAddress", Objects.requireNonNull(request.getParameterValues("emailAddress"))))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "index");

        CollegeStudent collegeStudent = studentDao.findByEmailAddress("yum.mete@demo.com");

        assertNotNull(collegeStudent, "Student Should be Found");
    }

    @Test
    void deleteStudentHttpRequest() throws Exception {
        assertTrue(studentDao.findById(1).isPresent());

        MvcResult mvcResult = mockMvc.perform(get("/delete/student/{id}", 1))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "index");

        assertFalse(studentDao.findById(1).isPresent());
    }

    @Test
    void deleteStudentHttpRequestErrorPage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/delete/student/{id}", 0))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "error");
    }
}