package principal;

import java.io.IOException;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import auxiliares.ApiRequests;
import modelo.Vuelo;

public class AccesoJSONRemoto {

		ApiRequests encargadoPeticiones;
		private String SERVER_PATH, GET_VUELO, SET_VUELO; // Datos de la conexion
		
		HashMap<Integer, Vuelo> hmLeer;
		HashMap<Integer, Vuelo> hmBuscar;
		
		int numeroVuelosLeer;
		int numeroVuelosBuscar;

		public AccesoJSONRemoto() {

			encargadoPeticiones = new ApiRequests();

			SERVER_PATH = "http://localhost/Javi/PHP/";
			GET_VUELO = "verVuelos.php";
			SET_VUELO = "buscarVuelo.php";
			hmLeer = new HashMap<Integer, Vuelo>();
			hmBuscar = new HashMap<Integer, Vuelo>();
			numeroVuelosLeer=0;
			numeroVuelosBuscar=0;

		}

		
		public HashMap<Integer, Vuelo> leerVuelos() {
			hmLeer = new HashMap<Integer, Vuelo>();
			//Cambiar la posicion del HM por el id pero eso hace que solo se descargue el del ultimo servicio que llamas
			//En un caso real creo que no compartirian ids
			numeroVuelosLeer=0;
			leerVuelosIberia();
			
			leerVuelosRyanair();
			

			return hmLeer;
			
		}
		
		public HashMap<Integer, Vuelo> buscarVuelo(Vuelo vuelo) {
			hmBuscar = new HashMap<Integer, Vuelo>();
			numeroVuelosBuscar=0;
			//buscarVueloIberia(vuelo);
			buscarVueloRyanair(vuelo);
			return hmBuscar;
		}
		
		
		public void leerVuelosRyanair() {

			
			
			try {

				System.out.println("---------- Leemos datos de JSON --------------------");

				System.out.println("Lanzamos peticion JSON para vuelos");

				String url = SERVER_PATH + GET_VUELO; // Sacadas de configuracion

				//System.out.println("La url a la que lanzamos la petición es " +  url); // Traza para pruebas

				String response = encargadoPeticiones.getRequest(url);

				//System.out.println(response); // Traza para pruebas

				// Parseamos la respuesta y la convertimos en un JSONObject
				JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

				if (respuesta == null) { // Si hay algún error de parseo (json
											// incorrecto porque hay algún caracter
											// raro, etc.) la respuesta será null
					System.out.println("El json recibido no es correcto. Finaliza la ejecución");
					System.exit(-1);
				} else { // El JSON recibido es correcto
					// Sera "ok" si todo ha ido bien o "error" si hay algún problema
					String estado = (String) respuesta.get("estado"); 
					// Si ok, obtenemos array de jugadores para recorrer y generar hashmap
					if (estado.equals("ok")) { 
						JSONArray array = (JSONArray) respuesta.get("vuelos");

						if (array.size() > 0) {

							// Declaramos variables
							Vuelo nuevoVuelo;
							int id;
							String origen;
							String destino;
							String fecha;
							Double precio;
							int plazasTotales;
							int plazasLibres;
							
							
							for (int i = 0; i < array.size(); i++) {
								JSONObject row = (JSONObject) array.get(i);
								//System.out.println(row.toString());

								id = Integer.parseInt(row.get("id").toString());
								origen = row.get("origen").toString();
								destino = row.get("destino").toString();
								fecha = row.get("fecha").toString();
								precio = Double.parseDouble(row.get("precio").toString());
								plazasTotales = Integer.parseInt(row.get("plazas_totales").toString());
								plazasLibres = Integer.parseInt(row.get("plazas_libres").toString());
								nuevoVuelo = new Vuelo(id, origen, destino, fecha, precio, plazasTotales, plazasLibres,"ry");
								 
								hmLeer.put(numeroVuelosLeer, nuevoVuelo);
								numeroVuelosLeer++;
							}

							System.out.println("Acceso JSON Remoto - Leidos datos correctamente y generado hashmap");
							System.out.println();

						} else { // El array de jugadores está vacío
							System.out.println("Acceso JSON Remoto - No hay datos que tratar");
							System.out.println();
						}

					} else { // Hemos recibido el json pero en el estado se nos
								// indica que ha habido algún error

						System.out.println("Ha ocurrido un error en la busqueda de datos");
						System.out.println("Error: " + (String) respuesta.get("error"));
						System.out.println("Consulta: " + (String) respuesta.get("query"));

						System.exit(-1);

					}
				}

			} catch (Exception e) {
				System.out.println("Ha ocurrido un error en la busqueda de datos");

				e.printStackTrace();

				System.exit(-1);
			}

			
		}
		
