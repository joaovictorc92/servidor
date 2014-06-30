package br.ufma.sistemasdistribuidos.form;

public interface IUsuario {

	

	public abstract String getSenha();
	
	public abstract void setLogin(String login);

	public abstract String getLogin();

	public abstract void setNome(String nome);

	public abstract String getNome();

	public abstract void setIdusuario(Integer idusuario);

	public abstract Integer getIdusuario();

	public abstract void setSenha(String senha);

	public abstract void setIdconexao(int idconexao);

	public abstract int getIdconexao();

	public abstract void setIp(String ip);

	public abstract String getIp();

	public abstract void setPorta(int porta);

	public abstract int getPorta();

}
