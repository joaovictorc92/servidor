package br.ufma.sistemasdistribuidos.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import br.ufma.sistemasdistribuidos.fachada.ServidorFachada;
import br.ufma.sistemasdistribuidos.fachada.ServidorFachadaImpl;
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
			while (true) {
				cliente = servidor.accept();
				ObjectInputStream input = new ObjectInputStream(cliente.getInputStream());
				ObjectOutputStream output = new ObjectOutputStream(cliente.getOutputStream());
				System.out.println("Nova conexão com o cliente "
						+ cliente.getInetAddress().getHostAddress());
				this.idConexao++;
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
        public EscutaCliente(ObjectInputStream input,ObjectOutputStream output, int idConexao){
        	this.input = input;
        	this.output = output;
        	this.idConexao = idConexao;
        }
		
		@Override
		public void run() {
		int tipo = 99;
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
						    
							usuariosLogados.add((Usuario) user);
							if(!listaClientes.contains(user))
								listaClientes.add(output);
							msg.setMensagemTexto("Usuário logado");
							msg.setTipo(2);
							msg.setObject(user);
							Serializacao.serializa(output,msg);
							msg.setMensagemTexto("Lista de Usuarios");
							msg.setTipo(4);
							msg.setObject(usuariosLogados);
							enviarParaClientes(listaClientes, msg);
						}
						else{
							System.out.println("Usuário ou senha não conferem");
							msg.setTipo(3);
							msg.setMensagemTexto("Usuário ou senha não conferem");
							Serializacao.serializa(output,msg);
							
						}
					}
					if(mensagem.getTipo()==5){
						if(usuariosLogados.contains(user)){
							usuariosLogados.remove(user);
							msg.setTipo(6);
							msg.setMensagemTexto("Usuario deslogado com sucesso");
							Serializacao.serializa(output,msg);
							msg.setMensagemTexto("Lista de Usuarios");
							msg.setTipo(4);
							msg.setObject(usuariosLogados);
							enviarParaClientes(listaClientes, msg);
						}	
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

	public static void main(String[] args) throws Exception {

		// ServidorFachada servidor = new ServidorFachadaImpl();
		// servidor.buscarUsuario("teste","teste");
		new Servidor();

	}

}
