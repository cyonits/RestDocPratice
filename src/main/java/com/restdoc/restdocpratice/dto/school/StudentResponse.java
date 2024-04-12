package com.restdoc.restdocpratice.dto.school;

import com.restdoc.restdocpratice.entity.Student;
import lombok.Builder;

import java.util.List;

@Builder
public record StudentResponse(Long studentId, String name, Integer grade, Integer classroom, Integer studentNumber) {
    public static List<StudentResponse> of(List<Student> students) {
        return students.stream()
                .map(student -> StudentResponse.builder()
                        .studentId(student.getId())
                        .name(student.getName())
                        .grade(student.getGrade())
                        .classroom(student.getClassroom())
                        .studentNumber(student.getStudentNumber())
                        .build()
                ).toList();
    }
}
