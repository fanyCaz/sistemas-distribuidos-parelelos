package sistema;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import org.slf4j.Logger;

import com.sun.management.OperatingSystemMXBean;

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
    CentralProcessor processor = hal.getProcessor();
    CentralProcessor.ProcessorIdentifier processorId = processor.getProcessorIdentifier();
    System.out.println( processorId.getStepping() );
    var procesadores = processor.getLogicalProcessors();
    for(var pr : procesadores) {
    	System.out.println("pr : " + pr.getProcessorNumber());
    }
    System.out.println( si.getOperatingSystem().getProcessCount() );	//Numero de procesos principales
    OperatingSystemMXBean f = ManagementFactory.getPlatformMXBean( OperatingSystemMXBean.class ) ;
    
    System.out.println( f.getProcessCpuLoad() );
    double prcUsoCPU = f.getSystemCpuLoad() * 100;
    String prcCPU = String.valueOf(prcUsoCPU) + "%";
    String prcLibreCPU = String.valueOf(100 - prcUsoCPU) + "%";
    
    //System.out.println( "nucleos : " + si.getHardware().getProcessor().getPhysicalProcessorCount() );
    //System.out.println("sockets  : " + si.getHardware().getProcessor().getPhysicalPackageCount() );
    //SACA EL NUMERO DE PROCESOS Y EL DE SUBPROCESOS, DIVIDELOS, Y TE DA EL PORCENTAJE DE USO
    String frecuencyCPU =  String.valueOf(processorId.getVendorFreq() / 1000000000.0);
    
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
    long numDiscoLibre, numDiscoTotal;
    for(OSFileStore fs : archivosLista) {
    	discoLibre = FormatUtil.formatBytes( fs.getFreeSpace() );
    	discoTotal = FormatUtil.formatBytes( fs.getTotalSpace() );
    }
    
    
    String[] systemInfo = {
	            "Modelo CPU: " + cpuModel, 
	            "Frecuencia CPU: " + cpuFreqStr,
	            "Velocidad de Base CPU: " + frecuencyCPU,
	            "Porcentaje uso CPU: " + prcCPU,
	            "Porcentaje libre CPU: " + prcLibreCPU,
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
