
package udpservidorejemplo2;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServidorEjemplo2 {

    
    public static void main(String[] args)throws IOException {
       
       // instaciamos objeto socket , es un objeto de tipo DatagramSocket
       // Asocio el socket al puerto 12345, PUERTO LOCAL POR EL QUE ESCUCHO
       //importante , no dejamos puerto aleatorio definido por el sistema, porque empezamos proceso escuchando..
		DatagramSocket socket = new DatagramSocket(12345);

		System.out.println("Servidor Esperando Datagrama .......... ");
		// necesito un objeto de tipo o clase DatagramPacket para recibir
		// el Datagrama
		DatagramPacket recibo;
		
		byte[] bufer = new byte[1024]; // buffer para almacenar mensaje del datagrama
		//construimos datagram para recibir, no precisa ip destino ni puerto destino
		recibo = new DatagramPacket(bufer, bufer.length);
                
		socket.receive(recibo); // esperando recibir datagrama	
                
            //obtengo string desde datagrama recibido
	    String mensaje = new String(recibo.getData()).trim();

		System.out.println("Servidor Recibe:" + mensaje);
		
		// ahora esta aplicación realiza el proceso, le damos roll "servidor" por esa razón
		// ambas aplicaciones pueden tener en un momento dado ese roll
		// no están definidos claramente  los roles servidor/cliente
		// proceso a realizar con dato recibido en datagrama es: contar el número de caracteres 'a'
		int contador=0;
		for(int i=0; i < mensaje.length(); i++ )		
			if(mensaje.charAt(i)=='a')
					contador++;
		
		// OBTENGO DESDE EL DATAGRAMA RECIBIDO  LA DIRECCION ORIGEN DEL MENSAJE
		//IP ORIGEN Y PUERTO ORIGEN
		// ESTOS DATOS LOS NECESITO PARA CONSTRUIR EL DATAGRAMA A ENVIAR 
		InetAddress IPOrigen = recibo.getAddress();
		int puerto = recibo.getPort();

		// ENVIANDO DATAGRAMA AL CLIENTE
		System.out.println("Enviando número de apariciones de la letra a=> " + contador);
		byte b = (byte) contador; // paso entero a byte PARA ENVIAR EN DATAGRAMA
		byte[] enviados = new byte[2];
		enviados[0] = b;
		
		DatagramPacket envio = new DatagramPacket(enviados, enviados.length, IPOrigen, puerto);
		// A TRAVÉS DEL MISMO SOCKET AHORA ENVÍO NUEVO DATAGRAMA CONSTRUÍDO
		socket.send(envio);

		// CERRAR STREAMS Y SOCKETS
		System.out.println("Cerrando conexión...");
		socket.close();
 
        
    }
    
}
