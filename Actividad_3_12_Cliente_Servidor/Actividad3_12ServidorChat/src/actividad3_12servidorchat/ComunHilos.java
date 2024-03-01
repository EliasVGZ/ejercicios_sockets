package actividad3_12servidorchat;

import java.net.Socket;
import java.util.Stack;

/**
 * clase que define al objeto compartido por todos los hilos del chat tiene
 * definidos varios métodos sincronizados.
 */
public class ComunHilos {

    int CONEXIONES;    // NUMERO  TOTAL DE CONEXIONES REALIZADAS HASTA EL MOMENTO > Desactualizado con el cambio
    int ACTUALES;        // NUMERO DE CONEXIONES ACTUALES
    int MAXIMO;             // MAXIMO DE CONEXIONES PERMITIDAS
    Socket[] tabla;         // SOCKETS CONECTADOS, ARRAY DE SOCKTES PARA COMUNICARSE CON TODOS
    String mensajes;     //MENSAJES DEL CHAT

    private Stack<Integer> pila;    // Pila con las posiciones disponibles en la tabla para nuevos sockets
    private boolean servidorDisponible;     // Comprobación de disponibilidad de espacio para nuevas conexiones

    public ComunHilos(int maximo, int actuales, int conexiones, Socket[] tabla, boolean servidorDisponible) {
        MAXIMO = maximo;
        ACTUALES = actuales;
        CONEXIONES = conexiones;
        this.tabla = tabla;
        mensajes = "";
        this.servidorDisponible = servidorDisponible;

        pila = new Stack<>();
        for (int i = MAXIMO - 1; 0 <= i; i--) {
            pila.add(i);
        }
    }

    public ComunHilos() {
        super();
    }

    public int getMAXIMO() {
        return MAXIMO;
    }

    public void setMAXIMO(int maximo) {
        MAXIMO = maximo;
    }

    public Stack<Integer> getPila() {
        return pila;
    }

    public void setPila(Stack<Integer> pila) {
        this.pila = pila;
    }

    public boolean isServidorDisponible() {
        return servidorDisponible;
    }

    public void setServidorDisponible(boolean servidorDisponible) {
        this.servidorDisponible = servidorDisponible;
    }

    public int getCONEXIONES() {
        return CONEXIONES;
    }

    public synchronized void setCONEXIONES(int conexiones) {
        CONEXIONES = conexiones;
    }

    public String getMensajes() {
        return mensajes;
    }

    public synchronized void setMensajes(String mensajes) {
        this.mensajes = mensajes;
    }

    public int getACTUALES() {
        return ACTUALES;
    }

    public synchronized void setACTUALES(int actuales) {
        ACTUALES = actuales;
    }

    // almacenar  un nuevo socket en posición i
    public synchronized void addTabla(Socket s, int posicion) {
        tabla[posicion] = s;
    }

    public Socket getElementoTabla(int i) {
        return tabla[i];
    }

    public synchronized void agregarPosicionPila(int posicion) {
        pila.add(posicion);
    }

}
