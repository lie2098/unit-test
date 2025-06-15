package com.lye.springmvc.repository;

import com.lye.springmvc.models.ScienceGrade;
import org.springframework.data.repository.CrudRepository;

public interface ScienceGradeDao extends CrudRepository<ScienceGrade, Integer> {
    Iterable<ScienceGrade> findGradeByStudentId(int studentId);

    void deleteByStudentId(int id);
}
