package br.ufma.sistemasdistribuidos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import br.ufma.sistemasdistribuidos.conexao.Conexao;
import br.ufma.sistemasdistribuidos.form.Apresentacao;

public class ApresentacaoDAOImpl implements ApresentacaoDAO{
	
	@Override
	public ArrayList<Apresentacao> carregarApresentacoes() throws SQLException{
		
		ArrayList<Apresentacao> listaApresentacoes = new ArrayList<Apresentacao>();
		
		PreparedStatement statement = Conexao.get().prepareStatement("SELECT * FROM apresentacao");
		ResultSet resultSet = statement.executeQuery();
		while(resultSet.next()){
			Apresentacao apresentacao = new Apresentacao();
			apresentacao.setIdapresentacao(resultSet.getInt("idapresentacao"));
			apresentacao.setNome(resultSet.getString("nome"));
			apresentacao.setCaminho(resultSet.getString("caminho"));
		    listaApresentacoes.add(apresentacao);
		}
		
		return listaApresentacoes;
	}
	
	@Override
	public ArrayList<ImageIcon> carregarApresentacao(int idApresentacao) throws SQLException{
		ArrayList<ImageIcon> listaImagens = new ArrayList<ImageIcon>();
        PreparedStatement statement = Conexao.get().prepareStatement("SELECT * FROM imagem where idapresentacao="+idApresentacao);
	    ResultSet resultSet = statement.executeQuery();
		while(resultSet.next()){
			System.out.println(resultSet.getString("caminho"));
			ImageIcon image = new ImageIcon(getClass().getResource(resultSet.getString("caminho")));
			listaImagens.add(image);
		}
		return listaImagens;
	}
	
	

}
