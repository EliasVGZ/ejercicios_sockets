
package udprecibodatagrama;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPreciboDatagrama {

    
    public static void main(String[] args)throws Exception {
       byte[] bufer = new byte[1024];// espacio para recibir el mensaje contenido en el datagrama

    //Asocio el socket al puerto 12345, puerto local por el que escucho
    //importante porque vamos a utilizar el socket para recibir datagramas, estamos suponiendo
    // roll "servidor"
    // no dejamos un puerto aleatorio 
    // este puerto debe conocerlo la aplicación que pretende comenzar a enviarme un datagrama
	
     DatagramSocket socket = new DatagramSocket(12345);  

    //construyo datagrama para recibir, no necesita conocer ip,ni puerto remoto de quien envía  
    DatagramPacket recibo = new DatagramPacket(bufer, bufer.length);
    System.out.println("Esperando Datagrama .......... ");
    
    socket.receive(recibo);// esperando recibo  de datagrama, aquí la aplicación se bloquea
                            //hasta que se recibe el datagrama
    //  a continuación, del objeto  de tipo DatagramPacket (recibo) obtengo número de bytes recibidos
    int bytesRec = recibo.getLength();
	// del objeto DatagramPacket (recibo), obtengo el mensaje
	
    String mensaje= new String(recibo.getData());//obtengo String    
    // analizo contenido y datos en objeto datagramPacket, paquete que hemos recibido
    // desde el paquete recibido podemos ver datos ip, puerto de aplicación remota
    System.out.println("Número de Bytes recibidos: "+ bytesRec);    
    System.out.println("Contenido del Paquete, mensaje: " + mensaje.trim());
    System.out.println("Puerto origen del mensaje: " + recibo.getPort());	
    System.out.println("IP de origen del mensaje: " + recibo.getAddress().getHostAddress());
    
    //analizo  datos desde nuestro socket 
    System.out.println("Puerto local al que está conectado nuestro socket: "+ socket.getLocalPort());
	
	
    socket.close(); //cierro el socket    
    
        
        
    }
    
}
