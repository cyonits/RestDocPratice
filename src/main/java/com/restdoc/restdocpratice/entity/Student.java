package com.restdoc.restdocpratice.entity;

import com.restdoc.restdocpratice.dto.student.UpdateStudentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer grade;

    private Integer classroom;

    private Integer studentNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private School school;

    public void updateStudent(UpdateStudentDto updateStudentDto, School findSchool) {
        this.grade = updateStudentDto.grade();
        this.classroom = updateStudentDto.classroom();
        this.studentNumber = updateStudentDto.studentNumber();
        this.school = findSchool;
    }
}
