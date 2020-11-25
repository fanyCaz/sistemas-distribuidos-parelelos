package sistema;

import java.net.ServerSocket;
import java.util.HashMap;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.Socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Servidor {
	// Inicializar hash map
	static HashMap<String, Object> ranking = new HashMap<String, Object>(); 
	
    public static void main(String[] args) throws Exception {
    	ranking.put("25.3.236.220", 0);
    	ranking.put("25.5.249.224", 0);
    	ranking.put("25.5.218.12", 0);
    	
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        Socket s = null;
        ServerSocket ss = new ServerSocket(5432);
        int counter = 1;
        System.out.println("Servidor conectado");
        while (true) {
            try {
                // el ServerSocket me da el Socket
                s = ss.accept();
                // enmascaro la entrada y salida de bytes
                ois = new ObjectInputStream(s.getInputStream());
                oos = new ObjectOutputStream(s.getOutputStream());
                String[] clientSysInfo = (String[])ois.readObject();
                
                //Nuevo arreglo de string con infomración del sistema + direccion y nombre del host
                
                String[] newClientSysInfo = {
                		"Nombre del host: " + s.getInetAddress().getHostName(),
                		"Direccion IP: " + s.getInetAddress().getHostAddress(),
                		"Modelo de CPU: " + clientSysInfo[0], // Modelo CPU
                		"Frecuencia de CPU: " + clientSysInfo[1], // Frecuencia de CPU
                		"Velocidad base de CPU: " + clientSysInfo[2], // Velocidad de Base CPU
                		"Porcentaje de uso de CPU: " + clientSysInfo[3], // Porcentaje de uso de CPU
                		"Porcentaje libre de CPU: " + clientSysInfo[4], // Porcentaje libre de CPU
                		"Memoria RAM total: " + clientSysInfo[5], // Memoria RAM Total
                		"Memoria RAM disponible: " + clientSysInfo[6], // Memoria Ram Disponible
                		"Memoria RAM en uso: " + clientSysInfo[7], // Memoria ram en uso
                		"Almacenamiento: " + clientSysInfo[8], // Almacenamiento 
                		"Almacenamiento total: " + clientSysInfo[9], // Almacenamiento total
                		"Almacenamiento libre: " + clientSysInfo[10], // Almacenamiento libre 
                		"Sistemas Operativos: " + clientSysInfo[11], 
                		"Ancho de banda: " + clientSysInfo[12]// SO
                		};
                
                // Suma de ranking
                double prcAlmacenamiento = Double.parseDouble(clientSysInfo[9]) / Double.parseDouble(clientSysInfo[10]);
                double prcRAM = Double.parseDouble(clientSysInfo[5]) / Double.parseDouble(clientSysInfo[6]);
                double prcLibreCPU = Double.parseDouble(clientSysInfo[4]);
                double anchBand = Double.parseDouble(clientSysInfo[12]);
                
                double sumaNuevoRanking = prcAlmacenamiento + prcRAM + prcLibreCPU + anchBand;
                
                //Actualizar ranking en hash
                String direccionCliente = s.getInetAddress().getHostAddress();
                ranking.replace(direccionCliente, sumaNuevoRanking);
                System.out.println("Ranking de " + direccionCliente + " : " + ranking.get(direccionCliente));
                
                
                oos.writeObject("Respuesta recibida!");
                System.out.println("Numero: " + counter);
                for(String info: newClientSysInfo) {
                	System.out.println(info);                	
                } 
                
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
            	counter += 1;
                if (oos != null)
                    oos.close();
                if (ois != null)
                    ois.close();
                if (s != null)
                    s.close();
                System.out.println("Conexion cerrada!");
            }
        }
    }
}