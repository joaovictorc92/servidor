package br.ufma.sistemasdistribuidos.form;

import java.io.Serializable;

public class Mensagem implements Serializable {
	private int tipo;
	private Object object;
	private String mensagemTexto;

	public String getMensagemTexto() {
		return mensagemTexto;
	}

	public void setMensagemTexto(String mensagemTexto) {
		this.mensagemTexto = mensagemTexto;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
}
