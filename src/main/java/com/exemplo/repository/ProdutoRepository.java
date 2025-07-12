package com.exemplo.repository;

import com.exemplo.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>, ElasticsearchRepository<Produto, Long> {
    
    List<Produto> findByCategoria(Produto.Categoria categoria);
    
    List<Produto> findByAtivoTrue();
    
    Page<Produto> findByPrecoBetween(BigDecimal precoMin, BigDecimal precoMax, Pageable pageable);
    
    @Query("SELECT p FROM Produto p WHERE p.nome LIKE %:termo% OR p.descricao LIKE %:termo%")
    List<Produto> buscarPorTermo(@Param("termo") String termo);
    
    @Query("SELECT p FROM Produto p WHERE p.quantidadeEstoque > 0")
    List<Produto> findProdutosEmEstoque();
    
    @Query("SELECT p FROM Produto p WHERE p.tags LIKE %:tag%")
    List<Produto> findByTag(@Param("tag") String tag);
    
    // Elasticsearch queries
    List<Produto> findByNomeContainingOrDescricaoContaining(String nome, String descricao);
    
    List<Produto> findByPrecoGreaterThan(BigDecimal preco);
    
    List<Produto> findByCategoriaAndAtivoTrue(Produto.Categoria categoria);
} 