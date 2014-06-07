package br.ufma.sistemasdistribuidos.servidor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import br.ufma.sistemasdistribuidos.form.Usuario;


public class Servidor {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ServerSocket servidor = new ServerSocket(5000);
		System.out.println("Porta 12345 aberta!");

		Socket cliente = servidor.accept();
		 System.out.println("Nova conexão com o cliente " +  cliente.getInetAddress().getHostAddress());
		 
		 Usuario usuario = new Usuario();
		 usuario.setNome("testando");
		 usuario.setSenha("123456");
		 
		 ObjectOutputStream output = new ObjectOutputStream(cliente.getOutputStream());
		 output.writeObject(usuario);
		 output.flush();
		 output.close();

	}

}
