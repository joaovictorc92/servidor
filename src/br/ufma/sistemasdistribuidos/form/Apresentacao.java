package br.ufma.sistemasdistribuidos.form;

import java.io.Serializable;

public class Apresentacao implements IApresentacao,Serializable {
	
	private Integer idapresentacao;
	private String nome;
	private String caminho;
	private IUsuario palestrante;
	
	@Override
	public IUsuario getPalestrante() {
		return palestrante;
	}
	@Override
	public void setPalestrante(IUsuario palestrante) {
		this.palestrante = palestrante;
	}
	@Override
	public Integer getIdapresentacao() {
		return idapresentacao;
	}
	@Override
	public void setIdapresentacao(Integer idapresentacao) {
		this.idapresentacao = idapresentacao;
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
	public String getCaminho() {
		return caminho;
	}
	@Override
	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}
	
	

}
