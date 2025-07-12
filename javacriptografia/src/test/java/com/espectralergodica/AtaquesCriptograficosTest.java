package com.espectralergodica;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Testes específicos para ataques criptográficos
 * 
 * Simula diferentes tipos de ataques para verificar a resistência
 * da criptografia espectral-ergódica.
 */
@DisplayName("Testes de Resistência a Ataques Criptográficos")
public class AtaquesCriptograficosTest {
    
    private static final String MENSAGEM_ALVO = "Mensagem secreta que deve ser protegida";
    private static final String CHAVE_CORRETA = "ChaveSecreta123!@#";
    
    @Test
    @DisplayName("Teste de Ataque de Força Bruta Simples")
    public void testAtaqueForcaBrutaSimples() {
        String cifra = EspectralErgodica.cifraEspectral(MENSAGEM_ALVO, CHAVE_CORRETA);
        
        // Lista de chaves comuns para teste
        String[] chavesComuns = {
            "123", "1234", "12345", "123456", "1234567", "12345678",
            "abc", "abcd", "abcde", "abcdef", "abcdefg", "abcdefgh",
            "teste", "teste123", "admin", "admin123", "root", "root123",
            "password", "password123", "senha", "senha123", "user", "user123",
            "qwerty", "qwerty123", "letmein", "welcome", "login", "login123"
        };
        
        boolean ataqueSucesso = false;
        String chaveEncontrada = null;
        
        for (String chave : chavesComuns) {
            try {
                String decifrada = EspectralErgodica.decifraEspectral(cifra, chave);
                if (decifrada.equals(MENSAGEM_ALVO)) {
                    ataqueSucesso = true;
                    chaveEncontrada = chave;
                    break;
                }
            } catch (Exception e) {
                // Continua tentando
            }
        }
        
        assertFalse(ataqueSucesso, 
            "Sistema não deve ser vulnerável a ataques de força bruta simples. " +
            "Chave encontrada: " + chaveEncontrada);
    }
    
    @Test
    @DisplayName("Teste de Ataque de Dicionário")
    public void testAtaqueDicionario() {
        String cifra = EspectralErgodica.cifraEspectral(MENSAGEM_ALVO, CHAVE_CORRETA);
        
        // Simulação de ataque de dicionário com palavras comuns
        String[] dicionario = {
            "a", "about", "all", "also", "and", "as", "at", "be", "because", "but", "by",
            "can", "come", "could", "day", "do", "even", "find", "first", "for", "from",
            "get", "give", "go", "have", "he", "her", "here", "him", "his", "how", "I",
            "if", "in", "into", "it", "its", "just", "know", "like", "look", "make",
            "man", "many", "me", "more", "my", "new", "no", "not", "now", "of", "on",
            "one", "only", "or", "other", "our", "out", "over", "people", "say", "see",
            "she", "so", "some", "take", "tell", "than", "that", "the", "their", "them",
            "then", "there", "they", "think", "this", "time", "to", "two", "up", "use",
            "very", "want", "way", "we", "well", "what", "when", "which", "who", "will",
            "with", "would", "year", "you", "your"
        };
        
        boolean ataqueSucesso = false;
        String chaveEncontrada = null;
        
        for (String palavra : dicionario) {
            try {
                String decifrada = EspectralErgodica.decifraEspectral(cifra, palavra);
                if (decifrada.equals(MENSAGEM_ALVO)) {
                    ataqueSucesso = true;
                    chaveEncontrada = palavra;
                    break;
                }
            } catch (Exception e) {
                // Continua tentando
            }
        }
        
        assertFalse(ataqueSucesso, 
            "Sistema não deve ser vulnerável a ataques de dicionário. " +
            "Chave encontrada: " + chaveEncontrada);
    }
    
    @Test
    @DisplayName("Teste de Ataque de Análise de Frequência")
    public void testAtaqueAnaliseFrequencia() {
        // Teste com texto conhecido para análise de frequência
        String textoConhecido = "Este é um texto conhecido que pode ser usado para análise de frequência. " +
                               "Ele contém muitas letras comuns como a, e, i, o, u e consoantes como r, s, t, n, l.";
        
        String cifra = EspectralErgodica.cifraEspectral(textoConhecido, CHAVE_CORRETA);
        
        // Verificar se a cifra não revela padrões óbvios
        byte[] cifraBytes = java.util.Base64.getDecoder().decode(cifra);
        
        // Verificar distribuição de bytes
        int[] frequencias = new int[256];
        for (byte b : cifraBytes) {
            frequencias[b & 0xFF]++;
        }
        
        // Calcular entropia da cifra
        double entropia = calcularEntropia(frequencias, cifraBytes.length);
        
        // Entropia alta indica boa distribuição (próxima de 8.0 para bytes)
        assertTrue(entropia > 7.0, 
            "Cifra deve ter alta entropia para resistir a análise de frequência. Entropia: " + entropia);
    }
    
