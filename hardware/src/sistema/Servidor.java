package sistema;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.management.OperatingSystemMXBean;

import java.net.Socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;

public class Servidor {
	// Inicializar hash map
	static HashMap<String, Double> ranking = new HashMap<String, Double>(); 
	static HashMap<String, Object> specs = new HashMap<String, Object>();
	static String rankMayor;
	static boolean isServer;
	
    public static void main(String[] args) {
    	// Inicializando el hash de ranking para las ips
    	ranking.put("25.3.236.220", 0.0);
    	ranking.put("25.5.249.224", 0.0);
    	ranking.put("25.5.218.12", 0.0);
    	
    	// Inicializando el hash de espcificaciones del sistema para las ips
    	specs.put("25.3.236.220", new String[] {});
    	specs.put("25.5.249.224", new String[] {});
    	specs.put("25.5.218.12", new String[] {});
    	
    	// Inicializar servidor mayor
    	rankMayor = "25.5.218.12";
    	
    	//
    	isServer = true;
    	
    	//
    	while(true) {
    		if(isServer) {
    			try {
    				Thread.sleep(5000);
    				isServer = funcionServidor();
    			} catch(Exception ex) {
    				System.out.println(ex);
    			}    			
    		} else {
    			try {
    				Thread.sleep(5000);
    				isServer = funcionCliente();
    			} catch(Exception ex) {
    				System.out.println(ex);
    			}
    		}
    	} 
    	
    	
    	
        
    }
    
    static boolean funcionServidor() throws Exception {
    	ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        Socket s = null;
        ServerSocket ss = new ServerSocket(5432);
        int counter = 1;
        System.out.println("Servidor conectado");
        int cont = 0;
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
                double prcAlmacenamiento = Double.parseDouble(clientSysInfo[9]) / Double.parseDouble(clientSysInfo[10]) * 0.05;
                double prcRAM = ((Double.parseDouble(clientSysInfo[6])/ 1000000000) * 100) / (Double.parseDouble(clientSysInfo[5]) / 1000000000);
                double prcLibreCPU = Double.parseDouble(clientSysInfo[4]) * 0.8;
                double anchBand = Double.parseDouble(clientSysInfo[12]) * 0.3;
                
//                System.out.println("Porcentaje almacenamiento: " + prcAlmacenamiento);
//                System.out.println("Porcentaje de RAM: " + prcRAM);
//                System.out.println("Porcentaje CPU: " + prcLibreCPU);
//                System.out.println("AB: " + anchBand);
                
                double sumaNuevoRanking = prcAlmacenamiento + prcRAM + prcLibreCPU + anchBand;
                
                // Obtener ip
                String direccionCliente = s.getInetAddress().getHostAddress();
                
                //Actualizar ranking en hash
                ranking.replace(direccionCliente, sumaNuevoRanking);
//                System.out.println("Ranking de " + direccionCliente + " : " + ranking.get(direccionCliente));
                
                for(Object element: ranking.entrySet()) {
                	System.out.println(element);
    		    }
                
                // 
                double tempMayor = Double.MIN_VALUE;
                String ipRankMayor= "";
                for(Map.Entry<String,Double> element: ranking.entrySet()) {
//                	System.out.println(element.getValue());
                	double actualClientRank = element.getValue();
                	if(actualClientRank > tempMayor) {
                		ipRankMayor = element.getKey();
                		tempMayor = actualClientRank;
                	}
                }
                
                if(!ipRankMayor.equals(rankMayor)) {
                    // Devolver respuesta
                    oos.writeObject(new Object[] {true, ranking});
                    
                    //Cerrar conexion
                    s.close();
                    oos.close();
                    ois.close();
                    return false;
                } else {
                	oos.writeObject(false);
                }
                
                System.out.println("El mayor es la ip: " + rankMayor);
                
                
                // Actualizar specs del cliente
                specs.replace(direccionCliente, newClientSysInfo);
                
//                // Obtener specs del cliente
//                for(Map.Entry<String,Object> element: specs.entrySet()) {
//                	System.out.println(element.getKey());
//                	for(String spec: (String[]) element.getValue()) {
//                		System.out.println(spec);
//                	}
//                }
               
                
                System.out.println("Numero: " + counter);
//                for(String info: newClientSysInfo) {
//                	System.out.println(info);                	
//                } 
                
                
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
    
    static boolean funcionCliente() throws Exception {
    	
    	boolean isClientServer = false;
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
	    
	    //System.out.println( f.getProcessCpuLoad() );
	    double prcUsoCPU = f.getProcessCpuLoad() * 100;
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
		    s = new Socket("25.3.236.220",5430);
		    oos = new ObjectOutputStream(s.getOutputStream());
		    ois = new ObjectInputStream(s.getInputStream());
		
		    oos.writeObject(systemInfo);
		    Object[] responseArray = (Object[]) ois.readObject();
		    isClientServer = (boolean) responseArray[0];
		    ranking = (HashMap<String, Double>) responseArray[1];
		    
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
	    
	    return isClientServer;
    }
}