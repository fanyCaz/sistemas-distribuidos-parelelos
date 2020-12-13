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
	static String rankMayor;
	static boolean isServer;
	static tabla tablaRanking;
	static InterfazCliente interfazCliente;
	
    public static void main(String[] args) {
    	// Inicializando el hash de ranking para las ips
    	ranking.put("25.3.236.220", 0.0);
    	ranking.put("25.5.249.224", 0.0);
    	ranking.put("25.5.218.12", 0.0);
    	
    	// Inicializar servidor mayor
    	rankMayor = "25.5.218.12";
    	isServer = true;
    	
    	//Iniciarlizar interfaces
    	tablaRanking = new tabla(isServer);    		
    	interfazCliente = new InterfazCliente(!isServer);
    	
    	//Ciclo principal de switcheo
    	while(true) {
    		if(isServer) {
    			try {
    				System.out.println("Espera servidor");
    				Thread.sleep(3000);
    				isServer = funcionServidor();
    			} catch(Exception ex) {
    				System.out.println(ex);
    			}    			
    		} else {
    			try {
    				System.out.println("Espera cliente");
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
        
        //Checar si contador es mayor a 3 para ejecutar la prueba de estres
        int contadorEstres = 0;
        while (true) {
        	if(contadorEstres > 3) {
            	pruebaEstres();
            	contadorEstres = 0;
            }
            try {
                // el ServerSocket me da el Socket
                s = ss.accept();
                s.setReuseAddress(true);
                // enmascaro la entrada y salida de bytes
                ois = new ObjectInputStream(s.getInputStream());
                oos = new ObjectOutputStream(s.getOutputStream());
                String[] clientSysInfo = (String[]) ois.readObject();
                
                //Informacion Actual de Server
                String[] ServerSysInfo = funcionObtenerInformacion();
                
                // Suma ranking server
                double prcAlmacenamiento = Double.parseDouble(ServerSysInfo[9]) / Double.parseDouble(ServerSysInfo[10]) * 0.2;
                double prcRAM = ((Double.parseDouble(ServerSysInfo[6])/ 1000000000) * 100) / (Double.parseDouble(ServerSysInfo[5]) / 1000000000);
                double prcLibreCPU = Double.parseDouble(ServerSysInfo[4]) * 0.8;
                
                double sumaRankingServer = prcAlmacenamiento +  prcRAM + prcLibreCPU;
               
               // Actualizar ranking server 
                ranking.replace(rankMayor, sumaRankingServer);
                
                double almacenamiento = Double.parseDouble(clientSysInfo[9]);
                almacenamiento = almacenamiento / 1000000000;
                
                //Actualizar tablas
                tablaRanking.update(rankMayor,prcRAM, Double.parseDouble(ServerSysInfo[4]), prcAlmacenamiento, sumaRankingServer);
                tablaRanking.updateStatic(rankMayor, ""+almacenamiento , ServerSysInfo[11].toString(), ServerSysInfo[5].toString(), ServerSysInfo[0]);
                tablaRanking.updateDinamic(rankMayor, ""+prcAlmacenamiento, ""+prcRAM, ServerSysInfo[4], ServerSysInfo[12]);
                
                // Obtener ip del cliente
                String direccionCliente = s.getInetAddress().getHostAddress();
                
                // Suma ranking Cliente
                prcAlmacenamiento = Double.parseDouble(clientSysInfo[9]) / Double.parseDouble(clientSysInfo[10]) * 0.2;
                prcRAM = ((Double.parseDouble(clientSysInfo[6])/ 1000000000) * 100) / (Double.parseDouble(clientSysInfo[5]) / 1000000000);
                prcLibreCPU = Double.parseDouble(clientSysInfo[4]) * 0.8;
                
                double sumaRankingCliente = prcAlmacenamiento +  prcRAM + prcLibreCPU;
                
                // Actualizar tabla 
                almacenamiento = Double.parseDouble(clientSysInfo[9]);
                almacenamiento = almacenamiento / 1000000000;
                tablaRanking.update(direccionCliente, prcRAM, Double.parseDouble(clientSysInfo[4]), prcAlmacenamiento, sumaRankingCliente);
                tablaRanking.updateStatic(direccionCliente, ""+almacenamiento , clientSysInfo[11].toString(), clientSysInfo[5].toString(), clientSysInfo[0]);
                tablaRanking.updateDinamic(direccionCliente, ""+prcAlmacenamiento, ""+prcRAM, clientSysInfo[4], clientSysInfo[12]);
                
                // Actualizar ranking en hash
                ranking.replace(direccionCliente, sumaRankingCliente);                
                
                // Calcular mayor rank
                double tempMayor = Double.MIN_VALUE;
                String ipRankMayor= "";
                for(Map.Entry<String,Double> element: ranking.entrySet()) {
                	double actualClientRank = element.getValue();
                	if(actualClientRank > tempMayor) {
                		ipRankMayor = element.getKey();
                		tempMayor = actualClientRank;
                	}
                }

                // Si hay otro ip con mayor ranking
                if(!ipRankMayor.equals(rankMayor)) {
                    // Devolver respuesta
                	rankMayor = ipRankMayor;
                    oos.writeObject(new Object[] {true, ranking,rankMayor});
                    //Cambiar interfaces
                    tablaRanking.cambiarVisibilidad();
        			interfazCliente.cambiarVisibilidad();
                    //Cerrar conexion
                    s.close();
                    ss.close();
                    oos.close();
                    ois.close();
                    return false;
                } else {
                	oos.writeObject(new Object[] {false, ranking, rankMayor});
                }
                // INcrementar contador para la prueba de estres
                contadorEstres = contadorEstres + 1;
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

	    String systemInfo[] = funcionObtenerInformacion();
	    try
	    {
		    // instancio el server con la IP y el PORT
		    s = new Socket(rankMayor,5432);
		    s.setReuseAddress(true);
		    oos = new ObjectOutputStream(s.getOutputStream());
		    ois = new ObjectInputStream(s.getInputStream());
		
		    oos.writeObject(systemInfo);
		    Object[] responseArray = (Object[]) ois.readObject();
		    isClientServer = (boolean) responseArray[0];
		    ranking = (HashMap<String, Double>) responseArray[1];
		    rankMayor = (String) responseArray[2];
		    
		    if(isClientServer) {
		    	tablaRanking.cambiarVisibilidad();
    			interfazCliente.cambiarVisibilidad();
		    	oos.close();
		    	ois.close();
		    	s.close();
		    }
	        
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
    	// Instanciando objeto para la info del sistema
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

	    //Informacion procesador
	    String cpuModel  = hal.getProcessor().getProcessorIdentifier().toString();
	    Long cpuFreq = hal.getProcessor().getMaxFreq();
	    
	    //Información de disco duro
	    Long HDD = hal.getDiskStores().get(0).getSize();
	    String HDDStr = HDD.toString();
	    String SO = System.getProperty("os.name");
		        
	    //Uso Procesador
	    CentralProcessor processor = hal.getProcessor();
	    CentralProcessor.ProcessorIdentifier processorId = processor.getProcessorIdentifier();
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
	    double cpuVel = processorId.getVendorFreq() / 1000000000.0;
	    
	    //Memoria RAM
	    GlobalMemory ram_memory = hal.getMemory();
	    long ramUsada = ram_memory.getTotal() - ram_memory.getAvailable();
	    
	    //Espacio de alamacenamiento
	    OperatingSystem sistOper = si.getOperatingSystem();
	    FileSystem sistArchivos = sistOper.getFileSystem();
	    List<OSFileStore> archivosLista = sistArchivos.getFileStores();
	    long numDiscoLibre = 0, numDiscoTotal = 0;
	    for(OSFileStore fs : archivosLista) {
	    	numDiscoLibre = fs.getFreeSpace();
	    	numDiscoTotal = fs.getTotalSpace();
	    }
	    
	   String[] systemInfo = {
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
    
    static void pruebaEstres() {
		try {
			String fileName = "stress.py";
			String path= System.getProperty("user.dir") + "\\src\\sistema\\" + fileName;
			Process process = Runtime.getRuntime().exec(new String[] {"python",path,"30","6"});
		} catch(Exception ex) {
			System.out.println(ex);
		}
    }
}