    @Test
    @DisplayName("Teste de Ataque de Colisão de Hash")
    public void testAtaqueColisaoHash() {
        Set<String> hashes = new HashSet<>();
        int tentativas = 10000;
        int colisoes = 0;
        
        for (int i = 0; i < tentativas; i++) {
            String mensagem = "Mensagem " + i + " para teste de colisão";
            String hash = EspectralErgodica.hashEspectral(mensagem, 2.0);
            
            if (!hashes.add(hash)) {
                colisoes++;
            }
        }
        
        double taxaColisao = (double) colisoes / tentativas;
        assertTrue(taxaColisao < 0.001, 
            "Taxa de colisão deve ser muito baixa. Taxa: " + taxaColisao);
    }
    
    @Test
    @DisplayName("Teste de Ataque de Birthday Paradox")
    public void testAtaqueBirthdayParadox() {
        Set<String> hashes = new HashSet<>();
        int tentativas = 0;
        int maxTentativas = 100000;
        
        while (tentativas < maxTentativas) {
            String mensagem = "Mensagem aleatória " + System.nanoTime();
            String hash = EspectralErgodica.hashEspectral(mensagem, 2.0);
            
            if (!hashes.add(hash)) {
                break; // Colisão encontrada
            }
            tentativas++;
        }
        
        // Deve ser difícil encontrar colisões
        assertTrue(tentativas >= 1000, 
            "Deve ser difícil encontrar colisões. Tentativas necessárias: " + tentativas);
    }
    
    @Test
    @DisplayName("Teste de Ataque de Padding Oracle")
    public void testAtaquePaddingOracle() {
        // Simular diferentes tamanhos de mensagem para verificar se há vazamento de informação
        String[] mensagens = {
            "a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa", "aaaaaaaa"
        };
        
        Set<String> cifras = new HashSet<>();
        for (String mensagem : mensagens) {
            String cifra = EspectralErgodica.cifraEspectral(mensagem, CHAVE_CORRETA);
            cifras.add(cifra);
        }
        
        // Todas as cifras devem ser únicas
        assertEquals(mensagens.length, cifras.size(), 
            "Cifras de diferentes tamanhos devem ser únicas para evitar ataques de padding oracle");
    }
    
    @Test
    @DisplayName("Teste de Ataque de Timing")
    public void testAtaqueTiming() {
        String mensagem = "Mensagem para teste de timing";
        String chaveCorreta = CHAVE_CORRETA;
        String chaveIncorreta = "ChaveIncorreta123!@#";
        
        // Medir tempo com chave correta
        long inicio = System.nanoTime();
        try {
            String cifra = EspectralErgodica.cifraEspectral(mensagem, chaveCorreta);
            EspectralErgodica.decifraEspectral(cifra, chaveCorreta);
        } catch (Exception e) {
            // Ignora
        }
        long tempoCorreto = System.nanoTime() - inicio;
        
        // Medir tempo com chave incorreta
        inicio = System.nanoTime();
        try {
            String cifra = EspectralErgodica.cifraEspectral(mensagem, chaveCorreta);
            EspectralErgodica.decifraEspectral(cifra, chaveIncorreta);
        } catch (Exception e) {
            // Ignora
        }
        long tempoIncorreto = System.nanoTime() - inicio;
        
        // Diferença de tempo deve ser pequena
        double diferencaPercentual = Math.abs(tempoCorreto - tempoIncorreto) / 
                                    Math.max(tempoCorreto, tempoIncorreto) * 100;
        
        assertTrue(diferencaPercentual < 50, 
            "Diferença de timing deve ser pequena para evitar ataques de timing. " +
            "Diferença: " + diferencaPercentual + "%");
    }
    
