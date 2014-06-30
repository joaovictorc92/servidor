package br.ufma.sistemasdistribuidos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import br.ufma.sistemasdistribuidos.conexao.Conexao;
import br.ufma.sistemasdistribuidos.form.IUsuario;


public class UsuarioDAOImpl implements UsuarioDAO {

@Override
public IUsuario buscarUsuario(String login, String senha) throws Exception {
		
		IUsuario usuario = new br.ufma.sistemasdistribuidos.form.Usuario();
		
		PreparedStatement statement = Conexao.get().prepareStatement("SELECT * FROM " +
				"usuario WHERE login = ? AND senha = ?;");
		statement.setString(1, login);
		statement.setString(2, senha);
		
		ResultSet resultSet = statement.executeQuery();
		
		if (resultSet.next()) {
			System.out.print("Usuario encontrado");
			usuario.setLogin(resultSet.getString("login"));
			usuario.setNome(resultSet.getString("nome"));
			usuario.setSenha(resultSet.getString("senha"));
			usuario.setIdusuario(resultSet.getInt("idusuario"));
			
			return usuario;
		}		
	
		return null;
		
		
	}	
	
	


}