package br.ufma.sistemadistribuidos.dao;

import br.ufma.sistemasdistribuidos.form.IUsuario;

public interface UsuarioDAO {

	public boolean isLoginValido(String login) throws Exception;

	public IUsuario buscarUsuario(String login, String senha)
			throws Exception;

	public abstract void inserirUsuario(IUsuario usuario);

}
