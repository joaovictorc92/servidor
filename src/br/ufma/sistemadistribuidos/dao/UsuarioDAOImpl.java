package br.ufma.sistemadistribuidos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import br.ufma.sistemasdistribuidos.conexao.Conexao;
import br.ufma.sistemasdistribuidos.form.IUsuario;
import br.ufma.sistemasdistribuidos.form.Usuario;


public class UsuarioDAOImpl implements UsuarioDAO {

@Override
public Usuario buscarUsuario(String login, String senha) throws Exception {
		
		Usuario usuario = new br.ufma.sistemasdistribuidos.form.Usuario();
		
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
	
	
	
@Override
public boolean isLoginValido(String login) throws Exception {
		
		PreparedStatement statement = Conexao.get().prepareStatement("SELECT * FROM " +
				"usuario WHERE login = ? ");
		statement.setString(1, login);
		
		ResultSet resultSet = statement.executeQuery();
		
		if (resultSet.next()) return false;
		else return true;
				
    }

@Override
public void inserirUsuario(IUsuario usuario){
	try {
		PreparedStatement statement = Conexao.get().prepareStatement("INSERT INTO usuario(nome,login,senha) values (?,?,?)");
		statement.setString(1, usuario.getNome());
		statement.setString(2, usuario.getLogin());
		statement.setString(3, usuario.getSenha());
		statement.executeUpdate();
		statement.close();
		System.out.println("Usuario inserido");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

}