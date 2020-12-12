package sistema;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
public class tabla extends JFrame{
	public String ip1 = "a";
	JTable table;
	Object[][] datos;
	public boolean isVisible = true;
	
     public tabla(boolean isServer)
        {
    	 this.isVisible = isServer;
    	 JPanel panel = new JPanel();
    	 panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    	 
//    	 JPanel panel1=new JPanel();
    	 this.datos = new Object[][] {
    		 {"Ip", "%Ram", "%CPU", "Almacenamiento", "Ranking"},
    		 {"25.3.236.220",0.0,0.0,0.0,0.0},
    		 {"25.5.249.224",0.0,0.0,0.0,0.0},
    		 {"25.5.218.12",0.0,0.0,0.0,0.0}
    	 };
    	 	
            //headers for the table
            String[] columns = new String[] {
                "Ip", "%Ram", "%CPU", "Almacenamiento", "Ranking"
            };
            Object[][] estaticos = new Object[][] {
            	{"Datos estaticos", "IP1", "IP2", "IP3"},
            	{"Disco Duro",0,0,0},
            	{"Sistema operativo", 0,0,0},
            	{"RAM total del procesador",0,0,0}
            };
            String[] ipes=new String[] {"Datos estaticos","IP1", "IP2", "IP3"};
            String[] ipes2=new String[] {"Datos dinamicos","IP1","IP2","IP3"};
            Object[][] dinamicos=new Object[][] {
            	{"Datos dinamicos", "IP1","IP2","IP3"},
            	{"Espacio libre HDD","no", "si", "maso"},
            	{"% libre de RAM","si", "maso", "no"},
            	{"% libre de procesador","maso","no","si"},
            	{"% libre de ancho de banda","a","yu","da"},
            };
            //create table with dataS
            
            JTable table = new JTable(datos, columns);
            JTable tablaEstatica =new JTable(estaticos, ipes);
            JTable tablaDinamica=new JTable(dinamicos,ipes2);
            
            JLabel rankings = new JLabel("Tabla de ranking");
            JLabel estaticas = new JLabel("Propiedades estaticas");
            JLabel dinamicas = new JLabel("Propiedades dinamicas");
            //add the table to the frame

            panel.add(rankings, BorderLayout.CENTER);
            panel.add(table, BorderLayout.CENTER);
            
            table.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            panel.add(estaticas, BorderLayout.CENTER);
            panel.add(tablaEstatica, BorderLayout.CENTER);
            panel.add(dinamicas, BorderLayout.CENTER);
            panel.add(tablaDinamica, BorderLayout.CENTER);

            this.add(new JScrollPane(panel));
        
             
            

            this.setTitle("Interfaz");
            
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
            
            this.pack();
            this.setVisible(isVisible);
        }  
     
     void update(String ip, double ram, double cpu, double hdd, double ranking) {
    	 for(Object[] row: datos) {
    		 if(row[0].equals(ip)) {
    			row[1] = ram;
    			row[2] = cpu;
    			row[3] = hdd;
    			row[4] = ranking;
    		 }
    	 }
    	 
    	 this.table.repaint();
     }
     
     void cambiarVisibilidad() {
    	 isVisible = !isVisible;
    	 this.setVisible(isVisible);
     }
     public static void main(String[] args) {
    	 tabla tabla1=new tabla(true);
     }
}