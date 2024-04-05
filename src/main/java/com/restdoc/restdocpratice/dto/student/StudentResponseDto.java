package com.restdoc.restdocpratice.dto.student;

import com.restdoc.restdocpratice.dto.school.SchoolResponseDto;
import com.restdoc.restdocpratice.entity.School;
import com.restdoc.restdocpratice.entity.Student;
import lombok.Builder;

@Builder
public record StudentResponseDto(Long studentId, String name, Integer grade, Integer classroom, Integer studentNumber, String schoolName, String schoolType) {

    public static StudentResponseDto of(Student student, School school) {
        return StudentResponseDto.builder()
                .studentId(student.getId())
                .name(student.getName())
                .grade(student.getGrade())
                .classroom(student.getClassroom())
                .studentNumber(student.getStudentNumber())
                .schoolName(school.getName())
                .schoolType(school.getSchoolType().type())
                .build();
    }
}
