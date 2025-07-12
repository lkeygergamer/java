package com.espectralergodica;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Testes unitários abrangentes para Criptografia Espectral-Ergódica
 * 
 * Testa todas as funcionalidades e verifica a segurança do sistema.
 */
@DisplayName("Testes da Criptografia Espectral-Ergódica")
public class EspectralErgodicaTest {
    
    private static final String MENSAGEM_TESTE = "Mensagem de teste para criptografia espectral";
    private static final String CHAVE_TESTE = "ChaveSecreta123!@#";
    private static final double PARAMETRO_ZETA = 2.0;
    
    @Test
    @DisplayName("Teste de hash espectral básico")
    public void testHashEspectralBasico() {
        String hash = EspectralErgodica.hashEspectral(MENSAGEM_TESTE, PARAMETRO_ZETA);
        
        assertNotNull(hash);
        assertEquals(64, hash.length()); // SHA-256 tem 64 caracteres hex
        assertTrue(hash.matches("[0-9a-f]{64}"));
    }
    
    @Test
    @DisplayName("Teste de determinismo do hash")
    public void testHashDeterministico() {
        String hash1 = EspectralErgodica.hashEspectral(MENSAGEM_TESTE, PARAMETRO_ZETA);
        String hash2 = EspectralErgodica.hashEspectral(MENSAGEM_TESTE, PARAMETRO_ZETA);
        
        assertEquals(hash1, hash2, "Hash deve ser determinístico");
    }
    
    @Test
    @DisplayName("Teste de efeito avalanche no hash")
    public void testEfeitoAvalancheHash() {
        String mensagem1 = "Mensagem de teste";
        String mensagem2 = "Mensagem de teste!";
        
        String hash1 = EspectralErgodica.hashEspectral(mensagem1, PARAMETRO_ZETA);
        String hash2 = EspectralErgodica.hashEspectral(mensagem2, PARAMETRO_ZETA);
        
        int diferencas = 0;
        for (int i = 0; i < hash1.length(); i++) {
            if (hash1.charAt(i) != hash2.charAt(i)) {
                diferencas++;
            }
        }
        
        double percentualDiferenca = (double) diferencas / hash1.length() * 100;
        assertTrue(percentualDiferenca > 30, 
            "Efeito avalanche deve ser significativo. Diferença: " + percentualDiferenca + "%");
    }
    
    @Test
    @DisplayName("Teste de cifragem e decifragem básica")
    public void testCifragemDecifragemBasica() {
        String cifra = EspectralErgodica.cifraEspectral(MENSAGEM_TESTE, CHAVE_TESTE);
        String decifrada = EspectralErgodica.decifraEspectral(cifra, CHAVE_TESTE);
        
        assertNotNull(cifra);
        assertNotNull(decifrada);
        assertEquals(MENSAGEM_TESTE, decifrada, "Mensagem decifrada deve ser igual à original");
    }
    
    @Test
    @DisplayName("Teste de unicidade das cifras")
    public void testUnicidadeCifras() {
        String cifra1 = EspectralErgodica.cifraEspectral(MENSAGEM_TESTE, CHAVE_TESTE);
        String cifra2 = EspectralErgodica.cifraEspectral(MENSAGEM_TESTE, CHAVE_TESTE);
        
        assertNotEquals(cifra1, cifra2, "Cifras devem ser únicas devido ao salt");
    }
    
    @Test
    @DisplayName("Teste de decifragem com chave incorreta")
    public void testDecifragemChaveIncorreta() {
        String cifra = EspectralErgodica.cifraEspectral(MENSAGEM_TESTE, CHAVE_TESTE);
        
        assertThrows(RuntimeException.class, () -> {
            EspectralErgodica.decifraEspectral(cifra, "ChaveIncorreta");
        }, "Deve falhar com chave incorreta");
    }
    
    @Test
    @DisplayName("Teste de assinatura espectral")
    public void testAssinaturaEspectral() {
        String assinatura = EspectralErgodica.assinaturaEspectral(MENSAGEM_TESTE, CHAVE_TESTE);
        
        assertNotNull(assinatura);
        assertEquals(64, assinatura.length());
        assertTrue(assinatura.matches("[0-9a-f]{64}"));
    }
    
