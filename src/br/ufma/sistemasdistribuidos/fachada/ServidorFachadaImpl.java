package br.ufma.sistemasdistribuidos.fachada;

import br.ufma.sistemadistribuidos.dao.UsuarioDAO;
import br.ufma.sistemadistribuidos.dao.UsuarioDAOImpl;
import br.ufma.sistemasdistribuidos.form.IUsuario;

public class ServidorFachadaImpl implements ServidorFachada{
       
	 UsuarioDAO usuarioDAO;
	 public ServidorFachadaImpl(){
		 usuarioDAO = new UsuarioDAOImpl();
	 }
	 @Override
	public IUsuario buscarUsuario(String login,String senha) throws Exception{
		 
			return usuarioDAO.buscarUsuario(login, senha);
		
	 }
	 
	 @Override
	public boolean isLoginValido(String login) throws Exception{
		 
		 return usuarioDAO.isLoginValido(login);
	 }
	 
	 public void inserirUsuario(IUsuario usuario){
		 
		 usuarioDAO.inserirUsuario(usuario);
		 
	 }
	  
}
