package br.ufma.sistemasdistribuidos.fachada;

import br.ufma.sistemasdistribuidos.form.IUsuario;

public interface ServidorFachada {

	public abstract boolean isLoginValido(String login) throws Exception;

	public abstract IUsuario buscarUsuario(String login, String senha)
			throws Exception;

}
