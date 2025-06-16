package com.lye.springmvc.service;

import com.lye.springmvc.models.*;
import com.lye.springmvc.repository.HistoryGradeDao;
import com.lye.springmvc.repository.MathGradeDao;
import com.lye.springmvc.repository.ScienceGradeDao;
import com.lye.springmvc.repository.StudentDao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {

    public static final String MATH = "math";
    public static final String SCIENCE = "science";
    public static final String HISTORY = "history";
    @Autowired
    private StudentDao studentDao;

    @Autowired
    @Qualifier("mathGrades")
    private MathGrade mathGrade;

    @Autowired
    @Qualifier("scienceGrades")
    private ScienceGrade scienceGrade;

    @Autowired
    @Qualifier("historyGrades")
    private HistoryGrade historyGrade;

    @Autowired
    private MathGradeDao mathGradeDao;

    @Autowired
    private ScienceGradeDao scienceGradeDao;

    @Autowired
    private HistoryGradeDao historyGradeDao;

    @Autowired
    private StudentGrades studentGrades;

    public void createStudent(String firstName, String lastName, String emailAddress) {
        CollegeStudent student = new CollegeStudent(firstName, lastName, emailAddress);
        student.setId(0);
        studentDao.save(student);
    }

    public boolean checkIfStudentIsNull(int id) {
        return studentDao.findById(id).isEmpty();
    }

    public void deleteStudent(int id) {
        if (!checkIfStudentIsNull(id)) {
            studentDao.deleteById(id);
            mathGradeDao.deleteByStudentId(id);
            scienceGradeDao.deleteByStudentId(id);
            historyGradeDao.deleteByStudentId(id);
        }
    }

    public Iterable<CollegeStudent> getGradebook() {
        return studentDao.findAll();
    }

    public boolean createGrade(double grade, int id, String gradeType) {
        if (checkIfStudentIsNull(id)) {
            return false;
        }

        boolean isSaved = false;

        if (isValidGrade(grade)) {
            isSaved = processGrades(grade, id, gradeType);
        }

        return isSaved;
    }

    private boolean processGrades(double grade, int id, String gradeType) {
        if (MATH.equals(gradeType)) {
            mathGrade.setId(0);
            mathGrade.setGrade(grade);
            mathGrade.setStudentId(id);
            mathGradeDao.save(mathGrade);

            return true;
        }

        if (SCIENCE.equals(gradeType)) {
            scienceGrade.setId(0);
            scienceGrade.setGrade(grade);
            scienceGrade.setStudentId(id);
            scienceGradeDao.save(scienceGrade);

            return true;
        }

        if (HISTORY.equals(gradeType)) {
            historyGrade.setId(0);
            historyGrade.setGrade(grade);
            historyGrade.setStudentId(id);
            historyGradeDao.save(historyGrade);

            return true;
        }

        return false;
    }

    private static boolean isValidGrade(double grade) {
        return grade >= 0 && grade <= 100;
    }

    public int getIdAndDeleteGrade(int studentId, int id, String gradeType) {
        if (!checkIfStudentIsNull(studentId)) {
            studentId = deleteGrade(id, gradeType);
        } else {
            studentId = 0;
        }

        return studentId;
    }

    private int deleteGrade(int id, String gradeType) {
        int studentId = 0;

        if (MATH.equals(gradeType)) {
            Optional<MathGrade> grade = mathGradeDao.findById(id);

            if (grade.isPresent()) {
                mathGradeDao.deleteById(id);
                studentId = grade.get().getStudentId();
            }
        } else if (SCIENCE.equals(gradeType)) {
            Optional<ScienceGrade> grade = scienceGradeDao.findById(id);

            if (grade.isPresent()) {
                scienceGradeDao.deleteById(id);
                studentId = grade.get().getStudentId();
            }
        } else if (HISTORY.equals(gradeType)) {
            Optional<HistoryGrade> grade = historyGradeDao.findById(id);

            if (grade.isPresent()) {
                historyGradeDao.deleteById(id);
                studentId = grade.get().getStudentId();
            }
        }

        return studentId;
    }

    private int deleteGrade(int id, int studentId, Optional<MathGrade> grade, Class<?> clazz) {
        if (grade.isPresent()) {
            mathGradeDao.deleteById(id);
            studentId = grade.get().getStudentId();
        }

        return studentId;
    }

    public GradebookCollegeStudent studentInformation(int id) {
        if (checkIfStudentIsNull(id)) {
            return null;
        }

        Optional<CollegeStudent> student = studentDao.findById(id);
        Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(id);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradeByStudentId(id);
        Iterable<HistoryGrade> historyGrades = historyGradeDao.findGradeByStudentId(id);

        List<Grade> mathGradeList = new ArrayList<>();
        mathGrades.forEach(mathGradeList::add);

        List<Grade> scienceGradeList = new ArrayList<>();
        scienceGrades.forEach(scienceGradeList::add);

        List<Grade> historyGradeList = new ArrayList<>();
        historyGrades.forEach(historyGradeList::add);

        studentGrades.setMathGradeResults(mathGradeList);
        studentGrades.setScienceGradeResults(scienceGradeList);
        studentGrades.setHistoryGradeResults(historyGradeList);

        return new GradebookCollegeStudent(student.get().getId(),
                student.get().getFirstname(), student.get().getLastname(), student.get().getEmailAddress(), studentGrades);
    }

    public void addModelAttribute(int id, Model m) {
        GradebookCollegeStudent studentEntity = studentInformation(id);

        m.addAttribute("student", studentEntity);

        if (!studentEntity.getStudentGrades().getMathGradeResults().isEmpty()) {
            m.addAttribute("mathAverage", studentEntity.getStudentGrades().findGradePointAverage(
                    studentEntity.getStudentGrades().getMathGradeResults()));
        } else {
            m.addAttribute("mathAverage", "N/A");
        }

        if (!studentEntity.getStudentGrades().getScienceGradeResults().isEmpty()) {
            m.addAttribute("scienceAverage", studentEntity.getStudentGrades().findGradePointAverage(
                    studentEntity.getStudentGrades().getScienceGradeResults()));
        } else {
            m.addAttribute("scienceAverage", "N/A");
        }

        if (!studentEntity.getStudentGrades().getHistoryGradeResults().isEmpty()) {
            m.addAttribute("historyAverage", studentEntity.getStudentGrades().findGradePointAverage(
                    studentEntity.getStudentGrades().getHistoryGradeResults()));
        } else {
            m.addAttribute("historyAverage", "N/A");
        }
    }
}
