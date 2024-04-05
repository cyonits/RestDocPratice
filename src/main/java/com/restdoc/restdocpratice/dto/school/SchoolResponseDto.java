package com.restdoc.restdocpratice.dto.school;

import com.restdoc.restdocpratice.entity.School;
import lombok.Builder;

@Builder
public record SchoolResponseDto(Long schoolId, String name, String phoneNumber, String schoolType) {
    public static SchoolResponseDto of(School school) {
        return SchoolResponseDto.builder()
                .schoolId(school.getId())
                .name(school.getName())
                .phoneNumber(school.getPhoneNumber())
                .schoolType(school.getSchoolType().type())
                .build();
    }
}
