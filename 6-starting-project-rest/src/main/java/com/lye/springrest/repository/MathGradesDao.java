package com.lye.springrest.repository;

import com.lye.springrest.models.MathGrade;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MathGradesDao extends CrudRepository<MathGrade, Integer> {

    public Iterable<MathGrade> findGradeByStudentId (int id);

    public void deleteByStudentId(int id);
}
