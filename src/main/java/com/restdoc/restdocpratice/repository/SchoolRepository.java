package com.restdoc.restdocpratice.repository;

import com.restdoc.restdocpratice.enums.SchoolType;
import com.restdoc.restdocpratice.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolRepository extends JpaRepository<School, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    List<School> findAllBySchoolTypeIn(List<SchoolType> schoolTypeList);

    List<School> findAllByIdIn(List<Long> schoolIdList);
}
