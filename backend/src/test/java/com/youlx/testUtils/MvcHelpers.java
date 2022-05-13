package com.youlx.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.util.Map;

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
        final var content = mapper.writeValueAsString(payload);
        return mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("host", "asdfga")
                        .header("user-agent", "asdfadsg")
        );
    }

    public ResultActions postFormRequest(Map<String, String> params, String url) throws Exception {
        final var content = EntityUtils.toString(
                new UrlEncodedFormEntity(
                        params
                                .entrySet()
                                .stream()
                                .map(
                                        it -> new BasicNameValuePair(it.getKey(), it.getValue())
                                ).toList()
                )
        );

        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(content)
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

