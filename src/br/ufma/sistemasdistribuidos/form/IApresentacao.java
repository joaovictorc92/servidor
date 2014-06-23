package br.ufma.sistemasdistribuidos.form;

public interface IApresentacao {

	public abstract void setCaminho(String caminho);

	public abstract String getCaminho();

	public abstract void setNome(String nome);

	public abstract String getNome();

	public abstract void setIdapresentacao(Integer idapresentacao);

	public abstract Integer getIdapresentacao();

}
