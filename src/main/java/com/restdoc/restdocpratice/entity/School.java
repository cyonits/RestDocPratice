package com.restdoc.restdocpratice.entity;

import com.restdoc.restdocpratice.enums.SchoolType;
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
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private SchoolType schoolType;

    public void updatePhone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
