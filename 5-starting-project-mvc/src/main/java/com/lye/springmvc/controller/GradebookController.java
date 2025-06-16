package com.lye.springmvc.controller;

import com.lye.springmvc.models.CollegeStudent;
import com.lye.springmvc.models.Gradebook;
import com.lye.springmvc.service.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GradebookController {

    @Autowired
    private Gradebook gradebook;

    @Autowired
    private StudentAndGradeService studentAndGradeService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getStudents(Model m) {
        Iterable<CollegeStudent> iterable = studentAndGradeService.getGradebook();
        m.addAttribute("students", iterable);

        return "index";
    }


    @PostMapping("/")
    public String createStudent(@ModelAttribute("student") CollegeStudent student, Model m) {
        studentAndGradeService.createStudent(student.getFirstname(), student.getLastname(), student.getEmailAddress());
        Iterable<CollegeStudent> collegeStudents = studentAndGradeService.getGradebook();
        m.addAttribute("students", collegeStudents);

        return "index";
    }

    @GetMapping("/delete/student/{id}")
    public String deleteStudent(@PathVariable int id, Model m) {
        if (studentAndGradeService.checkIfStudentIsNull(id)) {
            return "error";
        }

        studentAndGradeService.deleteStudent(id);

        Iterable<CollegeStudent> collegeStudents = studentAndGradeService.getGradebook();
        m.addAttribute("students", collegeStudents);

        return "index";
    }

    @GetMapping("/studentInformation/{id}")
    public String studentInformation(@PathVariable int id, Model m) {
        if (studentAndGradeService.checkIfStudentIsNull(id)) {
            return "error";
        }

        studentAndGradeService.addModelAttribute(id, m);

        return "studentInformation";
    }

    @PostMapping("/grades")
    public String createGrade(@RequestParam("grade") double grade,
                              @RequestParam("gradeType") String gradeType,
                              @RequestParam("studentId") int id,
                              Model m) {
        if (studentAndGradeService.checkIfStudentIsNull(id)) {
            return "error";
        }

        boolean success = studentAndGradeService.createGrade(grade, id, gradeType);

        if (!success) {
            return "error";
        }

        studentAndGradeService.addModelAttribute(id, m);

        return "studentInformation";
    }

    @GetMapping("student/{studentId}/grades/{id}/{gradeType}")
    public String deleteGrade(@PathVariable("studentId") int studentId,
                              @PathVariable("id") int id,
                              @PathVariable("gradeType") String gradeType,
                              Model m) {
        studentId = studentAndGradeService.getIdAndDeleteGrade(studentId, id, gradeType);

        if(studentId == 0) {
            return "error";
        }

        studentAndGradeService.addModelAttribute(studentId, m);

        return "studentInformation";
    }
}
