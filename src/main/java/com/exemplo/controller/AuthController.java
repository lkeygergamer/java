package com.exemplo.controller;

import com.exemplo.dto.TokenDTO;
import com.exemplo.dto.UsuarioDTO;
import com.exemplo.model.Usuario;
import com.exemplo.service.JwtService;
import com.exemplo.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "API para autenticação e autorização")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;
    
    @PostMapping("/login")
    @Operation(summary = "Fazer login")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );
        
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = jwtService.generateToken(usuario);
        
        TokenDTO tokenDTO = new TokenDTO(token, "Bearer", System.currentTimeMillis() + 86400000);
        return ResponseEntity.ok(tokenDTO);
    }
    
    @PostMapping("/registro")
    @Operation(summary = "Registrar novo usuário")
    public ResponseEntity<UsuarioDTO> registro(@Valid @RequestBody Usuario usuario) {
        UsuarioDTO usuarioCriado = usuarioService.criarUsuario(usuario);
        return ResponseEntity.ok(usuarioCriado);
    }
    
    @GetMapping("/perfil")
    @Operation(summary = "Obter perfil do usuário logado")
    public ResponseEntity<UsuarioDTO> obterPerfil(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(UsuarioDTO.fromEntity(usuario));
    }
    
    // Classe interna para request de login
    public static class LoginRequest {
        private String email;
        private String senha;
        
        // Getters e Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
    }
} 