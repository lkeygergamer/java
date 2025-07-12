package com.exemplo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "eventos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String tipo;
    
    @Column(columnDefinition = "TEXT")
    private String dados;
    
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
    
    @Column(name = "processado")
    private boolean processado = false;
    
    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }
    
    public enum TipoEvento {
        USUARIO_CRIADO,
        USUARIO_ATUALIZADO,
        USUARIO_DELETADO,
        PRODUTO_CRIADO,
        PRODUTO_ATUALIZADO,
        PRODUTO_DELETADO
    }
} 