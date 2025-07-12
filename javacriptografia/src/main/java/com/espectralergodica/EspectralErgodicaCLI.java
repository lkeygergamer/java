package com.espectralergodica;

import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Interface de Linha de Comando para Criptografia Espectral-Ergódica
 * 
 * Fornece uma interface interativa para testar todas as funcionalidades
 * de criptografia e realizar testes de segurança.
 */
public class EspectralErgodicaCLI {
    
    private static final Logger LOGGER = Logger.getLogger(EspectralErgodicaCLI.class.getName());
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=== Criptografia Espectral-Ergódica Java v2.0.0 ===");
        System.out.println("Autor: Adilson Oliveira & Assistente IA");
        System.out.println("Ano: 2025");
        System.out.println();
        
        while (true) {
            exibirMenu();
            String opcao = scanner.nextLine().trim();
            
            try {
                processarOpcao(opcao);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erro ao processar opção", e);
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
    
    private static void exibirMenu() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1.  Gerar hash espectral");
        System.out.println("2.  Cifrar mensagem");
        System.out.println("3.  Decifrar mensagem");
        System.out.println("4.  Gerar assinatura espectral");
        System.out.println("5.  Verificar assinatura");
        System.out.println("6.  Gerar chave aleatória");
        System.out.println("7.  Testar força da chave");
        System.out.println("8.  Executar testes de segurança");
        System.out.println("9.  Executar testes de performance");
        System.out.println("10. Executar testes de resistência a ataques");
        System.out.println("11. Sair");
        System.out.print("Escolha uma opção: ");
    }
    
    private static void processarOpcao(String opcao) {
        switch (opcao) {
            case "1":
                gerarHashEspectral();
                break;
            case "2":
                cifrarMensagem();
                break;
            case "3":
                decifrarMensagem();
                break;
            case "4":
                gerarAssinatura();
                break;
            case "5":
                verificarAssinatura();
                break;
            case "6":
                gerarChaveAleatoria();
                break;
            case "7":
                testarForcaChave();
                break;
            case "8":
                executarTestesSeguranca();
                break;
            case "9":
                executarTestesPerformance();
                break;
            case "10":
                executarTestesResistencia();
                break;
            case "11":
                System.out.println("Saindo...");
                scanner.close();
                System.exit(0);
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }
    
    private static void gerarHashEspectral() {
        System.out.print("Mensagem para hash: ");
        String mensagem = scanner.nextLine();
        System.out.print("Parâmetro s (ex: 2.0): ");
        String sStr = scanner.nextLine();
        double s = sStr.isEmpty() ? 2.0 : Double.parseDouble(sStr);
        
        String hash = EspectralErgodica.hashEspectral(mensagem, s);
        System.out.println("Hash espectral: " + hash);
    }
    
    private static void cifrarMensagem() {
        System.out.print("Mensagem para cifrar: ");
        String mensagem = scanner.nextLine();
        System.out.print("Chave secreta: ");
        String chave = scanner.nextLine();
        
        String cifra = EspectralErgodica.cifraEspectral(mensagem, chave);
        System.out.println("Mensagem cifrada: " + cifra);
    }
    
    private static void decifrarMensagem() {
        System.out.print("Mensagem cifrada (base64): ");
        String cifra = scanner.nextLine();
        System.out.print("Chave secreta: ");
        String chave = scanner.nextLine();
        
        try {
            String mensagem = EspectralErgodica.decifraEspectral(cifra, chave);
            System.out.println("Mensagem decifrada: " + mensagem);
        } catch (Exception e) {
            System.out.println("Erro na decifragem: " + e.getMessage());
        }
    }
    
    private static void gerarAssinatura() {
        System.out.print("Mensagem para assinar: ");
        String mensagem = scanner.nextLine();
        System.out.print("Chave secreta: ");
        String chave = scanner.nextLine();
        
        String assinatura = EspectralErgodica.assinaturaEspectral(mensagem, chave);
        System.out.println("Assinatura espectral: " + assinatura);
    }
    
    private static void verificarAssinatura() {
        System.out.print("Mensagem: ");
        String mensagem = scanner.nextLine();
        System.out.print("Chave secreta: ");
        String chave = scanner.nextLine();
        System.out.print("Assinatura: ");
        String assinatura = scanner.nextLine();
        
        boolean valida = EspectralErgodica.verificarAssinatura(mensagem, chave, assinatura);
        System.out.println("Assinatura válida: " + valida);
    }
    
    private static void gerarChaveAleatoria() {
        String chave = EspectralErgodica.gerarChaveAleatoria();
        System.out.println("Chave aleatória gerada: " + chave);
        
        int forca = EspectralErgodica.testarForcaChave(chave);
        System.out.println("Força da chave: " + forca + "/7");
    }
    
    private static void testarForcaChave() {
        System.out.print("Chave para testar: ");
        String chave = scanner.nextLine();
        
        int forca = EspectralErgodica.testarForcaChave(chave);
        System.out.println("Força da chave: " + forca + "/7");
        
        if (forca >= 5) {
            System.out.println("✓ Chave forte!");
        } else if (forca >= 3) {
            System.out.println("⚠ Chave moderada");
        } else {
            System.out.println("✗ Chave fraca");
        }
    }
    
    private static void executarTestesSeguranca() {
        System.out.println("\n=== EXECUTANDO TESTES DE SEGURANÇA ===");
        
        // Teste 1: Verificação de integridade
        System.out.println("1. Testando integridade...");
        String mensagem = "Teste de segurança da criptografia espectral";
        String chave = "ChaveSecreta123!@#";
        String cifra = EspectralErgodica.cifraEspectral(mensagem, chave);
        String decifrada = EspectralErgodica.decifraEspectral(cifra, chave);
        
        if (mensagem.equals(decifrada)) {
            System.out.println("✓ Integridade verificada");
        } else {
            System.out.println("✗ Falha na integridade");
        }
        
        // Teste 2: Verificação de assinatura
        System.out.println("2. Testando assinatura...");
        String assinatura = EspectralErgodica.assinaturaEspectral(mensagem, chave);
        boolean assinaturaValida = EspectralErgodica.verificarAssinatura(mensagem, chave, assinatura);
        
        if (assinaturaValida) {
            System.out.println("✓ Assinatura válida");
        } else {
            System.out.println("✗ Falha na assinatura");
        }
        
        // Teste 3: Verificação de hash
        System.out.println("3. Testando hash...");
        String hash1 = EspectralErgodica.hashEspectral(mensagem, 2.0);
        String hash2 = EspectralErgodica.hashEspectral(mensagem, 2.0);
        
        if (hash1.equals(hash2)) {
            System.out.println("✓ Hash determinístico");
        } else {
            System.out.println("✗ Hash não determinístico");
        }
        
        System.out.println("Testes de segurança concluídos!");
    }
    
    private static void executarTestesPerformance() {
        System.out.println("\n=== EXECUTANDO TESTES DE PERFORMANCE ===");
        
        String mensagem = "Mensagem de teste para performance " + "x".repeat(1000);
        String chave = "ChaveSecreta123!@#";
        
        // Teste de cifragem
        long inicio = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            EspectralErgodica.cifraEspectral(mensagem, chave);
        }
        long fim = System.currentTimeMillis();
        System.out.println("Tempo médio de cifragem: " + ((fim - inicio) / 100.0) + "ms");
        
        // Teste de decifragem
        String cifra = EspectralErgodica.cifraEspectral(mensagem, chave);
        inicio = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            EspectralErgodica.decifraEspectral(cifra, chave);
        }
        fim = System.currentTimeMillis();
        System.out.println("Tempo médio de decifragem: " + ((fim - inicio) / 100.0) + "ms");
        
        // Teste de hash
        inicio = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            EspectralErgodica.hashEspectral(mensagem, 2.0);
        }
        fim = System.currentTimeMillis();
        System.out.println("Tempo médio de hash: " + ((fim - inicio) / 1000.0) + "ms");
        
