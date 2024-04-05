package com.restdoc.restdocpratice.service;

import com.restdoc.restdocpratice.enums.SchoolType;
import com.restdoc.restdocpratice.dto.school.CreateSchoolRequestDto;
import com.restdoc.restdocpratice.dto.school.SchoolResponseDto;
import com.restdoc.restdocpratice.dto.school.UpdateSchoolPhoneDto;
import com.restdoc.restdocpratice.entity.School;
import com.restdoc.restdocpratice.exception.CustomRuntimeException;
import com.restdoc.restdocpratice.exception.ErrorCode;
import com.restdoc.restdocpratice.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolService {
    private final SchoolRepository schoolRepository;
    public Long createSchool(CreateSchoolRequestDto createSchoolRequestDto) {
        if (schoolRepository.existsByPhoneNumber(createSchoolRequestDto.phoneNumber())) {
            throw new CustomRuntimeException(ErrorCode.ALREADY_PHONE_NUMBER);
        }

        School newSchool = School.builder()
                .name(createSchoolRequestDto.name())
                .phoneNumber(createSchoolRequestDto.phoneNumber())
                .schoolType(SchoolType.getSchoolType(createSchoolRequestDto.schoolType()))
                .build();

        schoolRepository.save(newSchool);

        return newSchool.getId();
    }

    public SchoolResponseDto getSchool(Long findRequestSchoolId) {
        School findSchool = schoolRepository.findById(findRequestSchoolId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_SCHOOL));

        return SchoolResponseDto.of(findSchool);
    }

    public List<SchoolResponseDto> getSchoolList(List<String> schoolTypeList) {
        if (schoolTypeList == null || schoolTypeList.isEmpty()) {
            return schoolRepository.findAll().stream()
                    .map(SchoolResponseDto::of)
                    .toList();
        }

        List<SchoolType> schoolTypes = schoolTypeList.stream().map(SchoolType::getSchoolType).toList();

        if (schoolTypes.contains(null)){
            throw new CustomRuntimeException(ErrorCode.WIRED_SCHOOL_TYPE);
        }

        return schoolRepository.findAllBySchoolTypeIn(schoolTypes).stream()
                .map(SchoolResponseDto::of)
                .toList();
    }

    public Long updateSchoolPhone(UpdateSchoolPhoneDto updateSchoolPhoneDto) {
        if (schoolRepository.existsByPhoneNumber(updateSchoolPhoneDto.phoneNumber())) {
            throw new CustomRuntimeException(ErrorCode.ALREADY_PHONE_NUMBER);
        }

        School findSchool = schoolRepository.findById(updateSchoolPhoneDto.schoolId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_SCHOOL));

        findSchool.updatePhone(updateSchoolPhoneDto.phoneNumber());

        return findSchool.getId();
    }

    public String deleteSchool(Long deleteRequestSchoolId) {
        School findSchool = schoolRepository.findById(deleteRequestSchoolId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_SCHOOL));

        schoolRepository.delete(findSchool);

        return "ok";
    }
}
