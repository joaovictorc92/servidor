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
	String ip;
	public Servidor() {
        usuariosLogados = new ArrayList<Usuario>();
        listaClientes = new ArrayList<ObjectOutputStream>();
        listaApresentacoes = new ArrayList<Apresentacao>();
    	listaApresentacoesAndamento = new ArrayList<Apresentacao>();
    	ip = new String();
		try {
            
			servidor = new ServerSocket(12345); // abre o servidor
			System.out.println("\nPorta 12345 aberta!");
			//Aguarda a requição de um cliente
			Porta porta = new Porta();
			porta.setPorta(12345);
			while (true) { // espera a requisição de um novo cliente
				cliente = servidor.accept();
				ObjectInputStream input = new ObjectInputStream(cliente.getInputStream());
				ObjectOutputStream output = new ObjectOutputStream(cliente.getOutputStream());
				System.out.println("Nova conexão com o cliente "
						+ cliente.getInetAddress().getLocalHost().getHostAddress()); // imprime o endereço do cliente
			
				//Para cada cliente é criada uma thread
				ip = cliente.getInetAddress().getHostAddress();
				new Thread(new EscutaCliente(input,output,ip,porta)).start();

			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    
	public boolean contemApresentacao(ArrayList<Apresentacao> lista,int idApresentacao){ // Verifica se contém apresentação
		for(IApresentacao a: lista){
			if(a.getIdapresentacao()==idApresentacao)
				return true;
		}
		return false;
	}
	
	public void enviarParaClientes(ArrayList<ObjectOutputStream> listaClientes, Object object){// Envia para todos os clientes conectados
		
		for(ObjectOutputStream output : listaClientes){
			Serializacao.serializa(output, object);
		}
		
	}
	
	private class EscutaCliente implements Runnable { // Classe responsavel por tratar as requisições do cliente
		ObjectInputStream input;
		ObjectOutputStream output;
		int idConexao;
		IUsuario user;
		ArrayList<ImageIcon> listaImagens;
		String ip;
        Porta porta;
        public EscutaCliente(ObjectInputStream input,ObjectOutputStream output,String ip,Porta porta){
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
						
						
						if((user = fachada.buscarUsuario(usuario.getLogin(), usuario.getSenha()))!=null){ // verifica se o usuário está cadastrado antes de fazer o login
							user.setIp(ip);
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
							porta.add(); // Incrementa a porta para que um novo palestrante abra a conexão com os ouvintes
							usuario.setPorta(porta.getPorta());
							System.out.println("Ip:"+usuario.getIp()+" Porta:"+usuario.getPorta());
							listaApresentacoes.get(idApresentacao-1).setPalestrante((IUsuario) usuario);
							listaApresentacoesAndamento.add(listaApresentacoes.get(idApresentacao-1));
							msg.setTipo(12);
							msg.setObject(usuario);
							Serializacao.serializa(output, msg);
							msg.setTipo(9);
							msg.setIdApresentacao(idApresentacao);
							msg.setObject(listaImagens); // envia lista de imagens
							Serializacao.serializa(output, msg);
							msg.setTipo(10);
							msg.setObject(listaApresentacoesAndamento); // envia lista de apresentações em andamento
							enviarParaClientes(listaClientes, msg);
							
						}
					}
					if(mensagem.getTipo()==11){//Caso o palestrante fechar a apresentação, exclui apresentação em andamento da lista de apresentações em andamento
						for(int i=0;i<listaApresentacoesAndamento.size();i++){
							if(listaApresentacoesAndamento.get(i).getIdapresentacao()==mensagem.getIdApresentacao()){
								listaApresentacoesAndamento.remove(i);
								i--;
							}
						}
						msg.setTipo(10);
						msg.setObject(listaApresentacoesAndamento);
						enviarParaClientes(listaClientes, msg); // envia lista de apresentações em andamento
						msg.setTipo(16);
						Serializacao.serializa(output, msg);
					}
					if(mensagem.getTipo()==14){// envia apresentação em andamento
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
		msg.setTipo(13); //envia mensagem de confirmação de cliente se desconectando
		Serializacao.serializa(output, msg);
		try {
			output.close();
			cliente.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }

	}
	
	  public class Porta {  
		   private int porta;  
		   public synchronized void setPorta(int porta){this.porta = porta; }
		   public synchronized int getPorta() { return porta; }  
		   public synchronized void add() { porta++; }  
		} 
	
	public boolean usuarioExisteNaLista(ArrayList<Usuario> listaLogados,IUsuario usuario){// Se usuário existe na lista de logados
		for(IUsuario u : listaLogados){
			if(u.getLogin().contains(usuario.getLogin()))
               return true;			
		}
		
		return false;
	}

	public static void main(String[] args) throws Exception {

		new Servidor();

	}

}
