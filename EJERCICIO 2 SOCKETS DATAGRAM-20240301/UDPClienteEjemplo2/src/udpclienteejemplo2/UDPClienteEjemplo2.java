
package udpclienteejemplo2;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.util.Scanner;

public class UDPClienteEjemplo2 {
 private static Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args)throws IOException {
        
       DatagramSocket clientSocket = new DatagramSocket();// socket datagram 
		//como socket será utilizado en primer lugar para enviar dejamos puerto aleatorio,
                //al ser los primeros en comunicar, tenemos roll "cliente"

		// DATOS DEL SERVIDOR al que enviar mensaje
		InetAddress IPServidor = InetAddress.getLocalHost();// localhost
		int puerto = 12345; // puerto por el que escucha otra aplicación "servidor"

		// INTRODUCIR DATOS POR TECLADO
		System.out.print("Introduce mensaje a enviar: ");
		String cadena = sc.nextLine();

		byte[] enviados = new byte[1024];
		//codificando mensaje a bytes para enviarlo en el paquete
		enviados = cadena.getBytes();

		// CONSTRUYO PAQUETE DATAGRAMA PARA HACER ENVÌO DEL DATAGRAMA AL SERVIDOR, IPServidor y puerto
		DatagramPacket envio = new DatagramPacket(enviados, enviados.length, IPServidor, puerto);
		clientSocket.send(envio);

		// RECIBIENDO DATAGRAMA DEL SERVIDOR
		byte[] recibidos = new byte[2];
		// construyo datagram para recibir mensaje
		//objeto DatagramPacket para recibir no necesita ip y puerto
		DatagramPacket recibo = new DatagramPacket(recibidos, recibidos.length);
		System.out.println("Esperando datagrama....");
                //POR EL MISMO SOCKET ME PONGO A LA ESPERA DE RECIBIR DATAGRAMA  
		clientSocket.receive(recibo);

		// Obtener el número e caracteres
		byte[] hh = recibo.getData();
		int numero = hh[0];

		System.out.println("Recibo Nº de caracteres que son a=> " + numero);

		clientSocket.close();// cerrar socket 
        
    }
    
}
