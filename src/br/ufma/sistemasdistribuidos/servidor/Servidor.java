package br.ufma.sistemasdistribuidos.servidor;

import java.io.IOException;
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
	IUsuario usuario;
	ServidorFachada fachada;

	public Servidor() {
        usuariosLogados = new ArrayList<Usuario>();
		try {

			servidor = new ServerSocket(12345);
			System.out.println("\nPorta 12345 aberta!");
			while (true) {
				cliente = servidor.accept();
				System.out.println("Nova conexão com o cliente "
						+ cliente.getInetAddress().getHostAddress());
				new Thread(new EscutaCliente()).start();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private class EscutaCliente implements Runnable {
		

		@Override
		public void run() {
			try {

				Mensagem mensagem = Serializacao.deserializa(cliente
						.getInputStream());
				Mensagem msg = new Mensagem();
				if (mensagem != null) {
					System.out.println(mensagem.getTipo());
					if (mensagem.getTipo() == 1) {  // Tipo login
						usuario = (IUsuario) mensagem.getObject();
						fachada = new ServidorFachadaImpl();
						System.out.println("Login: "+ usuario.getLogin()+"Senha: "+usuario.getSenha());
						if(fachada.buscarUsuario(usuario.getLogin(), usuario.getSenha())!=null){
						    System.out.println("Usuario logado");
							usuariosLogados.add((Usuario) usuario);
							msg.setMensagemTexto("Usuário logado");
							msg.setTipo(2);
							msg.setObject(usuariosLogados);
							//Serializacao.serializa(cliente.getOutputStream(),msg );
						}
						else{
							System.out.println("Usuário ou senha não conferem");
							msg.setTipo(3);
							msg.setMensagemTexto("Usuário ou senha não conferem");
						}
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public static void main(String[] args) throws Exception {

		// ServidorFachada servidor = new ServidorFachadaImpl();
		// servidor.buscarUsuario("teste","teste");
		new Servidor();

	}

}
