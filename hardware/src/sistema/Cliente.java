package sistema;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.util.FormatUtil;

import org.slf4j.Logger;

public class Cliente {
 public static void main(String args[]) throws Exception {
	 //Index.index();
	ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    Socket s = null;
    SystemInfo si = new SystemInfo();
    HardwareAbstractionLayer hal = si.getHardware();
    String cpuModel  = hal.getProcessor().getProcessorIdentifier().toString();
    Long cpuFreq = hal.getProcessor().getMaxFreq();
    String cpuFreqStr = cpuFreq.toString() + " Hz";
    GlobalMemory ram_memory = hal.getMemory();
    String memory = hal.getMemory().toString();
    Long HDD = hal.getDiskStores().get(0).getSize();
    String HDDStr = HDD.toString();
    String SO = System.getProperty("os.name");
	        
    //Uso Procesador
    long[] prevTicks = new long[TickType.values().length];
    double cpuLoad = hal.getProcessor().getSystemCpuLoadBetweenTicks(prevTicks) * 100;
    prevTicks = hal.getProcessor().getSystemCpuLoadTicks();
    System.out.println("load " + cpuLoad);
    CentralProcessor processor = hal.getProcessor();
    System.out.println("procesador \n" + processor.toString() + "\n termino-procesador"); 
    //Memoria RAM
    String totalRam = FormatUtil.formatBytes( ram_memory.getTotal() );
    GlobalMemory gm = hal.getMemory();
    long usedMemory = gm.getTotal() - gm.getAvailable();
    var datosMemoria = hal.getMemory().getPhysicalMemory().toArray();
    for(var dm : datosMemoria) {
    	System.out.println(dm);
    }
    System.out.println("datos memoria finnnn");
    //System.out.println("mreoroijraoi " + hal.getMemory().getPhysicalMemory().toArray()[0]);
    String[] systemInfo = {
	            "Modelo CPU: " + cpuModel, 
	            "Frecuencia CPU: " + cpuFreqStr,
	            "Memoria RAM Total: " + totalRam,
	            "Memoria RAM Disponible: " + memory,
	            "Memoria RAM En Uso " + FormatUtil.formatBytes(usedMemory),
	            "Almacenamiento: " + HDDStr,
	            "Sistema operativo: " + SO
            };
    
    try
    {
    	for(String info: systemInfo) {
    		System.out.println(info);
    	}
    	/*
	    // instancio el server con la IP y el PORT
	    s = new Socket("25.5.218.12",5432);
	    oos = new ObjectOutputStream(s.getOutputStream());
	    ois = new ObjectInputStream(s.getInputStream());
	
	    oos.writeObject(systemInfo);
	    String ret = (String)ois.readObject();
        System.out.println( "Valor de servidor: " + ret);
        */
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
