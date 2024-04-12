package com.restdoc.restdocpratice.fixture;

import com.restdoc.restdocpratice.entity.Student;

public class StudentFixture {
    public static Student student1(){return new Student(1L, "홍길동", 1, 1, 1, SchoolFixture.school1());}
    public static Student student2(){return new Student(3L, "김철수", 2, 2, 2, SchoolFixture.school1());}
    public static Student student3(){return new Student(2L, "박영희", 3, 3, 3, SchoolFixture.school1());}

}
