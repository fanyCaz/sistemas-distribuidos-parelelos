package sistema;

import java.util.*;

import oshi.SystemInfo;
import oshi.driver.windows.wmi.Win32PhysicalMemory.PhysicalMemoryProperty;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.FormatUtil;
import oshi.hardware.GlobalMemory;
import oshi.hardware.PhysicalMemory;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;

import org.slf4j.Logger;

public class Index {
	public static void index() {
		SystemInfo si = new SystemInfo();
	    HardwareAbstractionLayer hal = si.getHardware();
	    //Memoria RAM
	    GlobalMemory gm = hal.getMemory();
	    long usedMemory = gm.getTotal() - gm.getAvailable();
	    String physicalMem = hal.getMemory().getPhysicalMemory().toString();
	    System.out.println("termino " + hal.getMemory().getPhysicalMemory().toArray().length);
	    CentralProcessor processor = hal.getProcessor();
	    
	    long[] prevTicks = new long[TickType.values().length];
	    double cpuLoad = hal.getProcessor().getSystemCpuLoadBetweenTicks(prevTicks) * 100;
	    prevTicks = hal.getProcessor().getSystemCpuLoadTicks();
	    System.out.println("load " + cpuLoad);
	    System.out.println("procesador " + processor.toString() ); 
	    System.out.println("termino " + hal.getMemory().getVirtualMemory().toString() );
	    String cpuModel  = hal.getProcessor().getProcessorIdentifier().toString();
	    Long cpuFreq = hal.getProcessor().getMaxFreq();
	    String cpuFreqStr = cpuFreq.toString() + " Hz";
	    String memory = hal.getMemory().toString();
	    Long HDD = hal.getDiskStores().get(0).getSize();
	    String HDDStr = HDD.toString();
	    String SO = System.getProperty("os.name");
		//String Ram = 
	    String[] systemInfo = {
		            "Modelo CPU: " + cpuModel, 
		            "Frecuencia CPU: " + cpuFreqStr,
		            "Memoria: " + memory,
		            "Memoria Usada " + FormatUtil.formatBytes(usedMemory),
		            "Almacenamiento: " + HDDStr, 
		            "Sistema operativo: " + SO
	            };
	    for(int i=0; i < systemInfo.length; i++) {
	    	System.out.println(systemInfo[i]);
	    }
	}
}
