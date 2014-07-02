package br.ufma.sistemasdistribuidos.dao;

import br.ufma.sistemasdistribuidos.form.IUsuario;

public interface UsuarioDAO {


	public IUsuario buscarUsuario(String login, String senha) // busca o usuario no banco
			throws Exception;


}