    @Test
    @DisplayName("Teste de Ataque de Replay")
    public void testAtaqueReplay() {
        String mensagem = "Mensagem para teste de replay";
        String cifra1 = EspectralErgodica.cifraEspectral(mensagem, CHAVE_CORRETA);
        String cifra2 = EspectralErgodica.cifraEspectral(mensagem, CHAVE_CORRETA);
        
        // Cifras devem ser diferentes mesmo para a mesma mensagem
        assertNotEquals(cifra1, cifra2, 
            "Cifras devem ser únicas para prevenir ataques de replay");
    }
    
    @Test
    @DisplayName("Teste de Ataque de Rainbow Table")
    public void testAtaqueRainbowTable() {
        // Simular tentativa de usar rainbow table
        String[] mensagensComuns = {
            "password", "123456", "qwerty", "admin", "root", "user", "test",
            "hello", "world", "welcome", "login", "secret", "private", "confidential"
        };
        
        Set<String> hashes = new HashSet<>();
        for (String mensagem : mensagensComuns) {
            String hash = EspectralErgodica.hashEspectral(mensagem, 2.0);
            hashes.add(hash);
        }
        
        // Todos os hashes devem ser únicos
        assertEquals(mensagensComuns.length, hashes.size(), 
            "Hashes de mensagens comuns devem ser únicos para resistir a rainbow tables");
    }
    
    @Test
    @DisplayName("Teste de Ataque de Man-in-the-Middle Simulado")
    public void testAtaqueManInTheMiddle() {
        String mensagem = "Mensagem secreta";
        String assinatura = EspectralErgodica.assinaturaEspectral(mensagem, CHAVE_CORRETA);
        
        // Simular modificação da mensagem
        String mensagemModificada = mensagem + " (MODIFICADA)";
        
        // Verificar se a assinatura é rejeitada
        boolean assinaturaValida = EspectralErgodica.verificarAssinatura(mensagemModificada, CHAVE_CORRETA, assinatura);
        
        assertFalse(assinaturaValida, 
            "Assinatura deve ser rejeitada para mensagem modificada (ataque MITM)");
    }
    
    @Test
    @DisplayName("Teste de Resistência a Ataques de Exaustão")
    public void testResistenciaAtaquesExaustao() {
        String mensagem = "Mensagem para teste de exaustão";
        String cifra = EspectralErgodica.cifraEspectral(mensagem, CHAVE_CORRETA);
        
        // Simular múltiplas tentativas de decifragem
        int tentativas = 1000;
        int sucessos = 0;
        
        for (int i = 0; i < tentativas; i++) {
            String chaveTeste = "ChaveTeste" + i + "!@#";
            try {
                String decifrada = EspectralErgodica.decifraEspectral(cifra, chaveTeste);
                if (decifrada.equals(mensagem)) {
                    sucessos++;
                }
            } catch (Exception e) {
                // Continua tentando
            }
        }
        
        assertEquals(0, sucessos, 
            "Nenhuma decifragem deve ser bem-sucedida com chaves incorretas");
    }
    
    @Test
    @DisplayName("Teste de Resistência a Ataques de Chave Fraca")
    public void testResistenciaChaveFraca() {
        String mensagem = "Mensagem para teste de chave fraca";
        String chaveFraca = "123";
        
        // Mesmo com chave fraca, o sistema deve funcionar
        String cifra = EspectralErgodica.cifraEspectral(mensagem, chaveFraca);
        String decifrada = EspectralErgodica.decifraEspectral(cifra, chaveFraca);
        
        assertEquals(mensagem, decifrada, 
            "Sistema deve funcionar mesmo com chaves fracas");
        
        // Mas deve ser resistente a ataques
        String[] chavesTeste = {"1", "12", "1234", "abc", "test"};
        boolean vulneravel = false;
        
        for (String chaveTeste : chavesTeste) {
            try {
                String decifradaTeste = EspectralErgodica.decifraEspectral(cifra, chaveTeste);
                if (decifradaTeste.equals(mensagem)) {
                    vulneravel = true;
                    break;
                }
            } catch (Exception e) {
                // Continua tentando
            }
        }
        
        assertFalse(vulneravel, 
            "Sistema deve ser resistente mesmo com chaves fracas");
    }
    
    /**
     * Calcula a entropia de Shannon para verificar a distribuição de bytes
     */
    private double calcularEntropia(int[] frequencias, int total) {
        double entropia = 0.0;
        
        for (int freq : frequencias) {
            if (freq > 0) {
                double probabilidade = (double) freq / total;
                entropia -= probabilidade * Math.log(probabilidade) / Math.log(2);
            }
        }
        
        return entropia;
    }
} 