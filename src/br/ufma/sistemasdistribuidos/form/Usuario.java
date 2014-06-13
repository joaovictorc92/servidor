package br.ufma.sistemasdistribuidos.form;

import java.io.Serializable;

public class Usuario implements Serializable,IUsuario {

	private Integer idusuario;
	
	private String nome;
	
	private String login;
	
	private String senha;

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
