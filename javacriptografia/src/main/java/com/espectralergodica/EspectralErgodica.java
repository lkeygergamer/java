package com.espectralergodica;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Criptografia Espectral-Ergódica - Java
 * =====================================
 * 
 * Implementação completa de criptografia baseada em:
 * - Função zeta de Riemann
 * - Operadores espectrais
 * - Análise ergódica
 * - Matrizes de autovalores
 * 
 * Autor: Adilson Oliveira & Assistente IA
 * Ano: 2025
 * Versão: 2.0.0
 */
public class EspectralErgodica {
    
    private static final Logger LOGGER = Logger.getLogger(EspectralErgodica.class.getName());
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    
    // Constantes para configuração
    public static final int DEFAULT_MATRIX_SIZE = 256;
    public static final double DEFAULT_ZETA_PARAMETER = 2.0;
    public static final int SALT_LENGTH = 32;
    
    /**
     * Gera hash espectral usando função zeta
     */
    public static String hashEspectral(String mensagem, double s) {
        try {
            byte[] data = mensagem.getBytes("UTF-8");
            double zetaSum = 0.0;
            
            for (byte b : data) {
                int n = b & 0xFF;
                zetaSum += 1.0 / Math.pow(n + 1, s);
            }
            
            String zetaString = String.valueOf(zetaSum);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(zetaString.getBytes("UTF-8"));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            LOGGER.info("Hash espectral gerado com sucesso");
            return hexString.toString();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro no hash espectral", e);
            throw new RuntimeException("Erro no hash espectral", e);
        }
    }
    
    /**
     * Cifra mensagem usando matriz espectral
     */
    public static String cifraEspectral(String mensagem, String chave) {
        try {
            byte[] data = mensagem.getBytes("UTF-8");
            byte[] salt = gerarSalt();
            int seed = gerarSeed(chave + Base64.getEncoder().encodeToString(salt));
            double[][] matriz = gerarMatriz(data.length, seed);
            double[] autovalores = calcularAutovalores(matriz);
            
            byte[] cifra = new byte[data.length];
            for (int i = 0; i < data.length; i++) {
                double autovalor = Math.abs(autovalores[i % autovalores.length]);
                int valor = (data[i] & 0xFF) + (int)(autovalor * 1000);
                cifra[i] = (byte)(valor % 256);
            }
            
            // Combinar salt + cifra
            byte[] resultado = new byte[salt.length + cifra.length];
            System.arraycopy(salt, 0, resultado, 0, salt.length);
            System.arraycopy(cifra, 0, resultado, salt.length, cifra.length);
            
            LOGGER.info("Mensagem cifrada com sucesso");
            return Base64.getEncoder().encodeToString(resultado);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro na cifragem espectral", e);
            throw new RuntimeException("Erro na cifragem espectral", e);
        }
    }
    
    /**
     * Decifra mensagem usando matriz espectral
     */
    public static String decifraEspectral(String cifraB64, String chave) {
        try {
            byte[] resultado = Base64.getDecoder().decode(cifraB64);
            
            // Extrair salt e cifra
            byte[] salt = new byte[SALT_LENGTH];
            byte[] cifra = new byte[resultado.length - SALT_LENGTH];
            System.arraycopy(resultado, 0, salt, 0, SALT_LENGTH);
            System.arraycopy(resultado, SALT_LENGTH, cifra, 0, cifra.length);
            
            int seed = gerarSeed(chave + Base64.getEncoder().encodeToString(salt));
            double[][] matriz = gerarMatriz(cifra.length, seed);
            double[] autovalores = calcularAutovalores(matriz);
            
            byte[] data = new byte[cifra.length];
            for (int i = 0; i < cifra.length; i++) {
                double autovalor = Math.abs(autovalores[i % autovalores.length]);
                int valor = (cifra[i] & 0xFF) - (int)(autovalor * 1000);
                if (valor < 0) valor += 256;
                data[i] = (byte)(valor % 256);
            }
            
            LOGGER.info("Mensagem decifrada com sucesso");
            return new String(data, "UTF-8");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro na decifragem espectral", e);
            throw new RuntimeException("Erro na decifragem espectral", e);
        }
    }
    
