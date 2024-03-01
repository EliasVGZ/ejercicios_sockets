/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actividad3_12servidorchat;

import java.io.*;
import java.net.*;

/**
 * El Stack con las posiciones disponibles está en la clase ComunHilos porque
 * necesita de un acceso sincronizado
 * 
 * Solo se modificó la parte del lado servidor.
 */
public class Actividad3_12ServidorChat {

    private static final int PUERTO = 44444;
    static final int MAXIMO = 5; //MAXIMO DE CONEXIONES PERMITIDAS

    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(PUERTO);
        Socket[] tabla = new Socket[MAXIMO]; //para controlar las  distintas conexiones

        System.out.println("Servidor iniciado...");

        ComunHilos comun = new ComunHilos(MAXIMO, 0, 0, tabla, true); //para distintos hilos

        while (true) {
            // Comprobamos que existen posiciones disponibles en el array
            if (comun.isServidorDisponible()) {
                Socket socket = servidor.accept();

                int posicion = comun.getPila().pop();
                comun.addTabla(socket, posicion);
                comun.setACTUALES(comun.getACTUALES() + 1);
                comun.setCONEXIONES(comun.getCONEXIONES() + 1);

                // Evalución de si el array está completo para bloquear la posibilidad de nuevas conexiones
                if (comun.getACTUALES() == MAXIMO) {
                    comun.setServidorDisponible(false);
                }

                //lanzo un hilo para cada cliente conectado
                // socket distinto para cada hilo, MISMO OBJETO comun PARA TODOS LOS HILOS
                // ESTE OBJETO oomun ES MANIPULADO POR EL HILO PRIMARIO EN CADA NUEVA CONEXIÓN
                // Y POR CADA UNO DE LOS HILOS EN LOS MENSAJES QUE AÑADEN AL CHAT Y EN EL CONTADOR DE CONEXIONES
                HiloServidorChat hilo = new HiloServidorChat(socket, comun, posicion);
                hilo.start();
            }

            // Evalución de si existen posiciones disponibles para habilitar nuevas conexiones
            if (comun.getACTUALES() < MAXIMO) {
                comun.setServidorDisponible(true);
            }

            // por optimización
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Error en la espera: " + e.getMessage());
            }
        }
    }

}
