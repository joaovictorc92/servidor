package br.ufma.sistemasdistribuidos.form;

import java.io.Serializable;

public class Usuario implements Serializable,IUsuario {

	private Integer idusuario;
	
	private int idconexao;

	private String nome;
	
	private String login;
	
	private String senha;
	
	private String ip;
	
	private int porta;
    
	@Override
	public int getPorta() {
		return porta;
	}

	@Override
	public void setPorta(int porta) {
		this.porta = porta;
	}

	@Override
	public String getIp() {
		return ip;
	}

	@Override
	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public int getIdconexao() {
		return idconexao;
	}

	@Override
	public void setIdconexao(int idconexao) {
		this.idconexao = idconexao;
	}
	
	@Override
	public Integer getIdusuario() {
		return idusuario;
	}

	@Override
	public void setIdusuario(Integer idusuario) {
		this.idusuario = idusuario;
	}

	@Override
	public String getNome() {
		return nome;
	}

	@Override
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String getLogin() {
		return login;
	}

	@Override
	public void setLogin(String login) {
		this.login = login;
	}

	@Override
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
}
