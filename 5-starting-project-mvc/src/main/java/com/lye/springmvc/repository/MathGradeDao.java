package com.lye.springmvc.repository;

import com.lye.springmvc.models.MathGrade;
import org.springframework.data.repository.CrudRepository;

public interface MathGradeDao extends CrudRepository<MathGrade, Integer> {
    Iterable<MathGrade> findGradeByStudentId(int id);

    void deleteByStudentId(int id);
}
