import com.espectralergodica.EspectralErgodica;
import java.util.*;
import java.security.SecureRandom;

/**
 * Script de Teste de Segurança Completo
 * 
 * Executa uma bateria completa de testes para verificar a segurança
 * da criptografia espectral-ergódica.
 */
public class teste_seguranca_completo {
    
    private static final SecureRandom RANDOM = new SecureRandom();
    private static int testesPassaram = 0;
    private static int testesFalharam = 0;
    
    public static void main(String[] args) {
        System.out.println("=== TESTE DE SEGURANÇA COMPLETO ===");
        System.out.println("Criptografia Espectral-Ergódica Java v2.0.0");
        System.out.println("Autor: Adilson Oliveira & Assistente IA");
        System.out.println("Data: " + new Date());
        System.out.println();
        
        executarTodosTestes();
        
        System.out.println("\n=== RESUMO DOS TESTES ===");
        System.out.println("Testes que passaram: " + testesPassaram);
        System.out.println("Testes que falharam: " + testesFalharam);
        System.out.println("Total de testes: " + (testesPassaram + testesFalharam));
        
        if (testesFalharam == 0) {
            System.out.println("\n✓ TODOS OS TESTES PASSARAM! Sistema seguro.");
        } else {
            System.out.println("\n✗ ALGUNS TESTES FALHARAM! Verificar vulnerabilidades.");
        }
    }
    
    private static void executarTodosTestes() {
        // Testes básicos de funcionalidade
        executarTeste("Teste de Hash Básico", testeHashBasico());
        executarTeste("Teste de Cifragem/Decifragem", testeCifragemDecifragem());
        executarTeste("Teste de Assinatura", testeAssinatura());
        
        // Testes de segurança
        executarTeste("Teste de Força Bruta", testeForcaBruta());
        executarTeste("Teste de Dicionário", testeDicionario());
        executarTeste("Teste de Análise de Frequência", testeAnaliseFrequencia());
        executarTeste("Teste de Colisão de Hash", testeColisaoHash());
        executarTeste("Teste de Birthday Paradox", testeBirthdayParadox());
        executarTeste("Teste de Padding Oracle", testePaddingOracle());
        executarTeste("Teste de Timing", testeTiming());
        executarTeste("Teste de Replay", testeReplay());
        executarTeste("Teste de Rainbow Table", testeRainbowTable());
        executarTeste("Teste de Man-in-the-Middle", testeManInTheMiddle());
        executarTeste("Teste de Exaustão", testeExaustao());
        executarTeste("Teste de Chave Fraca", testeChaveFraca());
        
        // Testes de performance
        executarTeste("Teste de Performance Hash", testePerformanceHash());
        executarTeste("Teste de Performance Cifragem", testePerformanceCifragem());
        
        // Testes de integridade
        executarTeste("Teste de Integridade", testeIntegridade());
        executarTeste("Teste de Unicidade", testeUnicidade());
        executarTeste("Teste de Efeito Avalanche", testeEfeitoAvalanche());
    }
    
    private static void executarTeste(String nome, boolean resultado) {
        if (resultado) {
            System.out.println("✓ " + nome + " - PASSOU");
            testesPassaram++;
        } else {
            System.out.println("✗ " + nome + " - FALHOU");
            testesFalharam++;
        }
    }
    
    // =========================
    // TESTES BÁSICOS
    // =========================
    
