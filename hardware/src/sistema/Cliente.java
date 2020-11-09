package sistema;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import java.util.List;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.util.FormatUtil;

import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

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

    Long HDD = hal.getDiskStores().get(0).getSize();
    String HDDStr = HDD.toString();
    String SO = System.getProperty("os.name");
	        
    //Uso Procesador
    //long[] prevTicks = new long[TickType.values().length];
    //double cpuLoad = hal.getProcessor().getSystemCpuLoadBetweenTicks(prevTicks) * 100;
    //prevTicks = hal.getProcessor().getSystemCpuLoadTicks();
    //System.out.println("load " + cpuLoad);
    //CentralProcessor processor = hal.getProcessor();
    //System.out.println("procesador \n" + processor.toString() + "\n termino-procesador"); 
    
    //Memoria RAM
    GlobalMemory ram_memory = hal.getMemory();
    String memory = hal.getMemory().toString();
    String totalRam = FormatUtil.formatBytes( ram_memory.getTotal() );
    long prc =  (ram_memory.getAvailable()*100) / ram_memory.getTotal();
    String prcRamDisponible = String.valueOf(prc) + "%";
    String usedMemory = FormatUtil.formatBytes( ram_memory.getTotal() - ram_memory.getAvailable() );
    //Datos como tipo de DDR, manufacturera, etc
    //var datosMemoria = hal.getMemory().getPhysicalMemory().toArray();
    
    //Disco Duro
    OperatingSystem sistOper = si.getOperatingSystem();
    FileSystem sistArchivos = sistOper.getFileSystem();
    List<OSFileStore> archivosLista = sistArchivos.getFileStores();
    String discoLibre = "", discoTotal = "";
    for(OSFileStore fs : archivosLista) {
    	discoLibre = FormatUtil.formatBytes( fs.getFreeSpace() );
    	discoTotal = FormatUtil.formatBytes( fs.getTotalSpace() );
    }
    String[] systemInfo = {
	            "Modelo CPU: " + cpuModel, 
	            "Frecuencia CPU: " + cpuFreqStr,
	            "Memoria RAM Total: " + totalRam,
	            "Memoria RAM Disponible: " + memory,
	            "Memoria RAM En Uso " + usedMemory,
	            "Porcentaje RAM libre: " + prcRamDisponible,
	            "Almacenamiento: " + HDDStr,
	            "Almacenamiento Total: " + discoTotal,
	            "Almacenamiento Libre: " + discoLibre,
	            "Sistema operativo: " + SO,
	            
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
