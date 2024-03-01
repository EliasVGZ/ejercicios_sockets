
package udpenviodatagrama;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**aplicación que primero inicia la comunicación, enviando un datagrama a otra...
 * aquí, (sockets UDP, envío de datagramas), los roles cliente_servidor no están bien delimitados...
 * consideramos que la aplicación que inicia la comunicación con el primer envío
 * es el"cliente", es la aplicación que tiene que conocer la IP y puerto de la aplicación
 * de destino del datagrama
 * @author user
 */
public class UDPenvioDatagrama {

    
    public static void main(String[] args)throws Exception {
       
          int port = 12345; //puerto por el que escucha la otra aplicación
	                    //la aplicación que pretenda enviar datagrama debe conocerlo
	  InetAddress destino = InetAddress.getLocalHost();//IP host local... host destino

	  byte[] mensaje = new byte[1024]; 
	    //matriz de bytes para contener en ella  el mensaje del datagrama a enviar       
	  String Saludo = "Enviando Saludos !!";
	  mensaje = Saludo.getBytes();  //codificar string a bytes para enviarlo
	   
	  //construyo el datagrama a enviar
	  // el objeto DatagramPacket tiene que tener en su constructor 4 argumentos!!
	  // porque es un paquete  para enviar a otra aplicación necesita  además del mensaje y su longitud
          // la dirección  de destino: objeto InetAddress y puerto de destino
          
	  DatagramPacket envio = new DatagramPacket
		                  (mensaje, mensaje.length, destino, port);
          // ya tenemos el paquete a enviar, instaciamos nuestro socket, es un socket DatagramaSocket
	  // podráiamos haber dejado que el sistema eligiera el puerto local asociado 
	  // al socket
      DatagramSocket socket = new DatagramSocket(34567);
	  socket.send(envio);//envio datagrama a destino y port
	  
	  socket.close();
 
        
    }
    
}