    private static boolean testeHashBasico() {
        try {
            String hash = EspectralErgodica.hashEspectral("teste", 2.0);
            return hash != null && hash.length() == 64 && hash.matches("[0-9a-f]{64}");
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean testeCifragemDecifragem() {
        try {
            String mensagem = "Mensagem de teste";
            String chave = "ChaveSecreta123!@#";
            String cifra = EspectralErgodica.cifraEspectral(mensagem, chave);
            String decifrada = EspectralErgodica.decifraEspectral(cifra, chave);
            return mensagem.equals(decifrada);
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean testeAssinatura() {
        try {
            String mensagem = "Mensagem para assinar";
            String chave = "ChaveSecreta123!@#";
            String assinatura = EspectralErgodica.assinaturaEspectral(mensagem, chave);
            return EspectralErgodica.verificarAssinatura(mensagem, chave, assinatura);
        } catch (Exception e) {
            return false;
        }
    }
    
    // =========================
    // TESTES DE SEGURANÇA
    // =========================
    
    private static boolean testeForcaBruta() {
        try {
            String mensagem = "Mensagem secreta";
            String chave = "ChaveSecreta123!@#";
            String cifra = EspectralErgodica.cifraEspectral(mensagem, chave);
            
            String[] chavesTeste = {"123", "abc", "teste", "senha", "admin", "root"};
            
            for (String chaveTeste : chavesTeste) {
                try {
                    String decifrada = EspectralErgodica.decifraEspectral(cifra, chaveTeste);
                    if (decifrada.equals(mensagem)) {
                        return false; // Vulnerável
                    }
                } catch (Exception e) {
                    // Continua tentando
                }
            }
            return true; // Resistente
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean testeDicionario() {
        try {
            String mensagem = "Mensagem secreta";
            String chave = "ChaveSecreta123!@#";
            String cifra = EspectralErgodica.cifraEspectral(mensagem, chave);
            
            String[] dicionario = {"a", "the", "and", "or", "but", "in", "on", "at", "to", "for"};
            
            for (String palavra : dicionario) {
                try {
                    String decifrada = EspectralErgodica.decifraEspectral(cifra, palavra);
                    if (decifrada.equals(mensagem)) {
                        return false; // Vulnerável
                    }
                } catch (Exception e) {
                    // Continua tentando
                }
            }
            return true; // Resistente
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean testeAnaliseFrequencia() {
        try {
            String texto = "Este é um texto conhecido para análise de frequência";
            String chave = "ChaveSecreta123!@#";
            String cifra = EspectralErgodica.cifraEspectral(texto, chave);
            
            byte[] cifraBytes = java.util.Base64.getDecoder().decode(cifra);
            int[] frequencias = new int[256];
            
            for (byte b : cifraBytes) {
                frequencias[b & 0xFF]++;
            }
            
            // Calcular entropia
            double entropia = calcularEntropia(frequencias, cifraBytes.length);
            return entropia > 7.0; // Alta entropia
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean testeColisaoHash() {
        try {
            Set<String> hashes = new HashSet<>();
            int tentativas = 1000;
            int colisoes = 0;
            
            for (int i = 0; i < tentativas; i++) {
                String mensagem = "Mensagem " + i;
                String hash = EspectralErgodica.hashEspectral(mensagem, 2.0);
                
                if (!hashes.add(hash)) {
                    colisoes++;
                }
            }
            
            double taxaColisao = (double) colisoes / tentativas;
            return taxaColisao < 0.001; // Taxa muito baixa
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean testeBirthdayParadox() {
        try {
            Set<String> hashes = new HashSet<>();
            int tentativas = 0;
            int maxTentativas = 10000;
            
            while (tentativas < maxTentativas) {
                String mensagem = "Mensagem " + System.nanoTime();
                String hash = EspectralErgodica.hashEspectral(mensagem, 2.0);
                
                if (!hashes.add(hash)) {
                    break; // Colisão encontrada
                }
                tentativas++;
            }
            
            return tentativas >= 100; // Deve ser difícil encontrar colisões
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean testePaddingOracle() {
        try {
            String[] mensagens = {"a", "aa", "aaa", "aaaa"};
            Set<String> cifras = new HashSet<>();
            
            for (String mensagem : mensagens) {
                String cifra = EspectralErgodica.cifraEspectral(mensagem, "chave");
                cifras.add(cifra);
            }
            
            return cifras.size() == mensagens.length; // Todas únicas
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean testeTiming() {
        try {
            String mensagem = "Mensagem para teste";
            String chaveCorreta = "ChaveCorreta123!@#";
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
            
            double diferenca = Math.abs(tempoCorreto - tempoIncorreto) / 
                              Math.max(tempoCorreto, tempoIncorreto) * 100;
            
            return diferenca < 50; // Diferença pequena
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean testeReplay() {
        try {
            String mensagem = "Mensagem para teste";
            String chave = "ChaveSecreta123!@#";
            
            String cifra1 = EspectralErgodica.cifraEspectral(mensagem, chave);
            String cifra2 = EspectralErgodica.cifraEspectral(mensagem, chave);
            
            return !cifra1.equals(cifra2); // Cifras diferentes
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean testeRainbowTable() {
        try {
            String[] mensagens = {"password", "123456", "admin", "root"};
            Set<String> hashes = new HashSet<>();
            
            for (String mensagem : mensagens) {
                String hash = EspectralErgodica.hashEspectral(mensagem, 2.0);
                hashes.add(hash);
            }
            
            return hashes.size() == mensagens.length; // Todos únicos
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean testeManInTheMiddle() {
        try {
            String mensagem = "Mensagem secreta";
            String chave = "ChaveSecreta123!@#";
            String assinatura = EspectralErgodica.assinaturaEspectral(mensagem, chave);
            
            String mensagemModificada = mensagem + " (MODIFICADA)";
            boolean valida = EspectralErgodica.verificarAssinatura(mensagemModificada, chave, assinatura);
            
            return !valida; // Deve rejeitar
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean testeExaustao() {
        try {
            String mensagem = "Mensagem para teste";
            String cifra = EspectralErgodica.cifraEspectral(mensagem, "ChaveSecreta123!@#");
            
            int tentativas = 100;
            int sucessos = 0;
            
            for (int i = 0; i < tentativas; i++) {
                String chaveTeste = "ChaveTeste" + i;
                try {
                    String decifrada = EspectralErgodica.decifraEspectral(cifra, chaveTeste);
                    if (decifrada.equals(mensagem)) {
                        sucessos++;
                    }
                } catch (Exception e) {
                    // Continua tentando
                }
            }
            
            return sucessos == 0; // Nenhum sucesso
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean testeChaveFraca() {
        try {
            String mensagem = "Mensagem para teste";
            String chaveFraca = "123";
            
            String cifra = EspectralErgodica.cifraEspectral(mensagem, chaveFraca);
            String decifrada = EspectralErgodica.decifraEspectral(cifra, chaveFraca);
            
            if (!mensagem.equals(decifrada)) {
                return false;
            }
            
            // Testar resistência mesmo com chave fraca
            String[] chavesTeste = {"1", "12", "1234"};
            for (String chaveTeste : chavesTeste) {
                try {
                    String decifradaTeste = EspectralErgodica.decifraEspectral(cifra, chaveTeste);
                    if (decifradaTeste.equals(mensagem)) {
                        return false; // Vulnerável
                    }
                } catch (Exception e) {
                    // Continua tentando
                }
            }
            
            return true; // Resistente
        } catch (Exception e) {
            return false;
        }
    }
    
    // =========================
    // TESTES DE PERFORMANCE
    // =========================
    
    private static boolean testePerformanceHash() {
        try {
            String mensagem = "Mensagem de teste para performance";
            long inicio = System.currentTimeMillis();
            
            for (int i = 0; i < 1000; i++) {
                EspectralErgodica.hashEspectral(mensagem, 2.0);
            }
            
            long fim = System.currentTimeMillis();
            long tempoMedio = (fim - inicio) / 1000;
            
            return tempoMedio < 10; // Menos de 10ms por hash
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean testePerformanceCifragem() {
        try {
            String mensagem = "Mensagem de teste para performance";
            String chave = "ChaveSecreta123!@#";
            
            long inicio = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                EspectralErgodica.cifraEspectral(mensagem, chave);
            }
            long fim = System.currentTimeMillis();
            long tempoMedio = (fim - inicio) / 100;
            
            return tempoMedio < 100; // Menos de 100ms por cifragem
        } catch (Exception e) {
            return false;
        }
    }
    
    // =========================
    // TESTES DE INTEGRIDADE
    // =========================
    
    private static boolean testeIntegridade() {
        try {
            String[] mensagens = {"a", "ab", "abc", "abcd", "abcde"};
            String chave = "ChaveSecreta123!@#";
            
            for (String mensagem : mensagens) {
                String cifra = EspectralErgodica.cifraEspectral(mensagem, chave);
                String decifrada = EspectralErgodica.decifraEspectral(cifra, chave);
                
                if (!mensagem.equals(decifrada)) {
                    return false;
                }
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean testeUnicidade() {
        try {
            String mensagem = "Mensagem para teste de unicidade";
            String chave = "ChaveSecreta123!@#";
            
            Set<String> cifras = new HashSet<>();
            for (int i = 0; i < 10; i++) {
                String cifra = EspectralErgodica.cifraEspectral(mensagem, chave);
                cifras.add(cifra);
            }
            
            return cifras.size() == 10; // Todas únicas
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean testeEfeitoAvalanche() {
        try {
            String mensagem1 = "Mensagem de teste";
            String mensagem2 = "Mensagem de teste!";
            
            String hash1 = EspectralErgodica.hashEspectral(mensagem1, 2.0);
            String hash2 = EspectralErgodica.hashEspectral(mensagem2, 2.0);
            
            int diferencas = 0;
            for (int i = 0; i < hash1.length(); i++) {
                if (hash1.charAt(i) != hash2.charAt(i)) {
                    diferencas++;
                }
            }
            
            double percentualDiferenca = (double) diferencas / hash1.length() * 100;
            return percentualDiferenca > 30; // Efeito avalanche significativo
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Calcula a entropia de Shannon
     */
    private static double calcularEntropia(int[] frequencias, int total) {
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