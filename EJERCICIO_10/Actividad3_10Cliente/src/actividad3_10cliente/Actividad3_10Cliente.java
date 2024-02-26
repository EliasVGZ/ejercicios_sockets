/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actividad3_10cliente;
//proceso lado cliente, está conectado mediante su socket
// a un hilo servidor que lo atiende en exclusividad a él
//utiliza un objeto  datos de tipo Datos para comunicarse con su servidor 


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.*;

public class Actividad3_10Cliente  extends JFrame  {
        static String Host = "localhost";
	static int Puerto = 6001;// puerto remoto por el que escucha el servidor 
	static Socket Cliente = null;
    //para "hablar con el servidor"
	static ObjectOutputStream fsalida;
	static ObjectInputStream fentrada;
	static DatosCliente datosCliente;

	private JPanel contentPane;
	private JLabel lblIdJugador;
	private static JTextField ID;
	private JTextField NUMERO;
	private static JTextField MENSAJE;
	private JLabel lblIntentos;
	private static JTextField INTENTOS;

	/**
	 * Launch the application.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    
    public static void main(String[] args)throws ClassNotFoundException, IOException {
        
        try {
		Cliente = new Socket(Host, Puerto);
		} catch (ConnectException cc) {
			JOptionPane.showMessageDialog(null, "EL SERVIDOR NO ESTÁ CONECTADO: " + cc.getMessage(), 
					"<< ERROR>>",
					JOptionPane.INFORMATION_MESSAGE);

			return;
		}
        // flujo para escribir objetos en el socket
		fsalida = new ObjectOutputStream(Cliente.getOutputStream());
	
		try{
		   fentrada = new ObjectInputStream(Cliente.getInputStream());
		}catch(SocketException ss){
			JOptionPane.showMessageDialog(null,
			"ERROR AL CREAR FLUJO DE ENTRADA "+ss.getMessage(),
			"<< ERROR>>",
			JOptionPane.INFORMATION_MESSAGE);
			return;
		}	
		
		
		// OBTENER PRIMER OBJETO ENVIADO POR EL SERVIDOR
		datosCliente = (DatosCliente) fentrada.readObject();

		if (!datosCliente.isJuega()) {
			JOptionPane.showMessageDialog(null, datosCliente.getCadena(), "<<FIN DE JUEGO>>", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Actividad3_10Cliente frame = new Actividad3_10Cliente();
					ID.setText(String.valueOf(datosCliente.getIdentificador()));
					MENSAJE.setText(datosCliente.getCadena());
					INTENTOS.setText("0");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

        
    }// fin main
    
  private static boolean validarCadena(String cadena) {
		// comprueba si la cadena es numerica
		boolean valor = false;
		try {
			Integer.parseInt(cadena);
			valor = true;
		} catch (NumberFormatException e) {

		}

		return valor;
	}// validarCadena 
  
  private void finalizar() {
		try {
			fsalida.close();
			fentrada.close();
			Cliente.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Fin de proceso... ");
		System.exit(0);

	}// finalizar
  
  
	/**
	 * Create the frame.
	 */
	public Actividad3_10Cliente() {
		setTitle("JUGADOR - ADIVINA UN N\u00DAMERO");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblIdJugador = new JLabel("ID JUGADOR:");
		lblIdJugador.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblIdJugador.setBounds(29, 22, 102, 14);
		contentPane.add(lblIdJugador);

		ID = new JTextField();
		ID.setEditable(false);
		ID.setHorizontalAlignment(SwingConstants.CENTER);
		ID.setForeground(Color.RED);
		ID.setFont(new Font("Tahoma", Font.BOLD, 14));
		ID.setBounds(144, 21, 48, 20);
		contentPane.add(ID);
		ID.setColumns(10);

		JLabel lblNmeroAEnviar = new JLabel("N\u00DAMERO A ADIVINAR:");
		lblNmeroAEnviar.setForeground(Color.BLUE);
		lblNmeroAEnviar.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNmeroAEnviar.setBounds(29, 93, 162, 14);
		contentPane.add(lblNmeroAEnviar);

		NUMERO = new JTextField();
		NUMERO.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 14));
		NUMERO.setHorizontalAlignment(SwingConstants.CENTER);
		NUMERO.setForeground(Color.RED);
		NUMERO.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				String Caracteres = NUMERO.getText();
				if (Caracteres.length() >= 2) {
					arg0.consume();				
				}				

				if (!Character.isDigit(arg0.getKeyChar())){
                    //System.out.println("no es digito");
					arg0.consume();
                }
			}
		});

		
		
		
		
		NUMERO.setBounds(201, 87, 54, 26);
		contentPane.add(NUMERO);
		NUMERO.setColumns(10);

		MENSAJE = new JTextField();
		MENSAJE.setEditable(false);
		MENSAJE.setHorizontalAlignment(SwingConstants.CENTER);
		MENSAJE.setForeground(Color.RED);
		MENSAJE.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 14));
		MENSAJE.setBounds(29, 153, 384, 32);
		contentPane.add(MENSAJE);
		MENSAJE.setColumns(10);

		JButton ENVIAR = new JButton("Enviar");
		ENVIAR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String cadena = NUMERO.getText();
				if (!validarCadena(cadena)) {
					MENSAJE.setText("NUMERO NO VALIDO");
					return;
				}
				int intento = datosCliente.getIntentos() + 1;

				INTENTOS.setText(String.valueOf(intento));
                // un d para  enviar cada jugada
				DatosCliente d = new DatosCliente();
				d.setCadena(cadena);
                //envío mi jugada
				try {
					fsalida.reset();
					fsalida.writeObject(d);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "ERROR AL ENVIAR DATOS AL SERVIDOR", "<< ERROR >>",
							JOptionPane.INFORMATION_MESSAGE);

					return;
					// e.printStackTrace();
				}
                //escucho respuesta del servidor en datos
				try {
					datosCliente = (DatosCliente) fentrada.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "ERROR AL RECIBIR DATOS AL SERVIDOR", "<< ERROR >>",
							JOptionPane.INFORMATION_MESSAGE);

					return;
					// e.printStackTrace();
				}
				// System.out.println("\t"+datos.getCadena());
				MENSAJE.setText(datosCliente.getCadena());

				if (datosCliente.getIntentos()== 5) {
					JOptionPane.showMessageDialog(null, "JUEGO FINALIZADO, NO HAY MÁS INTENTOS", "<< FIN DE JUEGO >>",
							JOptionPane.INFORMATION_MESSAGE);
					ENVIAR.setEnabled(false);

				}
				if (datosCliente.isGana()) {
					JOptionPane.showMessageDialog(null, "HAS GANADO!! EL JUEGO HA TERMINADO", "<< FIN DE JUEGO >>",
							JOptionPane.INFORMATION_MESSAGE);
					ENVIAR.setEnabled(false);

				} else if (!(datosCliente.isJuega())) {
					JOptionPane.showMessageDialog(null, "EL JUEGO HA TERMINADO, HAN ADIVINADO EL NUMERO",
							"<< FIN DE JUEGO >>", JOptionPane.INFORMATION_MESSAGE);
					ENVIAR.setEnabled(false);
				}

			}

		}); // boton enviar

		ENVIAR.setBounds(298, 84, 115, 32);
		contentPane.add(ENVIAR);

		JButton salir = new JButton("Salir");
		salir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				finalizar();
			}

		});
		salir.setBounds(174, 212, 102, 38);
		contentPane.add(salir);

		lblIntentos = new JLabel("INTENTOS:");
		lblIntentos.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblIntentos.setBounds(218, 23, 102, 14);
		contentPane.add(lblIntentos);

		INTENTOS = new JTextField();
		INTENTOS.setHorizontalAlignment(SwingConstants.CENTER);
		INTENTOS.setForeground(Color.RED);
		INTENTOS.setFont(new Font("Tahoma", Font.BOLD, 14));
		INTENTOS.setEditable(false);
		INTENTOS.setColumns(10);
		INTENTOS.setBounds(333, 22, 48, 20);
		contentPane.add(INTENTOS);
	}
    
}
