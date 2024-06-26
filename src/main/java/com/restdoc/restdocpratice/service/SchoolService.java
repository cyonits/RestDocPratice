package com.restdoc.restdocpratice.service;

import com.restdoc.restdocpratice.dto.school.*;
import com.restdoc.restdocpratice.entity.Student;
import com.restdoc.restdocpratice.enums.SchoolType;
import com.restdoc.restdocpratice.entity.School;
import com.restdoc.restdocpratice.exception.CustomRuntimeException;
import com.restdoc.restdocpratice.exception.ErrorCode;
import com.restdoc.restdocpratice.repository.SchoolRepository;
import com.restdoc.restdocpratice.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolService {
    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;
    public Long createSchool(CreateSchoolRequestDto createSchoolRequestDto) {
        if (schoolRepository.existsByPhoneNumber(createSchoolRequestDto.phoneNumber())) {
            throw new CustomRuntimeException(ErrorCode.ALREADY_PHONE_NUMBER);
        }

        School newSchool = School.builder()
                .name(createSchoolRequestDto.name())
                .phoneNumber(createSchoolRequestDto.phoneNumber())
                .schoolType(SchoolType.getSchoolType(createSchoolRequestDto.schoolType()))
                .build();

        return schoolRepository.save(newSchool).getId();
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

    public Long updateSchoolProfile(UpdateSchoolProfileDto updateSchoolProfileDto, MultipartFile multipartFile) {
        School findSchool = schoolRepository.findById(updateSchoolProfileDto.schoolId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_SCHOOL));

        return findSchool.getId();

    }

    public String deleteSchool(Long deleteRequestSchoolId) {
        School findSchool = schoolRepository.findById(deleteRequestSchoolId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_SCHOOL));

        schoolRepository.delete(findSchool);

        return "ok";
    }

    public SchoolWithStudentResponseDto getSchoolListWithStudent(Long schoolId) {
        School findSchool = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_SCHOOL));

        List<Student> students = studentRepository.findAllBySchool(findSchool);

        return SchoolWithStudentResponseDto.of(findSchool, students);
    }
}
