package com.ll.kotudy.document.utils;

import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.payload.AbstractFieldsSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CustomResponseFieldsSnippet extends AbstractFieldsSnippet {

    protected CustomResponseFieldsSnippet(String type, List<FieldDescriptor> descriptors,
                                          Map<String, Object> attributes, boolean ignoreUndocumentedFields,
                                          PayloadSubsectionExtractor<?> subsectionExtractor) {
        super(type, descriptors, attributes, ignoreUndocumentedFields, subsectionExtractor);
    }

    @Override
    protected MediaType getContentType(Operation operation) {
        return null;
    }

    @Override
    protected byte[] getContent(Operation operation) throws IOException {
        return new byte[0];
    }
}
