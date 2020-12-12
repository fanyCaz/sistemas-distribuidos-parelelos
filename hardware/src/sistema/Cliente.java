package sistema;

import java.io.ObjectInputStream;
import sistema.tabla;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
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
	    
	    //Informacion de red
	    var net = hal.getNetworkIFs().get(0);
	    long download1 = net.getBytesRecv();
	    long timestamp1 = net.getTimeStamp();
	    Thread.sleep(2000); //Sleep for a bit longer, 2s should cover almost every possible problem
	    net.updateAttributes(); //Updating network stats
	    long download2 = net.getBytesRecv();
	    long timestamp2 = net.getTimeStamp();
	    double bandwidth = (download2 - download1)/(timestamp2 - timestamp1) * 10;
//	    System.out.println(bandwidth);

	    //Do the correct calculations
	    String cpuModel  = hal.getProcessor().getProcessorIdentifier().toString();
	    Long cpuFreq = hal.getProcessor().getMaxFreq();
	    
	    String cpuFreqStr = cpuFreq.toString() + " Hz";
	    
	    Long HDD = hal.getDiskStores().get(0).getSize();
	    String HDDStr = HDD.toString();
	    String SO = System.getProperty("os.name");
		        
	    //Uso Procesador
	    CentralProcessor processor = hal.getProcessor();
	    CentralProcessor.ProcessorIdentifier processorId = processor.getProcessorIdentifier();
	    //System.out.println( processorId.getStepping() );
	    var procesadores = processor.getLogicalProcessors();
//	    for(var pr : procesadores) {
//	    	System.out.println("pr : " + pr.getProcessorNumber());
//	    }
//	    System.out.println( si.getOperatingSystem().getProcessCount());	//Numero de procesos principales
	    OperatingSystemMXBean f = ManagementFactory.getPlatformMXBean( OperatingSystemMXBean.class ) ;
	    double start = System.nanoTime()/1000000;
	    double prcUsoCPU = 0.0;
	    double timeElapsed = 0.0;
	    while(timeElapsed < 2) {
	    	double finish = System.nanoTime()/1000000;
		    timeElapsed = (finish - start) / 1000;
		    
		    prcUsoCPU = f.getSystemCpuLoad();
	    }
	    
	    prcUsoCPU = prcUsoCPU * 100;
	    System.out.println(prcUsoCPU);
	    String prcCPU = String.valueOf(prcUsoCPU) + "%";
	    String prcLibreCPU = String.valueOf(100 - prcUsoCPU) + "%";
	    
	    //System.out.println( "nucleos : " + si.getHardware().getProcessor().getPhysicalProcessorCount() );
	    //System.out.println("sockets  : " + si.getHardware().getProcessor().getPhysicalPackageCount() );
	    //SACA EL NUMERO DE PROCESOS Y EL DE SUBPROCESOS, DIVIDELOS, Y TE DA EL PORCENTAJE DE USO
	    double cpuVel = processorId.getVendorFreq() / 1000000000.0;
	    String frecuencyCPU =  String.valueOf(processorId.getVendorFreq() / 1000000000.0);
	    
	    //Memoria RAM
	    GlobalMemory ram_memory = hal.getMemory();
	    String memory = hal.getMemory().toString();
	    String totalRam = FormatUtil.formatBytes( ram_memory.getTotal() );
	    long prc =  (ram_memory.getAvailable()*100) / ram_memory.getTotal();
	    String prcRamDisponible = String.valueOf(prc) + "%";
	    long ramUsada = ram_memory.getTotal() - ram_memory.getAvailable();
	    String usedMemory = FormatUtil.formatBytes( ramUsada );
	    //Datos como tipo de DDR, manufacturera, etc
	    //var datosMemoria = hal.getMemory().getPhysicalMemory().toArray();
	    
	    //Disco Duro
	    OperatingSystem sistOper = si.getOperatingSystem();
	    FileSystem sistArchivos = sistOper.getFileSystem();
	    List<OSFileStore> archivosLista = sistArchivos.getFileStores();
	    String discoLibre = "", discoTotal = "";
	    long numDiscoLibre = 0, numDiscoTotal = 0;
	    for(OSFileStore fs : archivosLista) {
	    	numDiscoLibre = fs.getFreeSpace();
	    	numDiscoTotal = fs.getTotalSpace();
	    	discoLibre = FormatUtil.formatBytes( fs.getFreeSpace() );
	    	discoTotal = FormatUtil.formatBytes( fs.getTotalSpace() );
	    }
	    
	    InfoCliente info = new InfoCliente();
	    //CPU
	    info.setCpuMode(cpuModel);
	    info.setCpuFrecuencia(cpuFreq);
	    info.setCpuVelocidad(cpuVel);
	    info.setCpuPrcUso(prcUsoCPU);
	    info.setCputPrcLibre( 100 - prcUsoCPU );
	    //RAM
	    info.setRamTotal(ram_memory.getTotal());
	    info.setRamDisponible( ram_memory.getTotal() - ramUsada );
	    info.setRamUso( ramUsada );
	    //Almacenamiento
	    info.setDdString( HDDStr );
	    info.setDdTotal(numDiscoTotal);
	    info.setDdLibre(numDiscoLibre);
	    //SO
	    info.setSO(SO);
	    
	   String[] systemInfo = {
		            /*cpuModel,			//Modelo CPU
		            cpuFreqStr,			//Frecuencia CPU
		            frecuencyCPU,		//Velocidad de Base CPU
		            prcCPU,				//Porcentaje uso CPU
		            "porcentaje libre : " + prcLibreCPU,		//Porcentaje libre CPU
		            totalRam,			//Memoria RAM Total
		            memory,				//Memoria RAM Disponible
		            usedMemory,			//Memoria RAM En Uso
		            prcRamDisponible,	//Porcentaje RAM libre
		            HDDStr,				//Almacenamiento
		            discoTotal,			//Almacenamiento Total
		            discoLibre,			//Almacenamiento Libre
		            SO,					//Sistema operativo*/
	    			cpuModel,
	    			cpuFreq.toString(),
	    			 String.valueOf(cpuVel),
	    			 String.valueOf(prcUsoCPU),
	    			 String.valueOf(100 - prcUsoCPU),
	    			 String.valueOf( ram_memory.getTotal() ),
	    			 String.valueOf(  ram_memory.getTotal() - ramUsada ),
	    			 String.valueOf(ramUsada),
	    			 HDDStr.toString(),
	    			 String.valueOf( numDiscoTotal ),
	    			 String.valueOf( numDiscoLibre ),
	    			 SO,
	    			 String.valueOf(bandwidth)
	   			};
	   
	    try
	    {
//	    	for(var infos: systemInfo) {
//	    		System.out.println(infos);
//	    	}
	    	
		    // instancio el server con la IP y el PORT
		    s = new Socket("25.5.218.12",5432);
		    oos = new ObjectOutputStream(s.getOutputStream());
		    ois = new ObjectInputStream(s.getInputStream());
		
		    oos.writeObject(systemInfo);
		    boolean isServer = (boolean) ois.readObject();
		    
//		    HashMap<String, Object> ranking = (HashMap<String, Object>) ois.readObject();
//		    for(Object element: ranking.entrySet()) {
//		    	System.out.println(element);
//		    }
//	        System.out.println( "Valor de servidor: " + ret);
	        
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

