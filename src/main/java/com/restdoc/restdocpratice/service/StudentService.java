package com.restdoc.restdocpratice.service;

import com.restdoc.restdocpratice.dto.student.CreateStudentRequestDto;
import com.restdoc.restdocpratice.dto.student.StudentResponseDto;
import com.restdoc.restdocpratice.dto.student.UpdateStudentDto;
import com.restdoc.restdocpratice.entity.School;
import com.restdoc.restdocpratice.entity.Student;
import com.restdoc.restdocpratice.exception.CustomRuntimeException;
import com.restdoc.restdocpratice.exception.ErrorCode;
import com.restdoc.restdocpratice.repository.SchoolRepository;
import com.restdoc.restdocpratice.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {
    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;
    public Long createStudent(CreateStudentRequestDto createStudentRequestDto) {
        School findSchool = schoolRepository.findById(createStudentRequestDto.schoolId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_SCHOOL));

        if (studentRepository.existsBySchoolAndGradeAndClassroomAndStudentNumber
                (findSchool, createStudentRequestDto.grade(), createStudentRequestDto.classroom(), createStudentRequestDto.studentNumber())){
            throw new CustomRuntimeException(ErrorCode.ALREADY_STUDENT);
        }

        Student newStudent = Student.builder()
                .name(createStudentRequestDto.name())
                .grade(createStudentRequestDto.grade())
                .classroom(createStudentRequestDto.classroom())
                .studentNumber(createStudentRequestDto.studentNumber())
                .school(findSchool)
                .build();

        studentRepository.save(newStudent);

        return newStudent.getId();
    }

    public StudentResponseDto getStudent(Long findRequestStudentId) {
        Student student = studentRepository.findById(findRequestStudentId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_STUDENT));

        return StudentResponseDto.of(student, student.getSchool());
    }

    public List<StudentResponseDto> getStudentList(List<Long> schoolIdList) {
        if (schoolIdList == null || schoolIdList.isEmpty()) {
            return studentRepository.findAll().stream()
                    .map(student -> StudentResponseDto.of(student, student.getSchool()))
                    .toList();
        }

        List<School> schoolList = schoolRepository.findAllByIdIn(schoolIdList);

        if (schoolList == null || schoolList.size() != schoolIdList.size()) {
            throw new CustomRuntimeException(ErrorCode.NO_ID_SCHOOL);
        }

        return studentRepository.findAllBySchoolIn(schoolList).stream()
                .map(student -> StudentResponseDto.of(student, student.getSchool()))
                .toList();
    }

    public Long updateStudent(UpdateStudentDto updateStudentDto) {

        Student findStudent = studentRepository.findById(updateStudentDto.studentId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_STUDENT));

        School findSchool = schoolRepository.findById(updateStudentDto.schoolId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_SCHOOL));

        if (studentRepository.existsBySchoolAndGradeAndClassroomAndStudentNumber
                (findSchool, updateStudentDto.grade(), updateStudentDto.classroom(), updateStudentDto.studentNumber())){
            throw new CustomRuntimeException(ErrorCode.ALREADY_STUDENT);
        }

        findStudent.updateStudent(updateStudentDto, findSchool);

        return findStudent.getId();
    }

    public String deleteStudent(Long deleteRequestStudentId) {
        Student findStudent = studentRepository.findById(deleteRequestStudentId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_STUDENT));

        studentRepository.delete(findStudent);

        return "ok";
    }
}
