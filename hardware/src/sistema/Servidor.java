package sistema;
import sistema.tabla;

import java.net.InetAddress;
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

	static tabla tablaRanking;
	static InterfazCliente interfazCliente;
	
	
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
    	isServer = true;
    	
    	
    	String myIp = "25.5.218.12";
    	
    	
    	
    	tablaRanking = new tabla(isServer);    		
    	interfazCliente = new InterfazCliente(myIp,!isServer);
    	
    	while(true) {
    		if(isServer) {
    			try {
    				System.out.println("espera servidor");
    				Thread.sleep(5000);
    				isServer = funcionServidor();
    			} catch(Exception ex) {
    				System.out.println(ex);
    			}    			
    		} else {
    			try {
    				System.out.println("espera cliente");
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
        System.out.println("Servidor conectado");
        int cont = 0;
        while (true) {
            try {
                // el ServerSocket me da el Socket
                s = ss.accept();
                // enmascaro la entrada y salida de bytes
                ois = new ObjectInputStream(s.getInputStream());
                oos = new ObjectOutputStream(s.getOutputStream());
                String[] clientSysInfo = (String[]) ois.readObject();
                
                //Informacion Actual de Server
                String[] ServerSysInfo = funcionObtenerInformacion();
                specs.replace(rankMayor, ServerSysInfo);
                
                // Suma ranking server
                double prcAlmacenamiento = Double.parseDouble(ServerSysInfo[9]) / Double.parseDouble(ServerSysInfo[10]) * 0.2;
                double prcRAM = ((Double.parseDouble(ServerSysInfo[6])/ 1000000000) * 100) / (Double.parseDouble(ServerSysInfo[5]) / 1000000000);
                double prcLibreCPU = Double.parseDouble(ServerSysInfo[4]) * 0.8;
                
                double sumaRankingServer = prcAlmacenamiento +  prcRAM + prcLibreCPU;
               
               // Actualizar ranking server 
                ranking.replace(rankMayor, sumaRankingServer);
                
                
                //Actualizar tabla
                tablaRanking.update(rankMayor,prcRAM, Double.parseDouble(ServerSysInfo[4]), prcAlmacenamiento, sumaRankingServer);
                

                
                //Nuevo arreglo de string con infomración del sistema del cliente + direccion y nombre del host
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
                
                
                
                // Obtener ip
                String direccionCliente = s.getInetAddress().getHostAddress();
                
             // Suma ranking Cliente
                prcAlmacenamiento = Double.parseDouble(clientSysInfo[9]) / Double.parseDouble(clientSysInfo[10]) * 0.2;
                prcRAM = ((Double.parseDouble(clientSysInfo[6])/ 1000000000) * 100) / (Double.parseDouble(clientSysInfo[5]) / 1000000000);
                prcLibreCPU = Double.parseDouble(clientSysInfo[4]) * 0.8;
                
                double sumaRankingCliente = prcAlmacenamiento +  prcRAM + prcLibreCPU;
                
                // Actualizar tabla 
                tablaRanking.update(direccionCliente, prcRAM, Double.parseDouble(clientSysInfo[4]), prcAlmacenamiento, sumaRankingCliente);
                
                //Actualizar ranking en hash
                ranking.replace(direccionCliente, sumaRankingCliente);
//                System.out.println("Ranking de " + direccionCliente + " : " + ranking.get(direccionCliente));
                
                for(Object element: ranking.entrySet()) {
                	System.out.println(element);
    		    }
                
                // Calcular mayor rank
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
                	rankMayor = ipRankMayor;
                    oos.writeObject(new Object[] {true, ranking,rankMayor});
                    //Cambiar interfaces
                    tablaRanking.cambiarVisibilidad();
        			interfazCliente.cambiarVisibilidad();
                    
                    //Cerrar conexion
                    s.close();
                    oos.close();
                    ois.close();
                    return false;
                } else {
                	oos.writeObject(new Object[] {false, ranking});
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
               
                
//                for(String info: newClientSysInfo) {
//                	System.out.println(info);                	
//                } 
                
                
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (oos != null)
                    oos.close();
                if (ois != null)
                    ois.close();
                if (s != null)
                    s.close();
            }
        }
    }
    

    static boolean funcionCliente() throws Exception {
    	
    	boolean isClientServer = false;
    	 //Index.index();
		ObjectOutputStream oos = null;
	    ObjectInputStream ois = null;
	    Socket s = null;

	    Thread.sleep(2000); //Sleep for a bit longer, 2s should cover almost every possible problem
	    String systemInfo[] = funcionObtenerInformacion();
	    try
	    {
//	    	for(var infos: systemInfo) {
//	    		System.out.println(infos);
//	    	}
	    	
		    // instancio el server con la IP y el PORT
		    s = new Socket(rankMayor,5432);
		    oos = new ObjectOutputStream(s.getOutputStream());
		    ois = new ObjectInputStream(s.getInputStream());
		
		    oos.writeObject(systemInfo);
		    Object[] responseArray = (Object[]) ois.readObject();
		    isClientServer = (boolean) responseArray[0];
		    ranking = (HashMap<String, Double>) responseArray[1];
		    rankMayor = (String) responseArray[2];
		    
		    if(isClientServer) {
		    	oos.close();
		    	ois.close();
		    	s.close();
		    }
		    
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
    
    static String[] funcionObtenerInformacion() {
	    SystemInfo si = new SystemInfo();
	    HardwareAbstractionLayer hal = si.getHardware();
	    
	    //Informacion de red
	    var net = hal.getNetworkIFs().get(0);
	    long download1 = net.getBytesRecv();
	    long timestamp1 = net.getTimeStamp();
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
	   return systemInfo;
    }
    
    static double funcionObtenerRanking(String[] SysInfo) {
    	// Suma de ranking
        double prcAlmacenamiento = Double.parseDouble(SysInfo[9]) / Double.parseDouble(SysInfo[10]) * 0.2;
        double prcRAM = ((Double.parseDouble(SysInfo[6])/ 1000000000) * 100) / (Double.parseDouble(SysInfo[5]) / 1000000000);
        double prcLibreCPU = Double.parseDouble(SysInfo[4]) * 0.8;
//        double anchBand = Double.parseDouble(SysInfo[12]) * 0.3;
        
//        System.out.println("Porcentaje almacenamiento: " + prcAlmacenamiento);
//        System.out.println("Porcentaje de RAM: " + prcRAM);
//        System.out.println("Porcentaje CPU: " + prcLibreCPU);
//        System.out.println("AB: " + anchBand);
        
        return (prcAlmacenamiento +  prcRAM + prcLibreCPU);
    }
}