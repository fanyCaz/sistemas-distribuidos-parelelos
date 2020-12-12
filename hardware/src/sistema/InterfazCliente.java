package sistema;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class InterfazCliente extends JFrame{
	public boolean isVisible = true;
    JFrame f;
     public InterfazCliente(String ip)
        {
         
         f = new JFrame("interfaz 2");
          
         JLabel label = new JLabel();  
         label.setText("ip: "+ip);
         f.getContentPane().setLayout(new FlowLayout());
         f.setSize(300,300); 
         f.setMinimumSize(new Dimension(300, 300));
         f.add(label);
         f.pack();
         f.setVisible(isVisible);
         f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        }
     
     void cambiarVisibilidad() {
    	 isVisible = !isVisible;
    	 f.setVisible(isVisible);
     }
     
         
}
