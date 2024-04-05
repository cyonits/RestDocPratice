package com.restdoc.restdocpratice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restdoc.restdocpratice.dto.school.CreateSchoolRequestDto;
import com.restdoc.restdocpratice.service.SchoolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureRestDocs
@WebMvcTest(SchoolController.class)
class SchoolControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    SchoolService schoolService;

    @Nested
    @DisplayName("School 생성")
    class CreateSchool {
        @Test
        @DisplayName("School 생성 성공")
        void successCreateSchool() throws Exception {
            // given
            CreateSchoolRequestDto request = new CreateSchoolRequestDto("잠실초등학교", "0212341234", "primary");

            given(schoolService.createSchool(any())).willReturn(1L);

            // when
            ResultActions result = mockMvc.perform(post("/school")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-AUTH-TOKEN","Bearer + User JWT")
                    .content(objectMapper.writeValueAsBytes(request)));

            // then
            result.andExpect(status().isOk())

                    .andDo(print())

                    .andDo(document("/school",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("X-AUTH-TOKEN").description("User JWT")),
                            requestFields(
                                    fieldWithPath("name").type(JsonFieldType.STRING).description("학교 이름"),
                                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("학교 전화번호"),
                                    fieldWithPath("schoolType").type(JsonFieldType.STRING).description("학교 타입 : 초등학교/primary/, 중학교/middle/, 고등학교/high/, 대학/collage, 대학교/university, 대학원/grad"))));
        }
    }
}