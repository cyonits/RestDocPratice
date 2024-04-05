package com.restdoc.restdocpratice.dto.student;

public record UpdateStudentDto(Long studentId, Integer grade, Integer classroom, Integer studentNumber, Long schoolId) {
}
