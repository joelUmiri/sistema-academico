package br.com.exemplo.util;

import java.sql.DriverManager;

import javax.swing.JOptionPane;

import java.sql.Connection;

public class ConnectionFactory {
	
	public static Connection getConnection() throws Exception{
		//método getconnection - não poderá tratar erros
		
		try {
			//Indica o DB mysql e aponta para o driver
			Class.forName("com.mysql.jdbc.Driver");
			
			//conexão com DB
			String login = "root";
			String senha = "";
			String url = "jdbc:mysql://localhost:3307/db_sistema_academico";
			return DriverManager.getConnection(url,login,senha);	
		} catch(Exception e) {
			throw new Exception(e.getMessage());
		
		}
	}
		public static void main(String[] args) {
			try {
				Connection conn = ConnectionFactory.getConnection();
				JOptionPane.showMessageDialog(null, "DB Conectado");
			} catch(Exception e) {
			e.printStackTrace();
			}
		}
}