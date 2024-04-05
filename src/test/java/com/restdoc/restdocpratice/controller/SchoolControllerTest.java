package com.restdoc.restdocpratice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restdoc.restdocpratice.dto.school.CreateSchoolRequestDto;
import com.restdoc.restdocpratice.dto.school.SchoolResponseDto;
import com.restdoc.restdocpratice.entity.School;
import com.restdoc.restdocpratice.enums.SchoolType;
import com.restdoc.restdocpratice.exception.CustomRuntimeException;
import com.restdoc.restdocpratice.exception.ErrorCode;
import com.restdoc.restdocpratice.service.SchoolService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
                .contentType(MediaType.APPLICATION_JSON)
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
                .contentType(MediaType.APPLICATION_JSON)
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
        School school = new School(1L, "잠실초등학교", "0212341234", SchoolType.PRIMARY);

        given(schoolService.getSchool(any())).willReturn(SchoolResponseDto.of(school));

        // when
        ResultActions result = mockMvc.perform(get("/school/{schoolId}", 1)
                .queryParam("school", "example")
                .header("X-AUTH-TOKEN", "Bearer + User JWT"));

        // then
        result.andExpect(status().isOk())

                .andDo(print())

                .andDo(document("get_school_success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("schoolId").description("학교 ID")),
                        queryParameters(
                                parameterWithName("school").description("example")),
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("User JWT")),
                        responseFields(
                                fieldWithPath("schoolId").type(JsonFieldType.NUMBER).description("학교 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("학교 이름"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("학교 전화번호"),
                                fieldWithPath("schoolType").type(JsonFieldType.STRING).description("학교 타입 : 초등학교/primary/, 중학교/middle/, 고등학교/high/, 대학/collage, 대학교/university, 대학원/grad"))));
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
}