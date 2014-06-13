package br.ufma.sistemasdistribuidos.servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class Serializacao {

	public static void serializa(OutputStream fluxodesaida, Object object){
		ObjectOutputStream output;
		try {
			output = new ObjectOutputStream(fluxodesaida);
			output.reset();
			output.writeObject(object);
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static <T>T deserializa(InputStream fluxodeentrada) {
		ObjectInputStream input;
		T result = null;
		try {
			input = new ObjectInputStream(fluxodeentrada);
			result = (T)input.readObject();
			//input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
