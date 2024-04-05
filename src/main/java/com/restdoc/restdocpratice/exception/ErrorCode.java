package com.restdoc.restdocpratice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    WIRED_SCHOOL_TYPE(HttpStatus.BAD_REQUEST, "올바르지 않은 School Type이 포함되어 있습니다."),
    NO_ID_SCHOOL(HttpStatus.BAD_REQUEST, "존재하지 않는 SchoolId가 요청에 포함되어 있습니다."),

    NOT_FOUND_SCHOOL(HttpStatus.NOT_FOUND, "School을 찾을 수 없습니다."),
    NOT_FOUND_STUDENT(HttpStatus.NOT_FOUND, "Studnet를 찾을 수 없습니다."),

    ALREADY_PHONE_NUMBER(HttpStatus.CONFLICT, "이미 존재하는 전화번호 입니다."),
    ALREADY_STUDENT(HttpStatus.CONFLICT, "해당 학교, 학년, 반, 번호에 이미 학생이 등록되어 있습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
