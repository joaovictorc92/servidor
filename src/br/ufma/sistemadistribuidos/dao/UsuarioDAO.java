package br.ufma.sistemadistribuidos.dao;

import br.ufma.sistemasdistribuidos.form.IUsuario;
import br.ufma.sistemasdistribuidos.form.Usuario;

public interface UsuarioDAO {

	public boolean isLoginValido(String login) throws Exception;

	public Usuario buscarUsuario(String login, String senha)
			throws Exception;

	public abstract void inserirUsuario(IUsuario usuario);

}
