package com.lye.springmvc.repository;

import com.lye.springmvc.models.HistoryGrade;
import org.springframework.data.repository.CrudRepository;

public interface HistoryGradeDao extends CrudRepository<HistoryGrade, Integer> {
    Iterable<HistoryGrade> findGradeByStudentId(int id);

    void deleteByStudentId(int id);
}