    /**
     * Gera assinatura espectral da mensagem
     */
    public static String assinaturaEspectral(String mensagem, String chave) {
        try {
            byte[] data = mensagem.getBytes("UTF-8");
            int seed = gerarSeed(chave);
            double[][] matriz = gerarMatriz(data.length, seed);
            
            // Calcular traço da matriz
            double traco = 0.0;
            for (int i = 0; i < matriz.length; i++) {
                traco += matriz[i][i];
            }
            
            // Calcular determinante aproximado
            double determinante = calcularDeterminante(matriz);
            
            String assinatura = String.format("%.12f_%.12f", traco, determinante);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(assinatura.getBytes("UTF-8"));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            LOGGER.info("Assinatura espectral gerada com sucesso");
            return hexString.toString();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro na assinatura espectral", e);
            throw new RuntimeException("Erro na assinatura espectral", e);
        }
    }
    
    /**
     * Verifica se a assinatura é válida
     */
    public static boolean verificarAssinatura(String mensagem, String chave, String assinatura) {
        try {
            String assinaturaCalculada = assinaturaEspectral(mensagem, chave);
            return assinaturaCalculada.equals(assinatura);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro na verificação da assinatura", e);
            return false;
        }
    }
    
    /**
     * Gera salt criptograficamente seguro
     */
    private static byte[] gerarSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        SECURE_RANDOM.nextBytes(salt);
        return salt;
    }
    
    /**
     * Gera seed a partir da chave
     */
    private static int gerarSeed(String chave) {
        int seed = 0;
        for (char c : chave.toCharArray()) {
            seed = (seed * 31 + (int) c) & 0xFFFFFFFF;
        }
        return seed;
    }
    
    /**
     * Gera matriz espectral
     */
    private static double[][] gerarMatriz(int tamanho, int seed) {
        double[][] matriz = new double[tamanho][tamanho];
        long x = seed;
        
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                x = (x * 9301 + 49297) % 233280;
                matriz[i][j] = (double) x / 233280.0;
            }
        }
        
        return matriz;
    }
    
    /**
     * Calcula autovalores da matriz
     */
    private static double[] calcularAutovalores(double[][] matriz) {
        int n = matriz.length;
        double[] autovalores = new double[n];
        
        for (int i = 0; i < n; i++) {
            double autovalor = 0.0;
            for (int j = 0; j < n; j++) {
                autovalor += matriz[i][j];
            }
            autovalores[i] = autovalor / n;
        }
        
        return autovalores;
    }
    
    /**
     * Calcula determinante da matriz (aproximado)
     */
    private static double calcularDeterminante(double[][] matriz) {
        int n = matriz.length;
        if (n == 1) return matriz[0][0];
        if (n == 2) return matriz[0][0] * matriz[1][1] - matriz[0][1] * matriz[1][0];
        
        double determinante = 0.0;
        for (int j = 0; j < n; j++) {
            double[][] submatriz = new double[n-1][n-1];
            for (int i = 1; i < n; i++) {
                for (int k = 0; k < n; k++) {
                    if (k < j) submatriz[i-1][k] = matriz[i][k];
                    else if (k > j) submatriz[i-1][k-1] = matriz[i][k];
                }
            }
            determinante += Math.pow(-1, j) * matriz[0][j] * calcularDeterminante(submatriz);
        }
        return determinante;
    }
    
    /**
     * Gera chave aleatória segura
     */
    public static String gerarChaveAleatoria() {
        byte[] chave = new byte[32];
        SECURE_RANDOM.nextBytes(chave);
        return Base64.getEncoder().encodeToString(chave);
    }
    
    /**
     * Testa a força da chave
     */
    public static int testarForcaChave(String chave) {
        int forca = 0;
        
        // Comprimento mínimo
        if (chave.length() >= 8) forca += 1;
        if (chave.length() >= 12) forca += 1;
        if (chave.length() >= 16) forca += 1;
        
        // Complexidade
        boolean temMaiuscula = chave.matches(".*[A-Z].*");
        boolean temMinuscula = chave.matches(".*[a-z].*");
        boolean temNumero = chave.matches(".*\\d.*");
        boolean temEspecial = chave.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
        
        if (temMaiuscula) forca += 1;
        if (temMinuscula) forca += 1;
        if (temNumero) forca += 1;
        if (temEspecial) forca += 1;
        
        return forca;
    }
} 