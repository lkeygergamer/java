package com.exemplo.repository;

import com.exemplo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM Usuario u WHERE u.ativo = true")
    List<Usuario> findAllAtivos();
    
    @Query("SELECT u FROM Usuario u WHERE u.role = :role")
    List<Usuario> findByRole(@Param("role") Usuario.Role role);
    
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.ativo = true")
    long countAtivos();
} 