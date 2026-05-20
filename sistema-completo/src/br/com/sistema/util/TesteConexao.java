/*package br.com.sistema.util;

import java.sql.Connection;

public class TesteConexao {
    public static void main(String[] args) {
        System.out.println("Tentando conectar ao banco de dados...");
        
        try {
            // Tenta obter a conexão usando a classe que você acabou de criar
            Connection conexao = ConnectionFactory.getConnection();
            
            if (conexao != null) {
                System.out.println("\n=========================================");
                System.out.println("   SUCESSO! O Java se conectou ao MySQL!  ");
                System.out.println("=========================================");
                
                // Fecha a conexão para não deixar portas abertas à toa
                ConnectionFactory.closeConnection(conexao);
                System.out.println("Conexão fechada com segurança.");
            }
            
        } catch (Exception e) {
            System.err.println("\n❌ ERRO AO CONECTAR!");
            System.err.println("Motivo do erro: " + e.getMessage());
            System.err.println("\nVerifique:");
            System.err.println("1) Se o seu MySQL está ligado.");
            System.err.println("2) Se a senha na ConnectionFactory está certa.");
            System.err.println("3) Se adicionou o arquivo .jar do Driver nas Bibliotecas.");
        }
    }
}*/