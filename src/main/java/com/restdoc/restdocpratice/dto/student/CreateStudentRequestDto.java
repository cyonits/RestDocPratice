package com.restdoc.restdocpratice.dto.student;

public record CreateStudentRequestDto(String name, Integer grade, Integer classroom, Integer studentNumber, Long schoolId) {
}
