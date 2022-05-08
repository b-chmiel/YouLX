package com.youlx.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class MvcHelpers {
    private final MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    public MvcHelpers(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public <T> ResultActions postRequest(T payload, String url) throws Exception {
        final var value = mapper.writeValueAsString(payload);
        return mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value)
                        .header("host", "asdfga")
                        .header("user-agent", "asdfadsg")
        );
    }

    public ResultActions getRequest(String url) throws Exception {
        return mockMvc.perform(get(url));
    }

    public static String attributeFromResult(String attribute, ResultActions result) throws UnsupportedEncodingException {
        final var response = result.andReturn().getResponse();
        final var value = JsonPath.read(response.getContentAsString(), String.format("$.%s", attribute));
        return value.toString();
    }
}

