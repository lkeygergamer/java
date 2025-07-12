package com.exemplo.service;

import com.exemplo.model.Produto;
import com.exemplo.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProdutoService {
    
    private final ProdutoRepository produtoRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    
    @Cacheable(value = "produtos", key = "#id")
    public Optional<Produto> buscarPorId(Long id) {
        log.info("Buscando produto com ID: {}", id);
        return produtoRepository.findById(id);
    }
    
    @Cacheable(value = "produtos", key = "'todos'")
    public List<Produto> listarTodos() {
        log.info("Listando todos os produtos");
        return produtoRepository.findAll();
    }
    
    @Cacheable(value = "produtos", key = "'ativos'")
    public List<Produto> listarAtivos() {
        log.info("Listando produtos ativos");
        return produtoRepository.findByAtivoTrue();
    }
    
    @Cacheable(value = "produtos", key = "#categoria")
    public List<Produto> buscarPorCategoria(Produto.Categoria categoria) {
        log.info("Buscando produtos da categoria: {}", categoria);
        return produtoRepository.findByCategoria(categoria);
    }
    
    public Page<Produto> buscarPorPreco(BigDecimal precoMin, BigDecimal precoMax, Pageable pageable) {
        log.info("Buscando produtos entre {} e {}", precoMin, precoMax);
        return produtoRepository.findByPrecoBetween(precoMin, precoMax, pageable);
    }
    
    public List<Produto> buscarPorTermo(String termo) {
        log.info("Buscando produtos com termo: {}", termo);
        return produtoRepository.buscarPorTermo(termo);
    }
    
    public List<Produto> buscarEmEstoque() {
        log.info("Buscando produtos em estoque");
        return produtoRepository.findProdutosEmEstoque();
    }
    
    @CacheEvict(value = "produtos", allEntries = true)
    public Produto criarProduto(Produto produto) {
        log.info("Criando novo produto: {}", produto.getNome());
        Produto produtoSalvo = produtoRepository.save(produto);
        
        // Enviar evento para Kafka
        kafkaTemplate.send("produtos", "PRODUTO_CRIADO", produtoSalvo.getId().toString());
        
        return produtoSalvo;
    }
    
    @CacheEvict(value = "produtos", allEntries = true)
    public Produto atualizarProduto(Long id, Produto produto) {
        log.info("Atualizando produto com ID: {}", id);
        
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        produtoExistente.setNome(produto.getNome());
        produtoExistente.setDescricao(produto.getDescricao());
        produtoExistente.setPreco(produto.getPreco());
        produtoExistente.setQuantidadeEstoque(produto.getQuantidadeEstoque());
        produtoExistente.setCategoria(produto.getCategoria());
        produtoExistente.setTags(produto.getTags());
        produtoExistente.setAtivo(produto.isAtivo());
        
        Produto produtoAtualizado = produtoRepository.save(produtoExistente);
        
        // Enviar evento para Kafka
        kafkaTemplate.send("produtos", "PRODUTO_ATUALIZADO", produtoAtualizado.getId().toString());
        
        return produtoAtualizado;
    }
    
    @CacheEvict(value = "produtos", allEntries = true)
    public void deletarProduto(Long id) {
        log.info("Deletando produto com ID: {}", id);
        
        if (!produtoRepository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado");
        }
        
        produtoRepository.deleteById(id);
        
        // Enviar evento para Kafka
        kafkaTemplate.send("produtos", "PRODUTO_DELETADO", id.toString());
    }
    
    public List<Produto> buscarPorTag(String tag) {
        log.info("Buscando produtos com tag: {}", tag);
        return produtoRepository.findByTag(tag);
    }
    
    // Elasticsearch queries
    public List<Produto> buscarElasticsearch(String termo) {
        log.info("Buscando no Elasticsearch: {}", termo);
        return produtoRepository.findByNomeContainingOrDescricaoContaining(termo, termo);
    }
    
    public List<Produto> buscarPorPrecoMinimo(BigDecimal precoMin) {
        log.info("Buscando produtos com preço mínimo: {}", precoMin);
        return produtoRepository.findByPrecoGreaterThan(precoMin);
    }
} 