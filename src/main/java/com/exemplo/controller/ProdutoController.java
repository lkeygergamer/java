package com.exemplo.controller;

import com.exemplo.model.Produto;
import com.exemplo.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "API para gerenciamento de produtos")
public class ProdutoController {
    
    private final ProdutoService produtoService;
    
    @GetMapping
    @Operation(summary = "Listar todos os produtos")
    public ResponseEntity<List<Produto>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }
    
    @GetMapping("/ativos")
    @Operation(summary = "Listar produtos ativos")
    public ResponseEntity<List<Produto>> listarAtivos() {
        return ResponseEntity.ok(produtoService.listarAtivos());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar produtos por categoria")
    public ResponseEntity<List<Produto>> buscarPorCategoria(@PathVariable Produto.Categoria categoria) {
        return ResponseEntity.ok(produtoService.buscarPorCategoria(categoria));
    }
    
    @GetMapping("/busca")
    @Operation(summary = "Buscar produtos por termo")
    public ResponseEntity<List<Produto>> buscarPorTermo(@RequestParam String termo) {
        return ResponseEntity.ok(produtoService.buscarPorTermo(termo));
    }
    
    @GetMapping("/elasticsearch")
    @Operation(summary = "Buscar produtos no Elasticsearch")
    public ResponseEntity<List<Produto>> buscarElasticsearch(@RequestParam String termo) {
        return ResponseEntity.ok(produtoService.buscarElasticsearch(termo));
    }
    
    @GetMapping("/preco")
    @Operation(summary = "Buscar produtos por faixa de preço")
    public ResponseEntity<Page<Produto>> buscarPorPreco(
            @RequestParam BigDecimal precoMin,
            @RequestParam BigDecimal precoMax,
            Pageable pageable) {
        return ResponseEntity.ok(produtoService.buscarPorPreco(precoMin, precoMax, pageable));
    }
    
    @GetMapping("/estoque")
    @Operation(summary = "Listar produtos em estoque")
    public ResponseEntity<List<Produto>> buscarEmEstoque() {
        return ResponseEntity.ok(produtoService.buscarEmEstoque());
    }
    
    @GetMapping("/tag/{tag}")
    @Operation(summary = "Buscar produtos por tag")
    public ResponseEntity<List<Produto>> buscarPorTag(@PathVariable String tag) {
        return ResponseEntity.ok(produtoService.buscarPorTag(tag));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar novo produto")
    public ResponseEntity<Produto> criarProduto(@Valid @RequestBody Produto produto) {
        return ResponseEntity.ok(produtoService.criarProduto(produto));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar produto")
    public ResponseEntity<Produto> atualizarProduto(
            @PathVariable Long id,
            @Valid @RequestBody Produto produto) {
        return ResponseEntity.ok(produtoService.atualizarProduto(id, produto));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar produto")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
    
    @CacheEvict(value = "produtos", allEntries = true)
    @PostMapping("/cache/limpar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Limpar cache de produtos")
    public ResponseEntity<Void> limparCache() {
        return ResponseEntity.ok().build();
    }
} 