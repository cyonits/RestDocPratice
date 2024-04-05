package com.restdoc.restdocpratice.controller;

import com.restdoc.restdocpratice.dto.student.CreateStudentRequestDto;
import com.restdoc.restdocpratice.dto.student.StudentResponseDto;
import com.restdoc.restdocpratice.dto.student.UpdateStudentDto;
import com.restdoc.restdocpratice.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<Long> createStudent(@RequestBody CreateStudentRequestDto createSchoolRequestDto){
        return ResponseEntity.ok(studentService.createStudent(createSchoolRequestDto));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponseDto> getStudent(@PathVariable Long studentId){
        return ResponseEntity.ok(studentService.getStudent(studentId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<StudentResponseDto>> getStudentList(@RequestParam(value = "schId", required = false) List<Long> schoolIdList){
        return ResponseEntity.ok(studentService.getStudentList(schoolIdList));
    }

    @PatchMapping
    public ResponseEntity<Long> updateStudentPhone(@RequestBody UpdateStudentDto updateStudentDto){
        return ResponseEntity.ok(studentService.updateStudent(updateStudentDto));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long studentId){
        return ResponseEntity.ok(studentService.deleteStudent(studentId));
    }
}
