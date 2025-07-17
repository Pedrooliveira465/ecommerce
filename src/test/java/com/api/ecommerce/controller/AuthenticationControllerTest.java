package com.api.ecommerce.controller;

import com.api.ecommerce.modules.user.entities.User;
import com.api.ecommerce.modules.user.dtos.AuthenticationRequestDTO;
import com.api.ecommerce.modules.user.dtos.UserRequestDTO;
import com.api.ecommerce.modules.user.enums.Role;
import com.api.ecommerce.modules.user.controllers.AuthenticationController;
import com.api.ecommerce.modules.user.services.AuthenticationService;
import com.api.ecommerce.modules.user.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequestDTO userRequestDTO;
    private AuthenticationRequestDTO authRequestDTO;

    @BeforeEach
    void setup() {
        userRequestDTO = new UserRequestDTO(
                "User Name",
                "user@example.com",
                "password123",
                Role.USER
        );

        authRequestDTO = new AuthenticationRequestDTO(
                "user@example.com",
                "password123"
        );
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        User savedUser = User.builder()
                .name(userRequestDTO.name())
                .email(userRequestDTO.email())
                .role(userRequestDTO.role())
                .build();

        Mockito.when(authenticationService.register(any(User.class)))
                .thenReturn(savedUser);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(userRequestDTO.email()))
                .andExpect(jsonPath("$.name").value(userRequestDTO.name()))
                .andExpect(jsonPath("$.role").value(userRequestDTO.role().name()));
    }

    @Test
    void shouldLoginUserSuccessfully() throws Exception {
        String fakeToken = "fake-jwt-token";

        Mockito.when(authenticationService.login(eq(authRequestDTO.email()), eq(authRequestDTO.password())))
                .thenReturn(fakeToken);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(fakeToken));
    }

    @Test
    void shouldReturnBadRequestWhenRegisterWithInvalidEmail() throws Exception {
        UserRequestDTO invalidUser = new UserRequestDTO(
                "User Name",
                "invalid-email",
                "password123",
                Role.USER
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenLoginWithMissingFields() throws Exception {
        String invalidLoginJson = """
            {
                "email": "user@example.com"
            }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidLoginJson))
                .andExpect(status().isBadRequest());
    }
}

