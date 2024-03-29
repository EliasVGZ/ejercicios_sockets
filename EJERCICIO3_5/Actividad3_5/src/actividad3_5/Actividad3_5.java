/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actividad3_5;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author user
 */



public class Actividad3_5 {

    /**
     * @param args the command line arguments
     */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		int numeroPuerto = 6000; // Puerto local al que se asocia el socket servidor
		ServerSocket servidor = new ServerSocket(numeroPuerto);

		System.out.println("Esperando al cliente.....");
		Socket cliente = servidor.accept();

		// Se prepara un flujo de salida para escribir objetos al cliente
		ObjectOutputStream outObjeto = new ObjectOutputStream(cliente.getOutputStream());

		// Se obtiene un stream para leer objetos desde el cliente
		ObjectInputStream inObjeto = new ObjectInputStream(cliente.getInputStream());

		Numeros dato = new Numeros();

		try {
			dato = (Numeros) inObjeto.readObject();
		} catch (SocketException ce) {
			System.out.println("ERROR AL RECIBIR DATOS DEL CLIENTE...." + ce.getMessage());
			System.exit(0);
		}

		while (dato.getNumero() > 0) {
			long cuadrado = (long) dato.getNumero() * dato.getNumero();
			long cubo = cuadrado * dato.getNumero();

			dato.setCubo(cubo);
			dato.setCuadrado(cuadrado);

			System.out.println("Recibido y procesado " + dato.getNumero());
			outObjeto.writeObject(dato);

			try {
				dato = (Numeros) inObjeto.readObject();
			} catch (SocketException ce) {
				System.out.println("ERROR AL RECIBIR DATOS DEL CLIENTE...." + ce.getMessage());
				break; // exit the loop if an error occurs
			}
		}

		System.out.println("SERVIDOR FINALIZADO....");

		// CERRAR STREAMS Y SOCKETS
		outObjeto.close();
		inObjeto.close();
		cliente.close();
		// servidor.close(); // Do not close the server socket here
	}


}
