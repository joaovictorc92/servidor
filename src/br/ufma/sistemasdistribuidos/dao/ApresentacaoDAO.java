package br.ufma.sistemasdistribuidos.dao;


import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import br.ufma.sistemasdistribuidos.form.Apresentacao;

public interface ApresentacaoDAO {

	public abstract ArrayList<Apresentacao> carregarApresentacoes() throws SQLException; // Busca as op��es de apresenta��o

	public abstract ArrayList<ImageIcon> carregarApresentacao(int idApresentacao)// carrega as apresenta��es
			throws SQLException;

}
