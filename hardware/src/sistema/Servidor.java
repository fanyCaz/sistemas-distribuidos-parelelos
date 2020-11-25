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
    public static void main(String[] args) throws Exception {
    	// Initialize hash map
    	
    	
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
                String[] newClientSysInfo = {
                		"Nombre del host: " + s.getInetAddress().getHostName(),
                		"Direccion IP: " + s.getInetAddress(),
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
                		"Sistemas Operativos: " + clientSysInfo[11], // SO
                				
                		};
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