		public void leerVuelosIberia() {

			
			
			try {


				JSONObject objPeticion = new JSONObject();



				// Tenemos el vuelo como objeto JSON. Lo añadimos a una peticion
				// Lo transformamos a string y llamamos al
				// encargado de peticiones para que lo envie al PHP

				objPeticion.put("peticion", "leer");

				
				String json = objPeticion.toJSONString();
				
				//System.out.println("Lanzamos peticion JSON para buscar un vuelo");
				
				//String url = SERVER_PATH + SET_VUELO;
				String url = "http://localhost:8080/WBVuelos/MiServlet";

				//System.out.println("La url a la que lanzamos la petición es " + url); // Traza para pruebas
				System.out.println("La búsqueda que enviamos es: ");
				System.out.println(json);
				//System.exit(-1);

				String response = encargadoPeticiones.postRequest(url, json);
				System.out.println(response);
				JSONArray respuesta = (JSONArray) JSONValue.parse(response);
				
				//System.out.println(respuesta);
				for (int i = 0; i < respuesta.size(); i++) {
					JSONObject vueloJson = (JSONObject) respuesta.get(i);
					
					Vuelo vueloAux = new Vuelo(Integer.parseInt(vueloJson.get("idVuelo").toString()), vueloJson.get("origen").toString(), vueloJson.get("destino").toString(), vueloJson.get("diayhora").toString(), Double.parseDouble(vueloJson.get("precio").toString()), Integer.parseInt(vueloJson.get("plazas_totales").toString()), Integer.parseInt(vueloJson.get("plazas_libres").toString()),"ib");
					//System.out.println(vueloAux.toString());
					hmLeer.put(numeroVuelosLeer, vueloAux);
					numeroVuelosLeer++;
				}
				
			} catch (Exception e) {
				System.out.println("Ha ocurrido un error en la busqueda de datos");

				e.printStackTrace();

				System.exit(-1);
			}

			
		}
		
