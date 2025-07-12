package com.myfeest.blueprint.controller;

import com.myfeest.blueprint.core.Blueprint;
import com.myfeest.blueprint.entity.BlueprintEntity;
import com.myfeest.blueprint.service.BlueprintService;
import com.myfeest.blueprint.dto.BlueprintCreateRequest;
import com.myfeest.blueprint.dto.BlueprintUpdateRequest;
import com.myfeest.blueprint.dto.BlueprintResponse;
import com.myfeest.blueprint.dto.StandardApiResponse;
import com.myfeest.blueprint.exception.BlueprintNotFoundException;
import com.myfeest.blueprint.exception.BlueprintValidationException;
import com.myfeest.blueprint.exception.DuplicateBlueprintException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para operações com blueprints
 */
@RestController
@RequestMapping("/api/v1/blueprints")
@Tag(name = "Blueprint", description = "API para gerenciamento de blueprints")
@CrossOrigin(origins = "*")
public class BlueprintController {
    
    private static final Logger logger = LoggerFactory.getLogger(BlueprintController.class);
    
    private final BlueprintService blueprintService;
    
    @Autowired
    public BlueprintController(BlueprintService blueprintService) {
        this.blueprintService = blueprintService;
    }
    
    /**
     * Cria um novo blueprint
     */
    @PostMapping
    @Operation(summary = "Criar blueprint", description = "Cria um novo blueprint no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Blueprint criado com sucesso",
                    content = @Content(schema = @Schema(implementation = BlueprintResponse.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Blueprint já existe"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<StandardApiResponse<BlueprintResponse>> createBlueprint(
            @Valid @RequestBody BlueprintCreateRequest request,
            Authentication authentication) {
        
        logger.info("Recebida requisição para criar blueprint: {}", request.getName());
        
        try {
            String createdBy = authentication != null ? authentication.getName() : "anonymous";
            
            BlueprintEntity entity = blueprintService.createBlueprint(request.toBlueprint(), createdBy);
            BlueprintResponse response = BlueprintResponse.fromEntity(entity);
            
            logger.info("Blueprint criado com sucesso: {}", entity.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(StandardApiResponse.success("Blueprint criado com sucesso", response));
                
        } catch (DuplicateBlueprintException e) {
            logger.warn("Tentativa de criar blueprint duplicado: {}", request.getName());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(StandardApiResponse.error("Blueprint já existe", e.getMessage()));
        } catch (BlueprintValidationException e) {
            logger.warn("Blueprint inválido: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(StandardApiResponse.error("Blueprint inválido", e.getMessage()));
        } catch (Exception e) {
            logger.error("Erro ao criar blueprint: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(StandardApiResponse.error("Erro interno", "Erro ao processar requisição"));
        }
    }
    
    /**
     * Busca um blueprint por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar blueprint por ID", description = "Retorna um blueprint específico pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Blueprint encontrado",
                    content = @Content(schema = @Schema(implementation = BlueprintResponse.class))),
        @ApiResponse(responseCode = "404", description = "Blueprint não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<StandardApiResponse<BlueprintResponse>> getBlueprint(
            @Parameter(description = "ID do blueprint") @PathVariable String id) {
        
        logger.debug("Buscando blueprint: {}", id);
        
        try {
            BlueprintEntity entity = blueprintService.getBlueprint(id);
            BlueprintResponse response = BlueprintResponse.fromEntity(entity);
            
            return ResponseEntity.ok(StandardApiResponse.success("Blueprint encontrado", response));
            
        } catch (BlueprintNotFoundException e) {
            logger.warn("Blueprint não encontrado: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(StandardApiResponse.error("Blueprint não encontrado", e.getMessage()));
        } catch (Exception e) {
            logger.error("Erro ao buscar blueprint {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(StandardApiResponse.error("Erro interno", "Erro ao processar requisição"));
        }
    }
    
    /**
     * Lista blueprints paginados
     */
    @GetMapping
    @Operation(summary = "Listar blueprints", description = "Retorna uma lista paginada de blueprints")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de blueprints retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<StandardApiResponse<Page<BlueprintResponse>>> getBlueprints(
            @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        
        logger.debug("Buscando blueprints paginados");
        
        try {
            Page<BlueprintEntity> entities = blueprintService.getBlueprints(pageable);
            Page<BlueprintResponse> responses = entities.map(BlueprintResponse::fromEntity);
            
            return ResponseEntity.ok(StandardApiResponse.success("Blueprints listados com sucesso", responses));
            
        } catch (Exception e) {
            logger.error("Erro ao listar blueprints: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(StandardApiResponse.error("Erro interno", "Erro ao processar requisição"));
        }
    }
    
    /**
     * Busca blueprints por nome
     */
    @GetMapping("/search")
    @Operation(summary = "Buscar blueprints por nome", description = "Busca blueprints que contenham o nome especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<StandardApiResponse<List<BlueprintResponse>>> searchBlueprints(
            @Parameter(description = "Nome para busca") @RequestParam String name) {
        
        logger.debug("Buscando blueprints por nome: {}", name);
        
        try {
            List<BlueprintEntity> entities = blueprintService.searchBlueprintsByName(name);
            List<BlueprintResponse> responses = entities.stream()
                .map(BlueprintResponse::fromEntity)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(StandardApiResponse.success("Busca realizada com sucesso", responses));
            
        } catch (Exception e) {
            logger.error("Erro ao buscar blueprints por nome: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(StandardApiResponse.error("Erro interno", "Erro ao processar requisição"));
        }
    }
    
    /**
     * Atualiza um blueprint
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar blueprint", description = "Atualiza um blueprint existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Blueprint atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Blueprint não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<StandardApiResponse<BlueprintResponse>> updateBlueprint(
            @Parameter(description = "ID do blueprint") @PathVariable String id,
            @Valid @RequestBody BlueprintUpdateRequest request,
            Authentication authentication) {
        
        logger.info("Atualizando blueprint: {}", id);
        
        try {
            String updatedBy = authentication != null ? authentication.getName() : "anonymous";
            
            BlueprintEntity entity = blueprintService.updateBlueprint(id, request.toBlueprint(), updatedBy);
            BlueprintResponse response = BlueprintResponse.fromEntity(entity);
            
            logger.info("Blueprint atualizado com sucesso: {}", id);
            
            return ResponseEntity.ok(StandardApiResponse.success("Blueprint atualizado com sucesso", response));
            
        } catch (BlueprintNotFoundException e) {
            logger.warn("Blueprint não encontrado para atualização: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(StandardApiResponse.error("Blueprint não encontrado", e.getMessage()));
        } catch (BlueprintValidationException e) {
            logger.warn("Blueprint inválido para atualização: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(StandardApiResponse.error("Blueprint inválido", e.getMessage()));
        } catch (Exception e) {
            logger.error("Erro ao atualizar blueprint {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(StandardApiResponse.error("Erro interno", "Erro ao processar requisição"));
        }
    }
    
    /**
     * Executa um blueprint
     */
    @PostMapping("/{id}/execute")
    @Operation(summary = "Executar blueprint", description = "Executa um blueprint e retorna os resultados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Blueprint executado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Blueprint não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> executeBlueprint(
            @Parameter(description = "ID do blueprint") @PathVariable String id) {
        
        logger.info("Executando blueprint: {}", id);
        
        try {
            Map<String, Object> result = blueprintService.executeBlueprint(id);
            
            logger.info("Blueprint executado com sucesso: {}", id);
            
            return ResponseEntity.ok(StandardApiResponse.success("Blueprint executado com sucesso", result));
            
        } catch (BlueprintNotFoundException e) {
            logger.warn("Blueprint não encontrado para execução: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(StandardApiResponse.error("Blueprint não encontrado", e.getMessage()));
        } catch (Exception e) {
            logger.error("Erro ao executar blueprint {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(StandardApiResponse.error("Erro interno", "Erro ao executar blueprint"));
        }
    }
    
    /**
     * Executa um blueprint diretamente (sem salvar)
     */
    @PostMapping("/execute")
    @Operation(summary = "Executar blueprint diretamente", description = "Executa um blueprint sem salvá-lo no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Blueprint executado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Blueprint inválido"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> executeBlueprintDirectly(
            @Valid @RequestBody Blueprint blueprint) {
        
        logger.info("Executando blueprint diretamente: {}", blueprint.getName());
        
        try {
            Map<String, Object> result = blueprintService.executeBlueprintDirectly(blueprint);
            
            logger.info("Blueprint executado com sucesso: {}", blueprint.getName());
            
            return ResponseEntity.ok(StandardApiResponse.success("Blueprint executado com sucesso", result));
            
        } catch (BlueprintValidationException e) {
            logger.warn("Blueprint inválido para execução: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(StandardApiResponse.error("Blueprint inválido", e.getMessage()));
        } catch (Exception e) {
            logger.error("Erro ao executar blueprint diretamente: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(StandardApiResponse.error("Erro interno", "Erro ao executar blueprint"));
        }
    }
    
    /**
     * Deleta um blueprint
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar blueprint", description = "Remove um blueprint do sistema (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Blueprint deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Blueprint não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<StandardApiResponse<Void>> deleteBlueprint(
            @Parameter(description = "ID do blueprint") @PathVariable String id) {
        
        logger.info("Deletando blueprint: {}", id);
        
        try {
            blueprintService.deleteBlueprint(id);
            
            logger.info("Blueprint deletado com sucesso: {}", id);
            
            return ResponseEntity.ok(StandardApiResponse.success("Blueprint deletado com sucesso", null));
            
        } catch (BlueprintNotFoundException e) {
            logger.warn("Blueprint não encontrado para deleção: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(StandardApiResponse.error("Blueprint não encontrado", e.getMessage()));
        } catch (Exception e) {
            logger.error("Erro ao deletar blueprint {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(StandardApiResponse.error("Erro interno", "Erro ao processar requisição"));
        }
    }
    
    /**
     * Busca estatísticas dos blueprints
     */
    @GetMapping("/statistics")
    @Operation(summary = "Estatísticas dos blueprints", description = "Retorna estatísticas gerais dos blueprints")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> getStatistics() {
        
        logger.debug("Buscando estatísticas dos blueprints");
        
        try {
            Map<String, Object> statistics = blueprintService.getStatistics();
            
            return ResponseEntity.ok(StandardApiResponse.success("Estatísticas retornadas com sucesso", statistics));
            
        } catch (Exception e) {
            logger.error("Erro ao buscar estatísticas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(StandardApiResponse.error("Erro interno", "Erro ao processar requisição"));
        }
    }
} 