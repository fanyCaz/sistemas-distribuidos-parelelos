package sistema;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

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
	JTable table;
	Object[][] datos;
	JPanel panel;
	Object[][] estaticos;
	Object[][] dinamicos;
	public boolean isVisible = true;
	
     public tabla(boolean isServer)
        {
    	 
    	 Font miEstilo = new Font("Sans Serif", Font.PLAIN, 15);
    	 
    	 this.isVisible = isServer;
    	 panel = new JPanel();
    	 panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    	 panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    	 
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
            estaticos = new Object[][] {
            	{"Datos estaticos", "25.3.236.220", "25.5.249.224", "25.5.218.12"},
            	{"Disco Duro",0,0,0},
            	{"Sistema operativo", 0,0,0},
            	{"RAM total",0,0,0},
            	{"Procesador",0,0,0}
            };
            String[] ipes=new String[] {"Datos estaticos","25.3.236.220", "25.5.249.224", "25.5.218.12"};
            String[] ipes2=new String[] {"Datos dinamicos","25.3.236.220","25.5.249.224","25.5.218.12"};
            dinamicos=new Object[][] {
            	{"Datos dinamicos", "25.3.236.220","25.5.249.224","25.5.218.12"},
            	{"Espacio libre HDD",0, 0, 0},
            	{"% libre de RAM",0, 0, 0},
            	{"% libre de procesador",0,0,0},
            	{"% libre de ancho de banda",0,0,0},
            };
            //create table with dataS
            
            JTable table = new JTable(datos, columns);
            table.setFont(miEstilo);
            JTable tablaEstatica =new JTable(estaticos, ipes);
            tablaEstatica.setFont(miEstilo);
            JTable tablaDinamica=new JTable(dinamicos,ipes2);
            tablaDinamica.setFont(miEstilo);
            
            JLabel rankings = new JLabel("Tabla de ranking");
            rankings.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            rankings.setFont(miEstilo);
            JLabel estaticas = new JLabel("Propiedades estaticas");
            estaticas.setFont(miEstilo);
            estaticas.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            
            JLabel dinamicas = new JLabel("Propiedades dinamicas");
            dinamicas.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            dinamicas.setFont(miEstilo);
            //add the table to the frame

            panel.add(rankings, BorderLayout.CENTER);
            panel.add(table, BorderLayout.CENTER);
            panel.add(estaticas, BorderLayout.CENTER);
            panel.add(tablaEstatica, BorderLayout.CENTER);
            panel.add(dinamicas, BorderLayout.CENTER);
            panel.add(tablaDinamica, BorderLayout.CENTER);

            this.add(new JScrollPane(panel));
            this.setTitle("Interfaz servidor");
            this.setMinimumSize(new Dimension(700,400));
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
    	 
    	 this.panel.repaint();
     }
     
     void updateStatic(String ip, String hdd, String SO, String ram, String procesador) {
    	 int index = 3;
    	 if(ip.equals("25.3.236.220")) {
    		 index = 1;
    	 }
    	 else if(ip.equals("25.5.249.224")) {
    		 index = 2;
    	 } 
    	 estaticos[1][index] = hdd;
    	 estaticos[2][index] = SO;
    	 estaticos[3][index] = ram;
    	 estaticos[4][index] = procesador;
    	 this.panel.repaint();
    	 
     }
     
     void updateDinamic(String ip, String hdd, String ram, String procesador, String ancho) {
    	 int index = 3;
    	 if(ip.equals("25.3.236.220")) {
    		 index = 1;
    	 }
    	 else if(ip.equals("25.5.249.224")) {
    		 index = 2;
    	 } 
    	 dinamicos[1][index] = hdd;
    	 dinamicos[2][index] = ram;
    	 dinamicos[3][index] = procesador;
    	 dinamicos[4][index] = ancho;
    	 this.panel.repaint();
     }
     
     void cambiarVisibilidad() {
    	 isVisible = !isVisible;
    	 this.setVisible(isVisible);
     }
     
//     public static void main(String[] args) {
//    	 tabla tabla1 = new tabla(true);
//     }
}