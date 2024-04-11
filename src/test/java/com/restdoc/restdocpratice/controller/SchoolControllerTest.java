package com.restdoc.restdocpratice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restdoc.restdocpratice.dto.school.CreateSchoolRequestDto;
import com.restdoc.restdocpratice.dto.school.SchoolResponseDto;
import com.restdoc.restdocpratice.dto.school.UpdateSchoolPhoneDto;
import com.restdoc.restdocpratice.dto.school.UpdateSchoolProfileDto;
import com.restdoc.restdocpratice.entity.School;
import com.restdoc.restdocpratice.exception.CustomRuntimeException;
import com.restdoc.restdocpratice.exception.ErrorCode;
import com.restdoc.restdocpratice.fixture.FileFixture;
import com.restdoc.restdocpratice.fixture.SchoolFixture;
import com.restdoc.restdocpratice.service.SchoolService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(SchoolController.class)
class SchoolControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    SchoolService schoolService;

    @Test
    @DisplayName("create_school_success")
    void successCreateSchool() throws Exception {
        // given
        CreateSchoolRequestDto request = new CreateSchoolRequestDto("잠실초등학교", "0212341234", "primary");

        given(schoolService.createSchool(any())).willReturn(1L);

        // when
        ResultActions result = mockMvc.perform(post("/school")
                .contentType(APPLICATION_JSON)
                .header("X-AUTH-TOKEN", "Bearer + User JWT")
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andExpect(status().isOk())

                .andDo(print())

                .andDo(document("create_school_success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("User JWT")),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("학교 이름"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("학교 전화번호"),
                                fieldWithPath("schoolType").type(JsonFieldType.STRING).description("학교 타입 : 초등학교/primary/, 중학교/middle/, 고등학교/high/, 대학/collage, 대학교/university, 대학원/grad"))));
    }

    @Test
    @DisplayName("phoneNumber_conflict_409")
    void failCreateSchool() throws Exception {
        // given
        CreateSchoolRequestDto request = new CreateSchoolRequestDto("잠실초등학교", "0212341234", "primary");

        given(schoolService.createSchool(any())).willThrow(new CustomRuntimeException(ErrorCode.ALREADY_PHONE_NUMBER));

        // when
        ResultActions result = mockMvc.perform(post("/school")
                .contentType(APPLICATION_JSON)
                .header("X-AUTH-TOKEN", "Bearer + User JWT")
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andExpect(status().isConflict())

                .andDo(print())

                .andDo(document("phoneNumber_conflict_409",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("get_school_success")
    void successGetSchool() throws Exception {
        // given
        School school = SchoolFixture.school1();

        given(schoolService.getSchool(any())).willReturn(SchoolResponseDto.of(school));

        // when
        ResultActions result = mockMvc.perform(get("/school/{schoolId}", 1)
                .header("X-AUTH-TOKEN", "Bearer + User JWT"));

        // then
        result.andExpect(status().isOk())

                .andDo(print())

                .andDo(document("get_school_success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("schoolId").description("학교 ID")),
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("User JWT")),
                        responseFields(
                                fieldWithPath("schoolId").type(JsonFieldType.NUMBER).description("학교 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("학교 이름"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("학교 전화번호"),
                                fieldWithPath("schoolType").type(JsonFieldType.STRING).description("학교 타입 : 초등학교/primary/, 중학교/middle/, 고등학교/high/, 대학/collage, 대학교/university, 대학원/grad"))));
    }

    @Test
    @DisplayName("get_school_list_success")
    void successGetSchoolList() throws Exception {
        // given
        School school1 = SchoolFixture.school1();
        School school2 = SchoolFixture.school2();
        School school3 =SchoolFixture.school3();

        given(schoolService.getSchoolList(any())).willReturn(List.of(
                SchoolResponseDto.of(school1),
                SchoolResponseDto.of(school2),
                SchoolResponseDto.of(school3)));

        // when
        ResultActions result = mockMvc.perform(get("/school/list")
                .queryParam("schType", "primary")
                .queryParam("schType", "middle")
                .queryParam("schType", "high")
                .header("X-AUTH-TOKEN", "Bearer + User JWT"));

        // then
        result.andExpect(status().isOk())

                .andDo(print())

                .andDo(document("get_school_list_success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("schType").description("학교 타입 : 초등학교/primary/, 중학교/middle/, 고등학교/high/, 대학/collage, 대학교/university, 대학원/grad").optional()),
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("User JWT")),
                        responseFields(
                                fieldWithPath("[].schoolId").type(JsonFieldType.NUMBER).description("학교 ID"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("학교 이름"),
                                fieldWithPath("[].phoneNumber").type(JsonFieldType.STRING).description("학교 전화번호"),
                                fieldWithPath("[].schoolType").type(JsonFieldType.STRING).description("학교 타입 : 초등학교/primary/, 중학교/middle/, 고등학교/high/, 대학/collage, 대학교/university, 대학원/grad"))));
    }

    @Test
    @DisplayName("patch_school_phone_success")
    void patchSchoolPhoneSuccess() throws Exception {
        // given
        UpdateSchoolPhoneDto request = new UpdateSchoolPhoneDto(1L, "0212341234");

        given(schoolService.updateSchoolPhone(any())).willReturn(1L);

        // when
        ResultActions result = mockMvc.perform(patch("/school/phone")
                .contentType(APPLICATION_JSON)
                .header("X-AUTH-TOKEN", "Bearer + User JWT")
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andExpect(status().isOk())

                .andDo(print())

                .andDo(document("patch_school_phone_success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("User JWT")),
                        requestFields(
                                fieldWithPath("schoolId").type(JsonFieldType.NUMBER).description("학교 아이디"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("학교 전화번호"))));
    }

    @Test
    @DisplayName("post_school_profile_success")
    public void schoolPhotoUploadSuccess() throws Exception {
        //given
        UpdateSchoolProfileDto request = new UpdateSchoolProfileDto(1L);
        MockMultipartFile requestJson = new MockMultipartFile(
                "request",
                "jsonData",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(request).getBytes());

        MockMultipartFile image = FileFixture.image();

        given(schoolService.updateSchoolProfile(any(), any())).willReturn(1L);

        //when
        ResultActions result = mockMvc.perform(multipart("/school/photo")
                .file(requestJson)
                .file(image)
                .header("X-AUTH-TOKEN", "Bearer + User JWT")
                .contentType(MediaType.MULTIPART_FORM_DATA));

        //then
        result.andExpect(status().isOk())

                .andDo(print())

                .andDo(document("post_school_profile_success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("User JWT")),
                        requestParts(
                                partWithName("image").description("이미지"),
                                partWithName("request").description("학교정보")),
                        requestPartFields("request",
                                fieldWithPath("schoolId").type(JsonFieldType.NUMBER).description("학교 아이디"))));
    }

    @Test
    @DisplayName("delete_school_success")
    void deleteSchoolSuccess() throws Exception {
        // given
        given(schoolService.deleteSchool(any())).willReturn("ok");

        // when
        ResultActions result = mockMvc.perform(delete("/school/{schoolId}",1)
                .header("X-AUTH-TOKEN", "Bearer + User JWT"));

        // then
        result.andExpect(status().isOk())

                .andDo(print())

                .andDo(document("delete_school_success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("User JWT"))));
    }

    @Test
    @DisplayName("notFound_school_404")
    void failGetSchool() throws Exception {
        // given

        given(schoolService.getSchool(any())).willThrow(new CustomRuntimeException(ErrorCode.NOT_FOUND_SCHOOL));

        // when
        ResultActions result = mockMvc.perform(get("/school/{schoolId}",1)
                .header("X-AUTH-TOKEN", "Bearer + User JWT"));

        // then
        result.andExpect(status().isNotFound())

                .andDo(print())

                .andDo(document("notFound_school_404",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("wired_school_type_400")
    void wiredSchoolType() throws Exception {
        // given

        given(schoolService.getSchoolList(any())).willThrow(new CustomRuntimeException(ErrorCode.WIRED_SCHOOL_TYPE));

        // when
        ResultActions result = mockMvc.perform(get("/school/list")
                .header("X-AUTH-TOKEN", "Bearer + User JWT"));

        // then
        result.andExpect(status().isBadRequest())

                .andDo(print())

                .andDo(document("wired_school_type_400",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}