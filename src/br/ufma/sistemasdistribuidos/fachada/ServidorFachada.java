package br.ufma.sistemasdistribuidos.fachada;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import br.ufma.sistemasdistribuidos.form.IUsuario;
import br.ufma.sistemasdistribuidos.form.Apresentacao;

public interface ServidorFachada {

	public abstract IUsuario buscarUsuario(String login, String senha)
			throws Exception;

	public abstract ArrayList<Apresentacao> carregarListaApresentacao() throws SQLException;

	public abstract ArrayList<ImageIcon> carregarApresentacacao(int idApresentacao)
			throws SQLException;

}
