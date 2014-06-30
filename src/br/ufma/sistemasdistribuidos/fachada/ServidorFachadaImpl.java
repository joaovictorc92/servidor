package br.ufma.sistemasdistribuidos.fachada;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import br.ufma.sistemasdistribuidos.dao.ApresentacaoDAO;
import br.ufma.sistemasdistribuidos.dao.ApresentacaoDAOImpl;
import br.ufma.sistemasdistribuidos.dao.UsuarioDAO;
import br.ufma.sistemasdistribuidos.dao.UsuarioDAOImpl;
import br.ufma.sistemasdistribuidos.form.Apresentacao;
import br.ufma.sistemasdistribuidos.form.IUsuario;

public class ServidorFachadaImpl implements ServidorFachada{
       
	 UsuarioDAO usuarioDAO;
	 ApresentacaoDAO apresentacaoDAOImpl;
	 public ServidorFachadaImpl(){
		 usuarioDAO = new UsuarioDAOImpl();
		 apresentacaoDAOImpl = new ApresentacaoDAOImpl();
		 
	 }
	 @Override
	public IUsuario buscarUsuario(String login,String senha) throws Exception{
		 
			return usuarioDAO.buscarUsuario(login, senha);
		
	 }
	 
	 @Override
	public ArrayList<Apresentacao> carregarListaApresentacao() throws SQLException{
		 
		 return apresentacaoDAOImpl.carregarApresentacoes();
	 }
	  
	@Override
	public ArrayList<ImageIcon> carregarApresentacacao(int idApresentacao) throws SQLException{
		
		return apresentacaoDAOImpl.carregarApresentacao(idApresentacao);
	}
}
