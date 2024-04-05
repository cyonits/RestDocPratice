package com.restdoc.restdocpratice.controller;

import com.restdoc.restdocpratice.dto.school.CreateSchoolRequestDto;
import com.restdoc.restdocpratice.dto.school.SchoolResponseDto;
import com.restdoc.restdocpratice.dto.school.UpdateSchoolPhoneDto;
import com.restdoc.restdocpratice.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/school")
public class SchoolController {

    private final SchoolService schoolService;

    @PostMapping
    public ResponseEntity<Long> createSchool(@RequestBody CreateSchoolRequestDto createSchoolRequestDto){
        return ResponseEntity.ok(schoolService.createSchool(createSchoolRequestDto));
    }

    @GetMapping("/{schoolId}")
    public ResponseEntity<SchoolResponseDto> getSchool(@PathVariable Long schoolId){
        return ResponseEntity.ok(schoolService.getSchool(schoolId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<SchoolResponseDto>> getSchoolList(@RequestParam(value = "schType", required = false) List<String> schoolTypeList ){
        return ResponseEntity.ok(schoolService.getSchoolList(schoolTypeList));
    }

    @PatchMapping("/phone")
    public ResponseEntity<Long> updateSchoolPhone(@RequestBody UpdateSchoolPhoneDto updateSchoolPhoneDto){
        return ResponseEntity.ok(schoolService.updateSchoolPhone(updateSchoolPhoneDto));
    }

    @DeleteMapping("/{schoolId}")
    public ResponseEntity<String> deleteSchool(@PathVariable Long schoolId){
        return ResponseEntity.ok(schoolService.deleteSchool(schoolId));
    }
}
