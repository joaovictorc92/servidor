package br.ufma.sistemasdistribuidos.fachada;
import br.ufma.sistemasdistribuidos.form.Usuario;

public interface ServidorFachada {

	public abstract boolean isLoginValido(String login) throws Exception;

	public abstract Usuario buscarUsuario(String login, String senha)
			throws Exception;

}
