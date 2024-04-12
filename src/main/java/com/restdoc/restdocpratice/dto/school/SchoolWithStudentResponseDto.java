package com.restdoc.restdocpratice.dto.school;

import com.restdoc.restdocpratice.entity.School;
import com.restdoc.restdocpratice.entity.Student;
import lombok.Builder;

import java.util.List;

@Builder
public record SchoolWithStudentResponseDto(Long schoolId, String schoolName, String schoolPhoneNumber, String schoolType, List<StudentResponse> students) {
    public static SchoolWithStudentResponseDto of(School school, List<Student> students) {
        return SchoolWithStudentResponseDto.builder()
                .schoolId(school.getId())
                .schoolName(school.getName())
                .schoolPhoneNumber(school.getPhoneNumber())
                .schoolType(school.getSchoolType().type())
                .students(StudentResponse.of(students))
                .build();
    }
}
