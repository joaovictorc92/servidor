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
import br.ufma.sistemasdistribuidos.form.IApresentacao;
import br.ufma.sistemasdistribuidos.form.IUsuario;
import br.ufma.sistemasdistribuidos.form.Mensagem;
import br.ufma.sistemasdistribuidos.form.Usuario;

public class Servidor {
	ServerSocket servidor;
	Socket cliente;
	ArrayList<Usuario> usuariosLogados;
	ArrayList<ObjectOutputStream> listaClientes;
	ArrayList<Apresentacao> listaApresentacoes;
	ArrayList<Apresentacao> listaApresentacoesAndamento;
	IUsuario usuario;
	ServidorFachada fachada;
	int idConexao;
	String ip;
	int porta;

	public Servidor() {
		this.idConexao = 0;
        usuariosLogados = new ArrayList<Usuario>();
        listaClientes = new ArrayList<ObjectOutputStream>();
        listaApresentacoes = new ArrayList<Apresentacao>();
    	listaApresentacoesAndamento = new ArrayList<Apresentacao>();
    	ip = new String();
		try {
            
			servidor = new ServerSocket(12345);
			System.out.println("\nPorta 12345 aberta!");
			porta = 12345;
			//Aguarda a requição de um cliente
			while (true) {
				cliente = servidor.accept();
				ObjectInputStream input = new ObjectInputStream(cliente.getInputStream());
				ObjectOutputStream output = new ObjectOutputStream(cliente.getOutputStream());
				System.out.println("Nova conexão com o cliente "
						+ cliente.getInetAddress().getHostAddress());
				this.idConexao++;
				//Para cada cliente é criada uma thread
				ip = cliente.getInetAddress().getHostAddress();
				porta++;
				new Thread(new EscutaCliente(input,output,idConexao,ip,porta)).start();

			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    
	public boolean contemApresentacao(ArrayList<Apresentacao> lista,int idApresentacao){
		for(IApresentacao a: lista){
			if(a.getIdapresentacao()==idApresentacao)
				return true;
		}
		return false;
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
		IUsuario user;
		ArrayList<ImageIcon> listaImagens;
		String ip;
		int porta;
        public EscutaCliente(ObjectInputStream input,ObjectOutputStream output, int idConexao,String ip,int porta){
        	this.input = input;
        	this.output = output;
        	this.idConexao = idConexao;
        	this.ip = ip;
        	this.porta = porta;
        }
		
		@Override
		public void run() {
		int tipo = 99;
		//Enquanto o tipo da mensagem for diferente da mensagem de fechamento do cliente
		Mensagem msg =  new Mensagem();
		while(tipo!=0){
			try {
              
				Mensagem mensagem = Serializacao.deserializa(input);
				
				if (mensagem != null) {
					tipo = mensagem.getTipo();
					System.out.println(mensagem.getTipo());
					
					if (mensagem.getTipo() == 1) {  // Tipo login
						usuario = (IUsuario) mensagem.getObject();
						fachada = new ServidorFachadaImpl();
						
						
						if((user = fachada.buscarUsuario(usuario.getLogin(), usuario.getSenha()))!=null){
							user.setIp(ip);
							user.setPorta(porta);
						    System.out.println("Usuario logado");
						    listaApresentacoes = fachada.carregarListaApresentacao();
						    listaClientes.add(output);
							if(!usuarioExisteNaLista(usuariosLogados, usuario)){
								
								usuariosLogados.add((Usuario) user);
							}
							msg.setMensagemTexto("Usuário logado");
							msg.setTipo(2);
							msg.setObject(user);
							Serializacao.serializa(output,msg);// envia confirmação de usuario logado
							msg.setMensagemTexto("Lista de Usuarios");
							msg.setTipo(4); // 
							msg.setObject(usuariosLogados);//envia lista de usuarios logados
							enviarParaClientes(listaClientes, msg);
							msg.setTipo(7);
							msg.setObject(listaApresentacoes);
							Serializacao.serializa(output, msg);// envia lista de apresentacoes
							msg.setTipo(10);
							msg.setObject(listaApresentacoesAndamento);
							enviarParaClientes(listaClientes, msg);
						}
						else{
							msg.setTipo(3);
							msg.setMensagemTexto("Usuário ou senha não conferem");
							Serializacao.serializa(output,msg); // envia confirmação que não houve login
							
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
						}	
					}
					if(mensagem.getTipo()==8){ // envia a apresentação com a lista de imagens
						int idApresentacao = mensagem.getIdApresentacao();
						if(!contemApresentacao(listaApresentacoesAndamento, idApresentacao)){
							listaImagens = fachada.carregarApresentacacao(idApresentacao);
							usuario = (IUsuario) mensagem.getObject();
							System.out.println("IP do Usuario:"+usuario.getIp()+"Porta:"+usuario.getPorta());
							listaApresentacoes.get(idApresentacao-1).setPalestrante((IUsuario) usuario);
							listaApresentacoesAndamento.add(listaApresentacoes.get(idApresentacao-1));
							msg.setTipo(9);
							msg.setIdApresentacao(idApresentacao);
							msg.setObject(listaImagens);
							Serializacao.serializa(output, msg);
							msg.setTipo(10);
							msg.setObject(listaApresentacoesAndamento);
							enviarParaClientes(listaClientes, msg);
							msg.setTipo(12);
							msg.setObject(usuario);
							Serializacao.serializa(output, msg);
						}
					}
					if(mensagem.getTipo()==11){
						for(int i=0;i<listaApresentacoesAndamento.size();i++){
							if(listaApresentacoesAndamento.get(i).getIdapresentacao()==mensagem.getIdApresentacao()){
								listaApresentacoesAndamento.remove(i);
								i--;
							}
						}
						msg.setTipo(10);
						msg.setObject(listaApresentacoesAndamento);
						enviarParaClientes(listaClientes, msg);
						msg.setTipo(16);
						Serializacao.serializa(output, msg);
					}
					if(mensagem.getTipo()==14){
						int idApresentacao = mensagem.getIdApresentacao();
						Apresentacao apresentacao = listaApresentacoesAndamento.get(idApresentacao-1);
						msg.setTipo(15);
						msg.setObject(apresentacao.getPalestrante());
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
		msg.setTipo(13);
		Serializacao.serializa(output, msg);
	 }

	}
	
	public boolean usuarioExisteNaLista(ArrayList<Usuario> listaLogados,IUsuario usuario){
		for(IUsuario u : listaLogados){
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
