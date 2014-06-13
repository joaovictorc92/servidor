package br.ufma.sistemasdistribuidos.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Conexao {

	private static String jdbcDriver;
	private static String jdbcUrl;
	private static String user;
	private static String password;
	
	private static Connection conexao = null;
	
	public static Connection get(){
		
		if(conexao != null){
			return conexao;
		}
		if(jdbcDriver == null || jdbcUrl == null || user == null || password == null){
			setDatabaseConfig();
		}
		try{
			
			Class.forName(jdbcDriver);
			conexao = DriverManager.getConnection(jdbcUrl, user, password);
			
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			System.exit(1);
		}catch(Exception ex){
			ex.printStackTrace();
			System.exit(1);
		}
		
		return conexao;
	}
	
	public static void setDatabaseConfig(){
		
		jdbcUrl = "jdbc:postgresql://localhost/apresentacao";
		jdbcDriver = "org.postgresql.Driver";
		user = "postgres";
		password = "postgres";
		
	}
	
	
	

}