package actividad3_12servidorchat;

import java.io.*;
import java.net.*;

public class HiloServidorChat extends Thread {
    DataInputStream fentrada;
    Socket socket;  // para recibir el socket cliente , el hilo atiende a este cliente
    ComunHilos comun;   // para recibir objeto comun a todos los hilos,en este objeto tenemos referenciados a todos los demás sockets
    private int posicion;   // posición del socket en el array estático

    public HiloServidorChat(Socket s, ComunHilos comun, int posicion) {
        this.socket = s;
        this.comun = comun;     // objeto compartido por todos los hilos
        this.posicion = posicion;

        try {
            // CREO FLUJO DE entrada para leer los mensajes que envía mi cliente desde su aplicación
            // flujo de entrada para leer en él tipos primitivos java
            fentrada = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("ERROR DE E/S");
            System.out.println(e.getMessage());
        }
    }

    // tarea que realiza el hilo para atender a su cliente en el chat
    public void run() {
        System.out.println("NUMERO DE CONEXIONES ACTUALES: " + comun.getACTUALES());

        // NADA MAS CONECTARSE LE ENVIO TODOS LOS MENSAJES
        String texto = comun.getMensajes();
        EnviarMensajesaTodos(texto);
        
        // ahora me pongo a atender a mi cliente en un ciclo donde escucho sus mensajes
        while (true) {
            String cadena;
            try {
                cadena = fentrada.readUTF();

                // Si la conexión no está cerrada y la cadena es distinta del caracter de desconexión, envía el 
                // mensaje a todos los usaurios conectados
                if (!socket.isClosed() && !cadena.trim().equals("*")) {
                    comun.setMensajes(comun.getMensajes() + cadena + "\n");

                    EnviarMensajesaTodos(comun.getMensajes());
                }
            } catch (Exception e) {
//                e.printStackTrace();
                break;
            }
        }

        // se cierra el socket del cliente, porque el cliente se desconecta con un '*'
        try {
            if (!socket.isClosed()) {
                comun.setACTUALES(comun.getACTUALES() - 1);
                comun.agregarPosicionPila(posicion);
                comun.addTabla(null, posicion);
                socket.close();
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    // ENVIA LOS MENSAJES DEL CHAT A LOS CLIENTES
    private void EnviarMensajesaTodos(String texto) {
        int i;
        
        // recorremos tabla de sockets para enviarles los mensajes
        for (i = 0; i < comun.tabla.length; i++) {
            Socket s1 = comun.getElementoTabla(i);
            if (s1 != null) {
                if (!s1.isClosed()) {
                    try {
                        DataOutputStream fsalida = new DataOutputStream(s1.getOutputStream());
                        fsalida.writeUTF(texto);
                    } catch (IOException e) {
//                    e.printStackTrace();
                    }
                }
            }
        }
    }

}