    @Test
    @DisplayName("Teste de verificação de assinatura válida")
    public void testVerificacaoAssinaturaValida() {
        String assinatura = EspectralErgodica.assinaturaEspectral(MENSAGEM_TESTE, CHAVE_TESTE);
        boolean valida = EspectralErgodica.verificarAssinatura(MENSAGEM_TESTE, CHAVE_TESTE, assinatura);
        
        assertTrue(valida, "Assinatura válida deve ser verificada corretamente");
    }
    
    @Test
    @DisplayName("Teste de verificação de assinatura inválida")
    public void testVerificacaoAssinaturaInvalida() {
        String assinatura = EspectralErgodica.assinaturaEspectral(MENSAGEM_TESTE, CHAVE_TESTE);
        boolean valida = EspectralErgodica.verificarAssinatura(MENSAGEM_TESTE + "!", CHAVE_TESTE, assinatura);
        
        assertFalse(valida, "Assinatura inválida deve ser rejeitada");
    }
    
    @Test
    @DisplayName("Teste de geração de chave aleatória")
    public void testGeracaoChaveAleatoria() {
        String chave1 = EspectralErgodica.gerarChaveAleatoria();
        String chave2 = EspectralErgodica.gerarChaveAleatoria();
        
        assertNotNull(chave1);
        assertNotNull(chave2);
        assertNotEquals(chave1, chave2, "Chaves aleatórias devem ser diferentes");
        assertTrue(chave1.length() > 0);
        assertTrue(chave2.length() > 0);
    }
    
    @Test
    @DisplayName("Teste de força da chave")
    public void testForcaChave() {
        // Teste chave fraca
        int forcaFraca = EspectralErgodica.testarForcaChave("123");
        assertTrue(forcaFraca < 3, "Chave fraca deve ter pontuação baixa");
        
        // Teste chave forte
        int forcaForte = EspectralErgodica.testarForcaChave("ChaveForte123!@#");
        assertTrue(forcaForte >= 5, "Chave forte deve ter pontuação alta");
    }
    
    @Test
    @DisplayName("Teste de resistência a ataques de força bruta simples")
    public void testResistenciaForcaBruta() {
        String mensagem = "Mensagem secreta";
        String chave = "ChaveSecreta123!@#";
        String cifra = EspectralErgodica.cifraEspectral(mensagem, chave);
        
        String[] chavesTeste = {"123", "abc", "teste", "senha", "admin", "root", "password"};
        boolean forcaBrutaSucesso = false;
        
        for (String chaveTeste : chavesTeste) {
            try {
                String decifrada = EspectralErgodica.decifraEspectral(cifra, chaveTeste);
                if (decifrada.equals(mensagem)) {
                    forcaBrutaSucesso = true;
                    break;
                }
            } catch (Exception e) {
                // Continua tentando
            }
        }
        
        assertFalse(forcaBrutaSucesso, "Sistema deve ser resistente a ataques de força bruta simples");
    }
    
    @Test
    @DisplayName("Teste de performance de cifragem")
    public void testPerformanceCifragem() {
        String mensagem = "Mensagem de teste para performance " + "x".repeat(100);
        long inicio = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            EspectralErgodica.cifraEspectral(mensagem, CHAVE_TESTE);
        }
        
        long fim = System.currentTimeMillis();
        long tempoMedio = (fim - inicio) / 100;
        
