package com.unibuc.fmi.mycinemamvc.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void getByIdSuccessTest() throws Exception {
        mockMvc.perform(get("/movies/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("movieDetails"));
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void getByIdNotFoundTest() throws Exception {
        mockMvc.perform(get("/movies/{id}", "5"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("notFoundException"));
    }
}
