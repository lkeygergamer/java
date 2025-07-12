package com.exemplo.controller;

import com.exemplo.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class UsuarioControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Test
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void criarUsuario_DeveRetornar201_QuandoDadosValidos() throws Exception {
        // Given
        Usuario usuario = new Usuario();
        usuario.setNome("Teste Usuario");
        usuario.setEmail("teste@exemplo.com");
        usuario.setSenha("123456");

        // When & Then
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Teste Usuario"))
                .andExpect(jsonPath("$.email").value("teste@exemplo.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listarTodos_DeveRetornar200_QuandoUsuarioAdmin() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void listarTodos_DeveRetornar403_QuandoUsuarioNaoAdmin() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isForbidden());
    }

    @Test
    void criarUsuario_DeveRetornar400_QuandoDadosInvalidos() throws Exception {
        // Given
        Usuario usuario = new Usuario();
        usuario.setNome(""); // Nome vazio
        usuario.setEmail("email-invalido");
        usuario.setSenha("123"); // Senha muito curta

        // When & Then
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isBadRequest());
    }
} 