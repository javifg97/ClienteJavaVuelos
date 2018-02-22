package modelo;

public class Vuelo {
	int id;
	String origen;
	String destino;
	String fecha;
	int plazasTotales;
	int plazasLibres;
	double precio;
	//2018-02-23
	
	
	//Constructores
	
	public Vuelo() {
		
	}
	
	public Vuelo(int id, String origen, String destino, String fecha, double precio, int plazasTotales,	int plazasLibres) {
		this.id = id;
		this.origen = origen;
		this.destino = destino;
		this.fecha = fecha;
		this.precio = precio;
		this.plazasTotales= plazasTotales;
		this.plazasLibres = plazasLibres;
		
	}
	public Vuelo(String origen, String destino, String fecha) {
		this.origen = origen;
		this.destino = destino;
		this.fecha = fecha;
	}
	
	//Getters & Setters

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getOrigen() {
		return origen;
	}

	public String getDestino() {
		return destino;
	}

	public String getFecha() {
		return fecha;
	}

	

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	public String toString(){
		String aux ="";
		
		aux += "------------------------------------------";
		aux += "\n	NUMERO DE VUELO: " + this.id;
		aux += "\n	ORIGEN: " + this.origen;
		aux += "\n	DESTINO: " + this.destino;
		aux += "\n	FECHA: " + this.fecha;
		aux += "\n	PRECIO: " + this.precio;
		aux += "\n	PLAZAS TOTALES: " + this.plazasTotales;
		aux += "\n	PLAZAS LIBRES: " + this.plazasLibres;
		aux += "\n------------------------------------------";
		
		return aux;
	}
	
	

}
