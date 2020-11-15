package com.nikita.testproject;

import com.nikita.testproject.controller.AuthController;
import com.nikita.testproject.request.UserAccRequest;
import javafx.application.Application;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @Autowired(required=true)
    private MockMvc mvc;
    @Test
    public  void registerUser() throws Exception {
        RequestBuilder request =MockMvcRequestBuilders.post("/register")
                .content(
                        "{\"login\":\"niki555\",\"password\":\"nika787\",\"email\":\"nikita-andrey@mail.ru\"}"
                                .getBytes())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");
      //  System.out.println(request);
        mvc.perform(request).andDo(print()).andExpect(status().isOk());

    }

    @Test
    public  void registerUser_bad_request() throws Exception {
        RequestBuilder request =MockMvcRequestBuilders.post("/register")
                .content(
                        "{\"login\":\"内容\",\"password\":\"true\",\"email\":\"1\"}"
                                .getBytes())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");
        //  System.out.println(request);
        mvc.perform(request).andDo(print()).andExpect(status().isNotAcceptable());

    }


}
