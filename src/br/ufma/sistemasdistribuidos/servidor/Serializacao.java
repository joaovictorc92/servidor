package br.ufma.sistemasdistribuidos.servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class Serializacao {

	public static void serializa(ObjectOutputStream output, Object object){
	
		try {
			
			output.reset();
			output.writeObject(object);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static <T>T deserializa(ObjectInputStream input) {
		T result = null;
		try {
			T readObject = (T)input.readObject();
			result = readObject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
