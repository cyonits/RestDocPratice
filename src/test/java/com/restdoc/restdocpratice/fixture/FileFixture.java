package com.restdoc.restdocpratice.fixture;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class FileFixture {
    public static MockMultipartFile image() {
        return new MockMultipartFile("image", "school.jpg", MediaType.IMAGE_PNG_VALUE, "<<image>>".getBytes());
    }
}
