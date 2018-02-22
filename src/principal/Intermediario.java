package principal;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONObject;

import modelo.Vuelo;

public class Intermediario {

	Scanner teclado;
	AccesoJSONRemoto acceso;

	public Intermediario() {
		this.teclado = new Scanner(System.in); // Para leer las opciones de										// teclado
		this.acceso = new AccesoJSONRemoto();
	}

	public void ejecucion() {
		int op = 0; // Opcion
		boolean salir = false;

		while (!salir) { // Estructura que repite el algoritmo del menu
							// principal hasta que se la condicion sea falsa
			// Se muestra el menu principal
			System.out.println();
			System.out.println("........ MENU ........... \n" + ".  0 Salir \n" + ".  1 Ver Vuelos  \n"
					+ ".  2 Buscar vuelo \n"+ ".  3 Comprar vuelo \n" + "..........................");
			try {
				op = teclado.nextInt();
				teclado.nextLine();
				//System.out.println("OPCION SELECCIONADA:" + op);
				switch (op) {
				case 0:
					salir = true;
					break;
				case 1:
					HashMap<Integer, Vuelo> hm = buscaVuelo();
					pintaVuelos(hm);
					break;
				case 2:
					Vuelo auxVueloBusqueda = this.buscarVuelo();
					//Vuelo auxVuelo = this.buscarVueloPruebas();
					pintaVuelos(acceso.buscarVuelo(auxVueloBusqueda));
				case 3:
					//Vuelo auxVueloCompra = this.buscarVuelo();
					Vuelo auxVueloCompra = this.buscarVueloPruebas();
					pintaVuelos(acceso.buscarVuelo(auxVueloCompra));
					System.out.println("Selecciona el numero del vuelo que desea comprar");
					Scanner sc = new Scanner(System.in);
					int idVuelo = sc.nextInt();
				default:
					//System.out.println("Opcion invalida: marque un numero de 0 a 2");
					break;
				}

				// System.exit(1);

			} catch (InputMismatchException e) {
				System.out.println("Excepcion por opcion invalida: marque un numero de 0 a 2");
				teclado.next();
				
			} catch (Exception e) {
				System.out.println(
						"Excepcion desconocida. Traza de error comentada en el método 'ejecucion' de la clase intermediario");
				// e.printStackTrace();
				System.out.println("Fin ejecución");
				System.exit(-1);
			}
		}

		// teclado.close();

	}

	private HashMap<Integer, Vuelo> buscaVuelo() {

		HashMap<Integer, Vuelo> hmAux = acceso.leerVuelos();

		return hmAux;

	}

	private void pintaVuelos(HashMap<Integer, Vuelo> map) {

		// Recorre el hashmap y va pintando los jugadores (utiliza el método
		// toString de la clase Vuelo
		for (Map.Entry<Integer, Vuelo> entry : map.entrySet()) {
			System.out.println(entry.getValue());
		}

	}



	private Vuelo buscarVuelo() {

		String origen;
		String destino;
		String fecha;
		Vuelo vAux = null;

		try {

			System.out.println("Escriba el origen del vuelo");
			origen = teclado.nextLine();
			System.out.println("Escriba el destino del vuelo");
			destino = teclado.nextLine();
			System.out.println("Escriba la fecha del vuelo (2018-02-06)");
			fecha = teclado.nextLine();

			// Aquí lo lógico sería mostrar el listado de equipos y poder
			// seleccionar uno

			vAux = new Vuelo(origen, destino, fecha);

		} catch (InputMismatchException e) {
			System.out.println("Excepcion por opcion invalida: marque un numero de 0 a 2");
			teclado.next();
		}

		return vAux;

	}
	
	private Vuelo buscarVueloPruebas() {
		
		String origen = "Madrid";
		String destino = "Londres";
		String fecha = "2018-07-03";
		Vuelo vAux = new Vuelo(origen, destino, fecha);

		return vAux;

	}

}
