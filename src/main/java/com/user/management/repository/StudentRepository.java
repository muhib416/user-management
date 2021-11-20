package com.user.management.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.user.management.model.Student;


@Repository
public interface StudentRepository extends CrudRepository<Student, String> {}
