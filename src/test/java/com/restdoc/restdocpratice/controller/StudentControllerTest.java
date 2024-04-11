package com.restdoc.restdocpratice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restdoc.restdocpratice.dto.school.CreateSchoolRequestDto;
import com.restdoc.restdocpratice.dto.student.CreateStudentRequestDto;
import com.restdoc.restdocpratice.exception.CustomRuntimeException;
import com.restdoc.restdocpratice.exception.ErrorCode;
import com.restdoc.restdocpratice.service.StudentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(StudentController.class)
class StudentControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    StudentService studentService;
    @Test
    @DisplayName("create_student_success")
    void successCreateStudent() throws Exception {
        // given
        CreateStudentRequestDto request = new CreateStudentRequestDto("홍길동", 1, 1, 1, 1L);

        given(studentService.createStudent(any())).willReturn(1L);

        // when
        ResultActions result = mockMvc.perform(post("/student")
                .header("X-AUTH-TOKEN", "Bearer + User JWT")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andExpect(status().isOk())

                .andDo(print())

                .andDo(document("create_student_success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("User JWT")),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("학생 이름"),
                                fieldWithPath("grade").type(JsonFieldType.NUMBER).description("학년"),
                                fieldWithPath("classroom").type(JsonFieldType.NUMBER).description("반"),
                                fieldWithPath("studentNumber").type(JsonFieldType.NUMBER).description("번호"),
                                fieldWithPath("schoolId").type(JsonFieldType.NUMBER).description("학교 Id"))));
    }

    @Test
    @DisplayName("already_student_409")
    void failCreateStudent() throws Exception {
        // given
        CreateStudentRequestDto request = new CreateStudentRequestDto("홍길동", 1, 1, 1, 1L);

        given(studentService.createStudent(any())).willThrow( new CustomRuntimeException(ErrorCode.ALREADY_STUDENT));

        // when
        ResultActions result = mockMvc.perform(post("/student")
                .header("X-AUTH-TOKEN", "Bearer + User JWT")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andExpect(status().isConflict())

                .andDo(print())

                .andDo(document("already_student_409",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}