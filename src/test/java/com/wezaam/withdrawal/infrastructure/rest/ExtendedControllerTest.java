package com.wezaam.withdrawal.infrastructure.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ExtendedControllerTest {

    @Autowired protected MockMvc mvc;

    @Autowired protected ObjectMapper mapper;

    protected ResultActions requestBody(MockHttpServletRequestBuilder requestBuilder, String body)
            throws Exception {
        return mvc.perform(
                        requestBuilder.contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
                .andDo(print());
    }

    protected ResultActions request(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return mvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());
    }
}
