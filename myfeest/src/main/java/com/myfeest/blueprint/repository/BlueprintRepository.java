package com.myfeest.blueprint.repository;

import com.myfeest.blueprint.entity.BlueprintEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA para operações com blueprints
 */
@Repository
public interface BlueprintRepository extends JpaRepository<BlueprintEntity, String> {
    
    /**
     * Busca blueprints por nome (case insensitive)
     */
    List<BlueprintEntity> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);
    
    /**
     * Busca blueprints paginados e ativos
     */
    Page<BlueprintEntity> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Busca blueprints por usuário criador
     */
    List<BlueprintEntity> findByCreatedByAndIsActiveTrue(String createdBy);
    
    /**
     * Busca blueprints criados após uma data específica
     */
    List<BlueprintEntity> findByCreatedAtAfterAndIsActiveTrue(LocalDateTime date);
    
    /**
     * Busca blueprints atualizados após uma data específica
     */
    List<BlueprintEntity> findByUpdatedAtAfterAndIsActiveTrue(LocalDateTime date);
    
    /**
     * Busca blueprint por nome exato (case insensitive)
     */
    Optional<BlueprintEntity> findByNameIgnoreCaseAndIsActiveTrue(String name);
    
    /**
     * Conta blueprints ativos
     */
    long countByIsActiveTrue();
    
    /**
     * Conta blueprints por usuário criador
     */
    long countByCreatedByAndIsActiveTrue(String createdBy);
    
    /**
     * Busca blueprints com metadata específica
     */
    @Query("SELECT b FROM BlueprintEntity b WHERE b.isActive = true AND :key MEMBER OF b.metadata.keySet()")
    List<BlueprintEntity> findByMetadataKey(@Param("key") String key);
    
    /**
     * Busca blueprints com valor específico em metadata
     */
    @Query("SELECT b FROM BlueprintEntity b WHERE b.isActive = true AND b.metadata[:key] = :value")
    List<BlueprintEntity> findByMetadataValue(@Param("key") String key, @Param("value") String value);
    
    /**
     * Busca blueprints por versão
     */
    List<BlueprintEntity> findByVersionAndIsActiveTrue(Integer version);
    
    /**
     * Busca a versão mais recente de um blueprint por nome
     */
    @Query("SELECT MAX(b.version) FROM BlueprintEntity b WHERE b.name = :name AND b.isActive = true")
    Optional<Integer> findLatestVersionByName(@Param("name") String name);
    
    /**
     * Busca blueprints com maior versão
     */
    @Query("SELECT b FROM BlueprintEntity b WHERE b.isActive = true AND b.version = (SELECT MAX(b2.version) FROM BlueprintEntity b2 WHERE b2.name = b.name AND b2.isActive = true)")
    List<BlueprintEntity> findLatestVersions();
    
    /**
     * Busca estatísticas de blueprints
     */
    @Query("SELECT COUNT(b) as total, " +
           "COUNT(CASE WHEN b.createdAt >= :startDate THEN 1 END) as recent, " +
           "COUNT(DISTINCT b.createdBy) as uniqueUsers " +
           "FROM BlueprintEntity b WHERE b.isActive = true")
    Object[] getStatistics(@Param("startDate") LocalDateTime startDate);
} 