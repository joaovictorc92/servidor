package br.ufma.sistemasdistribuidos.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import br.ufma.sistemasdistribuidos.fachada.ServidorFachada;
import br.ufma.sistemasdistribuidos.fachada.ServidorFachadaImpl;
import br.ufma.sistemasdistribuidos.form.Apresentacao;
import br.ufma.sistemasdistribuidos.form.IUsuario;
import br.ufma.sistemasdistribuidos.form.Mensagem;
import br.ufma.sistemasdistribuidos.form.Usuario;

public class Servidor {
	ServerSocket servidor;
	Socket cliente;
	ArrayList<Usuario> usuariosLogados;
	ArrayList<ObjectOutputStream> listaClientes;
	IUsuario usuario;
	ServidorFachada fachada;
	int idConexao;

	public Servidor() {
		this.idConexao = 0;
        usuariosLogados = new ArrayList<Usuario>();
        listaClientes = new ArrayList<ObjectOutputStream>();
		try {
            
			servidor = new ServerSocket(12345);
			System.out.println("\nPorta 12345 aberta!");
			//Aguarda a requi��o de um cliente
			while (true) {
				cliente = servidor.accept();
				ObjectInputStream input = new ObjectInputStream(cliente.getInputStream());
				ObjectOutputStream output = new ObjectOutputStream(cliente.getOutputStream());
				System.out.println("Nova conex�o com o cliente "
						+ cliente.getInetAddress().getHostAddress());
				this.idConexao++;
				//Para cada cliente � criada uma thread
				new Thread(new EscutaCliente(input,output,idConexao)).start();

			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    
	public void enviarParaClientes(ArrayList<ObjectOutputStream> listaClientes, Object object){
		for(ObjectOutputStream output : listaClientes){
			Serializacao.serializa(output, object);
		}
		
	}
	
	private class EscutaCliente implements Runnable {
		ObjectInputStream input;
		ObjectOutputStream output;
		int idConexao;
		Usuario user;
		ArrayList<ImageIcon> listaImagens;
		ArrayList<Apresentacao> listaApresentacoes;
        public EscutaCliente(ObjectInputStream input,ObjectOutputStream output, int idConexao){
        	this.input = input;
        	this.output = output;
        	this.idConexao = idConexao;
        	listaApresentacoes = new ArrayList<Apresentacao>();
        }
		
		@Override
		public void run() {
		int tipo = 99;
		//Enquanto o tipo da mensagem for diferente da mensagem de fechamento do cliente
		while(tipo!=0){
			try {
              
				Mensagem mensagem = Serializacao.deserializa(input);
				
				if (mensagem != null) {
					tipo = mensagem.getTipo();
					System.out.println(mensagem.getTipo());
					Mensagem msg =  new Mensagem();
					if (mensagem.getTipo() == 1) {  // Tipo login
						usuario = (IUsuario) mensagem.getObject();
						fachada = new ServidorFachadaImpl();
						
						
						if((user = fachada.buscarUsuario(usuario.getLogin(), usuario.getSenha()))!=null){
						    System.out.println("Usuario logado");
						    listaApresentacoes = fachada.carregarListaApresentacao();
						    listaClientes.add(output);
							if(!usuarioExisteNaLista(usuariosLogados, usuario)){
								
								usuariosLogados.add((Usuario) user);
							}
							msg.setMensagemTexto("Usu�rio logado");
							msg.setTipo(2);
							msg.setObject(user);
							Serializacao.serializa(output,msg);// envia confirma��o de usuario logado
							msg.setMensagemTexto("Lista de Usuarios");
							msg.setTipo(4); // 
							msg.setObject(usuariosLogados);//envia lista de usuarios logados
							enviarParaClientes(listaClientes, msg);
							msg.setTipo(7);
							msg.setObject(listaApresentacoes);
							Serializacao.serializa(output, msg);// envia lista de apresentacoes
						}
						else{
							msg.setTipo(3);
							msg.setMensagemTexto("Usu�rio ou senha n�o conferem");
							Serializacao.serializa(output,msg); // envia confirma��o que n�o houve login
							
						}
					}
					if(mensagem.getTipo()==5){// mensagem para fazer o logout
						if(usuariosLogados.contains(user)){
							usuariosLogados.remove(user); //Retira da lista de usuarios logados
							msg.setTipo(6);
							msg.setMensagemTexto("Usuario deslogado com sucesso");
							Serializacao.serializa(output,msg);
							msg.setMensagemTexto("Lista de Usuarios");
							msg.setTipo(4);
							msg.setObject(usuariosLogados);
							enviarParaClientes(listaClientes, msg); //envia a lista novamente dos usuarios logados
							msg.setTipo(7);
						    
						}	
					}
					if(mensagem.getTipo()==8){ // envia a apresenta��o com a lista de imagens
						listaImagens = fachada.carregarApresentacacao((int)mensagem.getObject());
						msg.setTipo(9);
						msg.setObject(listaImagens);
						Serializacao.serializa(output, msg);
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		listaClientes.remove(output);
		
	 }

	}
	
	public boolean usuarioExisteNaLista(ArrayList<Usuario> listaLogados,IUsuario usuario){
		for(Usuario u : listaLogados){
			if(u.getLogin().contains(usuario.getLogin()))
               return true;			
		}
		
		return false;
	}

	public static void main(String[] args) throws Exception {

		// ServidorFachada servidor = new ServidorFachadaImpl();
		// servidor.buscarUsuario("teste","teste");
		new Servidor();

	}

}
