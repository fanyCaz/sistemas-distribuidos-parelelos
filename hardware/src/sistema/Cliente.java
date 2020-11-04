package sistema;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;


import org.slf4j.Logger;

public class Cliente {
 public static void main(String args[]) throws Exception {
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    Socket s = null;
    SystemInfo si = new SystemInfo();
    HardwareAbstractionLayer hal = si.getHardware();
    String cpuModel  = hal.getProcessor().getProcessorIdentifier().toString();
    Long cpuFreq = hal.getProcessor().getMaxFreq();
    String cpuFreqStr = cpuFreq.toString() + " Hz";
    String memory = hal.getMemory().toString();
    Long HDD = hal.getDiskStores().get(0).getSize();
    String HDDStr = HDD.toString();
    String SO = System.getProperty("os.name");
	        
    String[] systemInfo = {
	            "Modelo CPU: " + cpuModel, 
	            "Frecuencia CPU: " + cpuFreqStr,
	            "Memoria: " + memory, 
	            "Almacenamiento: " + HDDStr, 
	            "Sistema operativo: " + SO
            };
    try
    {
	    // instancio el server con la IP y el PORT
	    s = new Socket("25.5.218.12",5432);
	    oos = new ObjectOutputStream(s.getOutputStream());
	    ois = new ObjectInputStream(s.getInputStream());
	
	    oos.writeObject(systemInfo);
	    String ret = (String)ois.readObject();
        System.out.println( "Valor de servidor: " + ret);
        
    }
    catch(Exception ex)
    {
    	ex.printStackTrace();
    }
    finally
    {
	    if( ois != null ) ois.close();
	    if( oos != null ) oos.close();
	    if( s != null ) s.close();
    }
 }
}
