package com.restdoc.restdocpratice.config;

import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.payload.AbstractFieldsSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CustomSnippet extends AbstractFieldsSnippet {

    public CustomSnippet(
            String type, List<FieldDescriptor> descriptors,
            boolean ignoreUndocumentedFields) {

        super(type, descriptors, null, ignoreUndocumentedFields);
    }

    @Override
    protected MediaType getContentType(Operation operation) {
        return operation.getResponse().getHeaders().getContentType();
    }

    @Override
    protected byte[] getContent(Operation operation) throws IOException {
        return operation.getResponse().getContent();
    }

    public static CustomSnippet customSnippetFields(String snippetFilePrefix, FieldDescriptor... descriptors) {
        return new CustomSnippet(snippetFilePrefix, Arrays.asList(descriptors), true);
    }
}
