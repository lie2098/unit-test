package com.lye.springmvc.repository;

import com.lye.springmvc.models.CollegeStudent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDao extends CrudRepository<CollegeStudent, Integer> {
    CollegeStudent findByEmailAddress(String email);
}
