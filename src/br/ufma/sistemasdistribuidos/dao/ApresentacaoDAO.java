package br.ufma.sistemasdistribuidos.dao;


import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import br.ufma.sistemasdistribuidos.form.Apresentacao;

public interface ApresentacaoDAO {

	public abstract ArrayList<Apresentacao> carregarApresentacoes() throws SQLException;

	public abstract ArrayList<ImageIcon> carregarApresentacao(int idApresentacao)
			throws SQLException;

}