		public void buscarVueloIberia(Vuelo auxVuelo) {
			
			
			try {
				JSONObject objVuelo = new JSONObject();
				JSONObject objPeticion = new JSONObject();

				objVuelo.put("origen", auxVuelo.getOrigen());
				objVuelo.put("destino", auxVuelo.getDestino());
				objVuelo.put("fecha", auxVuelo.getFecha());

				// Tenemos el vuelo como objeto JSON. Lo añadimos a una peticion
				// Lo transformamos a string y llamamos al
				// encargado de peticiones para que lo envie al PHP

				objPeticion.put("peticion", "buscar");
				objPeticion.put("vueloBuscar", objVuelo);
				
				String json = objPeticion.toJSONString();
				
				System.out.println("Lanzamos peticion JSON para buscar un vuelo");
				
				//String url = SERVER_PATH + SET_VUELO;
				String url = "http://localhost:8080/WBVuelos/MiServlet";

				//System.out.println("La url a la que lanzamos la petición es " + url); // Traza para pruebas
				System.out.println("La búsqueda que enviamos es: ");
				System.out.println(json);
				//System.exit(-1);

				String response = encargadoPeticiones.postRequest(url, json);
				
				System.out.println("Los vuelos disponibles para la búsqueda realizada son: ");
				
				System.out.println(response); // Traza para pruebas
				//System.exit(-1);
				
				// Parseamos la respuesta y la convertimos en un JSONObject
				JSONArray respuesta = (JSONArray) JSONValue.parse(response);
				
				//System.out.println(respuesta);
				for (int i = 0; i < respuesta.size(); i++) {
					JSONObject vueloJson = (JSONObject) respuesta.get(i);
					
					Vuelo vueloAux = new Vuelo(Integer.parseInt(vueloJson.get("idVuelo").toString()), vueloJson.get("origen").toString(), vueloJson.get("destino").toString(), vueloJson.get("diayhora").toString(), Double.parseDouble(vueloJson.get("precio").toString()), Integer.parseInt(vueloJson.get("plazas_totales").toString()), Integer.parseInt(vueloJson.get("plazas_libres").toString()),"ib");
					
					//System.out.println(vueloAux.toString());
					hmBuscar.put(numeroVuelosBuscar, vueloAux);
					numeroVuelosBuscar++;
				}

				/*if (respuesta == null) { // Si hay algún error de parseo (json
											// incorrecto porque hay algún caracter
											// raro, etc.) la respuesta será null
					System.out.println("El json recibido no es correcto. Finaliza la ejecución");
					System.exit(-1);
				} else { // El JSON recibido es correcto
					
					// Sera "ok" si todo ha ido bien o "error" si hay algún problema
					String estado = (String) respuesta.get("estado"); 
					if (estado.equals("ok")) {

						System.out.println("Buscado vuelo enviado por JSON Remoto");

					} else { // Hemos recibido el json pero en el estado se nos
								// indica que ha habido algún error

						System.out.println("Acceso JSON REMOTO - Error al almacenar los datos");
						System.out.println("Error: " + (String) respuesta.get("error"));
						System.out.println("Consulta: " + (String) respuesta.get("query"));

						System.exit(-1);

					}
				}*/
			} catch (Exception e) {
				System.out.println(
						"Excepcion desconocida. Traza de error comentada en el método 'annadirJugador' de la clase JSON REMOTO");
				// e.printStackTrace();
				System.out.println("Fin ejecución");
				System.exit(-1);
			}
			

		}	
				
		public void buscarVueloRyanair(Vuelo auxVuelo) {
			String url = SERVER_PATH + SET_VUELO ;
			
			try {
				JSONObject objVuelo = new JSONObject();
				JSONObject objPeticion = new JSONObject();

				objVuelo.put("origen", auxVuelo.getOrigen());
				objVuelo.put("destino", auxVuelo.getDestino());
				objVuelo.put("fecha", auxVuelo.getFecha());

				// Tenemos el vuelo como objeto JSON. Lo añadimos a una peticion
				// Lo transformamos a string y llamamos al
				// encargado de peticiones para que lo envie al PHP

				objPeticion.put("peticion", "buscar");
				objPeticion.put("vueloBuscar", objVuelo);
				
				String json = objPeticion.toJSONString();
				
				//System.out.println(url);
				String response = encargadoPeticiones.postRequest(url, json);
				//System.out.println(response);
				JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());
				//System.out.println(respuesta);
				JSONArray vuelos = (JSONArray) respuesta.get("vuelos");
				
				//System.out.println(respuesta);
				for (int i = 0; i < respuesta.size(); i++) {
					JSONObject vueloJson = (JSONObject) vuelos.get(i);
					//System.out.println(vueloJson);
					Vuelo vueloAux = new Vuelo(Integer.parseInt(vueloJson.get("idVuelo").toString()), vueloJson.get("origen").toString(), vueloJson.get("destino").toString(), vueloJson.get("diayhora").toString(), Double.parseDouble(vueloJson.get("precio").toString()), Integer.parseInt(vueloJson.get("plazas_totales").toString()), Integer.parseInt(vueloJson.get("plazas_libres").toString()),"ry");
					hmBuscar.put(numeroVuelosBuscar, vueloAux);
					numeroVuelosBuscar++;
				}
				//System.out.println(hmBuscar);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
