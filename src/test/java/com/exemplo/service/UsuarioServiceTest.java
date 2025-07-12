package com.exemplo.service;

import com.exemplo.model.Usuario;
import com.exemplo.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@exemplo.com");
        usuario.setSenha("123456");
        usuario.setRole(Usuario.Role.USER);
    }

    @Test
    void criarUsuario_DeveCriarUsuarioComSucesso() {
        // Given
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(usuario.getSenha())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        var resultado = usuarioService.criarUsuario(usuario);

        // Then
        assertNotNull(resultado);
        assertEquals(usuario.getNome(), resultado.getNome());
        assertEquals(usuario.getEmail(), resultado.getEmail());
        verify(passwordEncoder).encode(usuario.getSenha());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void criarUsuario_DeveLancarExcecao_QuandoEmailJaExiste() {
        // Given
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> usuarioService.criarUsuario(usuario));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void loadUserByUsername_DeveRetornarUsuario_QuandoEmailExiste() {
        // Given
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        // When
        var resultado = usuarioService.loadUserByUsername(usuario.getEmail());

        // Then
        assertNotNull(resultado);
        assertEquals(usuario.getEmail(), resultado.getUsername());
    }

    @Test
    void loadUserByUsername_DeveLancarExcecao_QuandoEmailNaoExiste() {
        // Given
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class,
                () -> usuarioService.loadUserByUsername(usuario.getEmail()));
    }
} 