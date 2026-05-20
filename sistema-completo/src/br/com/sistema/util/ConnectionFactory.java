package br.com.sistema.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    
    // URL de conexão local (ajusta a porta 3306 se o teu MySQL usar outra)
    private static final String URL = "jdbc:mysql://localhost:3306/db_sistema_academico?useTimezone=true&serverTimezone=UTC&useSSL=false";
    private static final String USER = "root"; // altere pro nome do dono do sql utilizado
    private static final String PASSWORD = ""; // Altere a senha pra ser a senha do pc 'unicid'?

    /**
     * Método para obter uma conexão ativa com o MySQL
     */
    public static Connection getConnection() throws Exception {
        try {
            // Regista o driver JDBC do MySQL na memória
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new Exception("Driver do banco de dados (JDBC) não foi encontrado.", e);
        } catch (SQLException e) {
            throw new Exception("Erro ao conectar com o banco de dados: " + e.getMessage(), e);
        }
    }

    /**
     * Método utilitário para fechar a conexão em segurança
     */
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar a conexão: " + e.getMessage());
        }
    }
}