        assertTrue(tempoMedio < 100, "Tempo médio de cifragem deve ser menor que 100ms. Tempo: " + tempoMedio + "ms");
    }
    
    @Test
    @DisplayName("Teste de performance de hash")
    public void testPerformanceHash() {
        String mensagem = "Mensagem de teste para performance " + "x".repeat(100);
        long inicio = System.currentTimeMillis();
        
        for (int i = 0; i < 1000; i++) {
            EspectralErgodica.hashEspectral(mensagem, PARAMETRO_ZETA);
        }
        
        long fim = System.currentTimeMillis();
        long tempoMedio = (fim - inicio) / 1000;
        
        assertTrue(tempoMedio < 10, "Tempo médio de hash deve ser menor que 10ms. Tempo: " + tempoMedio + "ms");
    }
    
    @Test
    @DisplayName("Teste de diferentes parâmetros zeta")
    public void testDiferentesParametrosZeta() {
        String hash1 = EspectralErgodica.hashEspectral(MENSAGEM_TESTE, 1.5);
        String hash2 = EspectralErgodica.hashEspectral(MENSAGEM_TESTE, 2.0);
        String hash3 = EspectralErgodica.hashEspectral(MENSAGEM_TESTE, 2.5);
        
        assertNotEquals(hash1, hash2, "Hashes com parâmetros diferentes devem ser diferentes");
        assertNotEquals(hash2, hash3, "Hashes com parâmetros diferentes devem ser diferentes");
        assertNotEquals(hash1, hash3, "Hashes com parâmetros diferentes devem ser diferentes");
    }
    
    @Test
    @DisplayName("Teste de mensagens vazias")
    public void testMensagensVazias() {
        // Teste hash com mensagem vazia
        String hash = EspectralErgodica.hashEspectral("", PARAMETRO_ZETA);
        assertNotNull(hash);
        assertEquals(64, hash.length());
        
        // Teste cifragem com mensagem vazia
        String cifra = EspectralErgodica.cifraEspectral("", CHAVE_TESTE);
        String decifrada = EspectralErgodica.decifraEspectral(cifra, CHAVE_TESTE);
        assertEquals("", decifrada);
        
        // Teste assinatura com mensagem vazia
        String assinatura = EspectralErgodica.assinaturaEspectral("", CHAVE_TESTE);
        assertNotNull(assinatura);
        assertEquals(64, assinatura.length());
    }
    
    @Test
    @DisplayName("Teste de mensagens longas")
    public void testMensagensLongas() {
        String mensagemLonga = "Mensagem muito longa " + "x".repeat(10000);
        
        // Teste hash
        String hash = EspectralErgodica.hashEspectral(mensagemLonga, PARAMETRO_ZETA);
        assertNotNull(hash);
        assertEquals(64, hash.length());
        
        // Teste cifragem
        String cifra = EspectralErgodica.cifraEspectral(mensagemLonga, CHAVE_TESTE);
        String decifrada = EspectralErgodica.decifraEspectral(cifra, CHAVE_TESTE);
        assertEquals(mensagemLonga, decifrada);
        
        // Teste assinatura
        String assinatura = EspectralErgodica.assinaturaEspectral(mensagemLonga, CHAVE_TESTE);
        assertNotNull(assinatura);
        assertEquals(64, assinatura.length());
    }
    
    @Test
    @DisplayName("Teste de caracteres especiais")
    public void testCaracteresEspeciais() {
        String mensagemEspecial = "Mensagem com caracteres especiais: áéíóú çãõ ñ ü ß € £ ¥ © ® ™";
        
        String cifra = EspectralErgodica.cifraEspectral(mensagemEspecial, CHAVE_TESTE);
        String decifrada = EspectralErgodica.decifraEspectral(cifra, CHAVE_TESTE);
        
        assertEquals(mensagemEspecial, decifrada, "Caracteres especiais devem ser preservados");
    }
    
    @Test
    @DisplayName("Teste de distribuição de hash")
    public void testDistribuicaoHash() {
        Set<String> hashes = new HashSet<>();
        
        for (int i = 0; i < 1000; i++) {
            String mensagem = "Mensagem " + i;
            String hash = EspectralErgodica.hashEspectral(mensagem, PARAMETRO_ZETA);
            hashes.add(hash);
        }
        
        assertEquals(1000, hashes.size(), "Todos os hashes devem ser únicos");
    }
    
    @Test
    @DisplayName("Teste de integridade com diferentes tamanhos")
    public void testIntegridadeDiferentesTamanhos() {
        String[] mensagens = {
            "a",
            "ab",
            "abc",
            "abcd",
            "abcde",
            "abcdef",
            "abcdefg",
            "abcdefgh",
            "abcdefghi",
            "abcdefghij"
        };
        
        for (String mensagem : mensagens) {
            String cifra = EspectralErgodica.cifraEspectral(mensagem, CHAVE_TESTE);
            String decifrada = EspectralErgodica.decifraEspectral(cifra, CHAVE_TESTE);
            assertEquals(mensagem, decifrada, "Integridade deve ser mantida para mensagem: " + mensagem);
        }
    }
} 