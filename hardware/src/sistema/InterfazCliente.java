package sistema;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InterfazCliente extends JFrame{
	public boolean isVisible = true;
	public JPanel panel;
	public JPanel panelError;
	public Font miEstilo = new Font("Sans Serif", Font.PLAIN, 15);
	public JLabel errorConexion  = new JLabel("");
	public JLabel puntosEspera = new JLabel("");
     public InterfazCliente(boolean isServer)
        {
    	 this.isVisible = isServer;
         
         panel = new JPanel();
         panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
         panel.setBorder(BorderFactory.createEmptyBorder(50,50,50,100));
         JLabel label = new JLabel("Enviando propiedades del sistema                         ...");
         
         label.setFont(miEstilo);
         puntosEspera.setText("");
         errorConexion.setText("");
         
         panel.add(label, new BoxLayout(label, BoxLayout.PAGE_AXIS));
         panel.add(puntosEspera, new BoxLayout(puntosEspera, BoxLayout.PAGE_AXIS));
         panel.add(errorConexion, new BoxLayout(errorConexion, BoxLayout.PAGE_AXIS));
         
         this.setTitle("Interfaz Cliente");
         this.add(panel);
         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         this.setVisible(isVisible);
         this.pack();
        }
     
     void cambiarVisibilidad() {
    	 isVisible = !isVisible;
    	 this.setVisible(isVisible);
     }
     
     void hayError(boolean hayError,String mensaje) {
         if(hayError) {
	         puntosEspera.setText("Ha ocurrido un error");
	         puntosEspera.setFont(miEstilo);
	         errorConexion.setForeground(Color.red);
	         errorConexion.setOpaque(true);
	         errorConexion.setFont(miEstilo);
	         errorConexion.setText(mensaje);
         }else {
        	 puntosEspera.setText("");
        	 errorConexion.setText("");
         }
         this.repaint();
     }
     
         
}
