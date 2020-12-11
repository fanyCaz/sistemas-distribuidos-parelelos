package sistema;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
public class tabla extends JFrame{
	public String ip1 = "a";
	JTable table;
	Object[][] datos;
	
     public tabla()
        {
    	 this.datos = new Object[][] {
    		 {"25.3.236.220",0.0,0.0,0.0,0.0},
    		 {"25.5.249.224",0.0,0.0,0.0,0.0},
    		 {"25.5.218.12",0.0,0.0,0.0,0.0}
    	 };
    	 	
            //headers for the table
            String[] columns = new String[] {
                "Ip", "%Ram", "%CPU", "Almacenamiento", "Ranking"
            };
            //create table with dataS
            table = new JTable(datos, columns);
             
            //add the table to the frame
            this.add(new JScrollPane(table));
             
            this.setTitle("Interfaz");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
            this.pack();
            this.setVisible(true);
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
}