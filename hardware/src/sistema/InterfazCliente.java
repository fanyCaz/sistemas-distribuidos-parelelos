package sistema;
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
     public InterfazCliente(String ip, boolean isServer)
        {
    	 
    	 Font miEstilo = new Font("Sans Serif", Font.PLAIN, 15);
    	 
    	 this.isVisible = isServer;
         
         panel = new JPanel();
         panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
         panel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
         JLabel label = new JLabel("Enviando propiedades del sistema");
         label.setFont(miEstilo);
         panel.add(label, new BoxLayout(label, BoxLayout.PAGE_AXIS));
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
     
//     public static void main(String[] args) {
//    	 InterfazCliente interfaz1 = new InterfazCliente("AA",true);
//     }
     
         
}