        System.out.println("Testes de performance concluídos!");
    }
    
    private static void executarTestesResistencia() {
        System.out.println("\n=== EXECUTANDO TESTES DE RESISTÊNCIA A ATAQUES ===");
        
        String mensagem = "Mensagem secreta";
        String chave = "ChaveSecreta123!@#";
        String cifra = EspectralErgodica.cifraEspectral(mensagem, chave);
        
        // Teste 1: Ataque de força bruta simples
        System.out.println("1. Testando resistência a força bruta...");
        boolean forcaBrutaSucesso = false;
        String[] chavesTeste = {"123", "abc", "teste", "senha", "admin", "root"};
        
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
        
        if (!forcaBrutaSucesso) {
            System.out.println("✓ Resistente a força bruta simples");
        } else {
            System.out.println("✗ Vulnerável a força bruta");
        }
        
        // Teste 2: Verificação de avalanche
        System.out.println("2. Testando efeito avalanche...");
        String mensagemModificada = "Mensagem secreta!";
        String hash1 = EspectralErgodica.hashEspectral(mensagem, 2.0);
        String hash2 = EspectralErgodica.hashEspectral(mensagemModificada, 2.0);
        
        int diferencas = 0;
        for (int i = 0; i < Math.min(hash1.length(), hash2.length()); i++) {
            if (hash1.charAt(i) != hash2.charAt(i)) {
                diferencas++;
            }
        }
        
        double percentualDiferenca = (double) diferencas / hash1.length() * 100;
        if (percentualDiferenca > 40) {
            System.out.println("✓ Efeito avalanche presente (" + String.format("%.1f", percentualDiferenca) + "%)");
        } else {
            System.out.println("✗ Efeito avalanche fraco (" + String.format("%.1f", percentualDiferenca) + "%)");
        }
        
        // Teste 3: Verificação de unicidade
        System.out.println("3. Testando unicidade das cifras...");
        String cifra1 = EspectralErgodica.cifraEspectral(mensagem, chave);
        String cifra2 = EspectralErgodica.cifraEspectral(mensagem, chave);
        
        if (!cifra1.equals(cifra2)) {
            System.out.println("✓ Cifras únicas (salt funciona)");
        } else {
            System.out.println("✗ Cifras idênticas (problema com salt)");
        }
        
        System.out.println("Testes de resistência concluídos!");
    }
} 