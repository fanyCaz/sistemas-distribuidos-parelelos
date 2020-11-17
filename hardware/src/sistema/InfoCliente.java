package sistema;

public class InfoCliente {
	
   /* cpuModel,			//Modelo CPU 
    cpuFreqStr,			//Frecuencia CPU
    frecuencyCPU,		//Velocidad de Base CPU
    prcCPU,				//Porcentaje uso CPU
    prcLibreCPU,		//Porcentaje libre CPU
    totalRam,			//Memoria RAM Total
    memory,				//Memoria RAM Disponible
    usedMemory,			//Memoria RAM En Uso
    prcRamDisponible,	//Porcentaje RAM libre
    HDDStr,				//Almacenamiento
    discoTotal,			//Almacenamiento Total
    discoLibre,			//Almacenamiento Libre
    SO,					//Sistema operativo*/
    public String cpuModel;
    public long cpuFrecuencia;
    public double cpuVelocidad;
    public double cpuPrcUso;
    public double cpuPrcLibre;
    public long ramTotal;
    public long ramDisponible;
    public long ramUso;
    public long ramPrcLibre;
    public String ddString;
    public long ddTotal;
    public long ddLibre;
    public String sistemaOperativo;
	public void setCpuMode(String cpumodel) {
    	this.cpuModel = cpumodel;
    }
	public void setCpuFrecuencia(long cpufreq) {
		this.cpuFrecuencia = cpufreq;
	}
	public void setCpuVelocidad(double cpuvelocidad) {
		this.cpuVelocidad = cpuvelocidad;
	}
	public void setCpuPrcUso(double cpuprcuso) {
		this.cpuPrcUso = cpuprcuso;
	}
	public void setCputPrcLibre(double cpuprclibre) {
		this.cpuPrcLibre = cpuprclibre;
	}
	public void setRamTotal(long ramtotal) {
		this.ramTotal = ramtotal;
	}
	public void setRamDisponible(long ramdisp) {
		this.ramDisponible = ramdisp;
	}
	public void setRamUso(long ramuso) {
		this.ramUso = ramuso;
	}
	public void setRamPrcLibre(long ramprclibre) {
		this.ramPrcLibre = ramprclibre;
	}
	public void setDdString(String discoduro) {
		this.ddString = discoduro;
	}
	public void setDdTotal(long ddtotal) {
		this.ddTotal = ddtotal;
	}
	public void setDdLibre(long ddlibre) {
		this.ddLibre = ddlibre;
	}
	public void setSO(String sistoper) {
		this.sistemaOperativo = sistoper;
	}
	
}
