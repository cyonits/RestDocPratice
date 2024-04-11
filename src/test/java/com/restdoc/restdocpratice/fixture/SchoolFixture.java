package com.restdoc.restdocpratice.fixture;

import com.restdoc.restdocpratice.entity.School;
import com.restdoc.restdocpratice.enums.SchoolType;

public class SchoolFixture {
    public static School school1(){
        return new School(1L, "잠실초등학교", "0212341234", SchoolType.PRIMARY);
    }

    public static School school2(){
        return new School(2L, "잠실중학교", "0278907890", SchoolType.MIDDLE);
    }

    public static School school3(){
        return new School(3L, "잠실고등학교", "0134563456", SchoolType.HIGH);
    }
}
