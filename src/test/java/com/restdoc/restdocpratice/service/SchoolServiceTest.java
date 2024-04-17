package com.restdoc.restdocpratice.service;

import com.restdoc.restdocpratice.dto.school.CreateSchoolRequestDto;
import com.restdoc.restdocpratice.dto.school.SchoolResponseDto;
import com.restdoc.restdocpratice.dto.school.UpdateSchoolPhoneDto;
import com.restdoc.restdocpratice.entity.School;
import com.restdoc.restdocpratice.enums.SchoolType;
import com.restdoc.restdocpratice.exception.CustomRuntimeException;
import com.restdoc.restdocpratice.fixture.SchoolFixture;
import com.restdoc.restdocpratice.repository.SchoolRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class SchoolServiceTest {
    @InjectMocks
    SchoolService schoolService;
    @Mock
    SchoolRepository schoolRepository;

    @Nested
    @DisplayName("create_school")
    class createSchool {
        @Test
        @DisplayName("success_create_school")
        void successCreateSchool() {
            // given
            CreateSchoolRequestDto request = new CreateSchoolRequestDto("잠실초등학교", "0212341234", "primary");

            School mockSchool = SchoolFixture.school1();
            given(schoolRepository.save(any(School.class))).willReturn(mockSchool);

            // when
            Long createdSchoolId = schoolService.createSchool(request);

            // then
            assertThat(createdSchoolId).isEqualTo(1L);
        }

        @Test
        @DisplayName("phoneNumber_conflict_409")
        void failCreateSchool() {
            // given
            CreateSchoolRequestDto request = new CreateSchoolRequestDto("잠실초등학교", "0212341234", "primary");

            given(schoolRepository.existsByPhoneNumber(anyString())).willReturn(true);

            // when

            // then
            assertThatThrownBy(() -> schoolService.createSchool(request)).isInstanceOf(CustomRuntimeException.class);
        }
    }

    @Nested
    @DisplayName("find_school")
    class findSchool {
        @Test
        @DisplayName("success_find_school")
        void successFindSchool() {
            // given
            School mockSchool = SchoolFixture.school1();
            given(schoolRepository.findById(anyLong())).willReturn(Optional.of(mockSchool));

            // when
            SchoolResponseDto findSchool = schoolService.getSchool(1L);

            // then
            assertThat(findSchool.schoolId()).isEqualTo(mockSchool.getId());
            assertThat(findSchool.name()).isEqualTo(mockSchool.getName());
            assertThat(findSchool.phoneNumber()).isEqualTo(mockSchool.getPhoneNumber());
            assertThat(findSchool.schoolType()).isEqualTo(mockSchool.getSchoolType().type());
        }

        @Test
        @DisplayName("fail_find_school_404")
        void failFindSchool() {
            // given
            given(schoolRepository.findById(anyLong())).willReturn(Optional.empty());

            // when

            // then
            assertThatThrownBy(() -> schoolService.getSchool(1L)).isInstanceOf(CustomRuntimeException.class);
        }
    }

    @Nested
    @DisplayName("find_school_list")
    class findSchoolList {
        @Test
        @DisplayName("success_find_school_list")
        void successFindSchoolList() {
            // given
            List<School> mockSchools = List.of(SchoolFixture.school1(), SchoolFixture.school2(), SchoolFixture.school3());
            given(schoolRepository.findAll()).willReturn(mockSchools);

            // when
            List<SchoolResponseDto> findSchools = schoolService.getSchoolList(null);

            // then
            assertThat(findSchools.size()).isEqualTo(mockSchools.size());
            for (int i = 0; i < findSchools.size(); i++) {
                assertThat(findSchools.get(i).schoolId()).isEqualTo(mockSchools.get(i).getId());
                assertThat(findSchools.get(i).name()).isEqualTo(mockSchools.get(i).getName());
                assertThat(findSchools.get(i).phoneNumber()).isEqualTo(mockSchools.get(i).getPhoneNumber());
                assertThat(findSchools.get(i).schoolType()).isEqualTo(mockSchools.get(i).getSchoolType().type());
            }
        }

        @Test
        @DisplayName("success_find_school_list_with_filter")
        void successFindSchoolListWithFilter() {
            // given
            List<String> typeFilters = List.of("primary", "middle");
            List<School> mockSchools = List.of(SchoolFixture.school1(), SchoolFixture.school2());
            given(schoolRepository.findAllBySchoolTypeIn(anyList())).willReturn(mockSchools);

            // when
            List<SchoolResponseDto> findSchools = schoolService.getSchoolList(typeFilters);

            // then
            assertThat(findSchools.size()).isEqualTo(mockSchools.size());
            for (int i = 0; i < findSchools.size(); i++) {
                assertThat(findSchools.get(i).schoolId()).isEqualTo(mockSchools.get(i).getId());
                assertThat(findSchools.get(i).name()).isEqualTo(mockSchools.get(i).getName());
                assertThat(findSchools.get(i).phoneNumber()).isEqualTo(mockSchools.get(i).getPhoneNumber());
                assertThat(findSchools.get(i).schoolType()).isEqualTo(mockSchools.get(i).getSchoolType().type());
            }
        }


        @Test
        @DisplayName("fail_find_school_list_400")
        void failFindSchoolList() {
            // given
            List<String> typeFilters = List.of("bad", "filter");

            // when

            // then
            assertThatThrownBy(() -> schoolService.getSchoolList(typeFilters)).isInstanceOf(CustomRuntimeException.class);
        }
    }

    @Nested
    @DisplayName("update_school_phone")
    class updateSchoolPhone {
        @Test
        @DisplayName("success_update_school_phone")
        void successUpdatePhoneNumber() {
            // given
            UpdateSchoolPhoneDto request = new UpdateSchoolPhoneDto(1L, "0212341234");
            School mockSchool = SchoolFixture.school1();
            given(schoolRepository.findById(anyLong())).willReturn(Optional.of(mockSchool));

            // when
            Long updatedSchoolId = schoolService.updateSchoolPhone(request);

            // then
            assertThat(updatedSchoolId).isEqualTo(mockSchool.getId());
        }

        @Test
        @DisplayName("fail_update_school_phone_409")
        void failPhoneNumberUpdate409() {
            // given
            UpdateSchoolPhoneDto request = new UpdateSchoolPhoneDto(1L, "0212341234");
            given(schoolRepository.existsByPhoneNumber(anyString())).willReturn(true);

            // when

            // then
            assertThatThrownBy(() -> schoolService.updateSchoolPhone(request)).isInstanceOf(CustomRuntimeException.class);
        }

        @Test
        @DisplayName("fail_update_school_phone_404")
        void failPhoneNumberUpdate404() {
            // given
            UpdateSchoolPhoneDto request = new UpdateSchoolPhoneDto(1L, "0212341234");
            given(schoolRepository.findById(anyLong())).willReturn(Optional.empty());

            // when

            // then
            assertThatThrownBy(() -> schoolService.updateSchoolPhone(request)).isInstanceOf(CustomRuntimeException.class);
        }
    }

    @Nested
    @DisplayName("delete_school")
    class deleteSchool {
        @Test
        @DisplayName("success_delete_school")
        void successUpdatePhoneNumber() {
            // given
            School mockSchool = SchoolFixture.school1();
            given(schoolRepository.findById(anyLong())).willReturn(Optional.of(mockSchool));

            // when

            // then
            assertThat(schoolService.deleteSchool(1L)).isEqualTo("ok");
        }

        @Test
        @DisplayName("fail_delete_school_404")
        void failPhoneNumberUpdate404() {
            // given
            given(schoolRepository.findById(anyLong())).willReturn(Optional.empty());

            // when

            // then
            assertThatThrownBy(() -> schoolService.deleteSchool(1L)).isInstanceOf(CustomRuntimeException.class);
        }
    }
}