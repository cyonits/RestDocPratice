package com.restdoc.restdocpratice.repository;

import com.restdoc.restdocpratice.entity.School;
import com.restdoc.restdocpratice.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsBySchoolAndGradeAndClassroomAndStudentNumber(School school, Integer grade, Integer classroom, Integer studentNumber);

    List<Student> findAllBySchoolIn(List<School> schoolList);
}
