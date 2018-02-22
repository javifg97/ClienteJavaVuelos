package principal;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import auxiliares.ApiRequests;
import modelo.Vuelo;

public class AccesoJSONRemoto {

		ApiRequests encargadoPeticiones;
		private String SERVER_PATH, GET_VUELO, SET_VUELO; // Datos de la conexion

		public AccesoJSONRemoto() {

			encargadoPeticiones = new ApiRequests();

			SERVER_PATH = "http://localhost/ACCESO-DATOS-FINAL-VUELOS/PHP/";
			GET_VUELO = "verVuelos.php";
			SET_VUELO = "buscarVuelo.php";

		}

		public HashMap<Integer, Vuelo> leerVuelos() {

			HashMap<Integer, Vuelo> auxhm = new HashMap<Integer, Vuelo>();
			
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

							for (int i = 0; i < array.size(); i++) {
								JSONObject row = (JSONObject) array.get(i);

								id = Integer.parseInt(row.get("id").toString());
								origen = row.get("origen").toString();
								destino = row.get("destino").toString();
								fecha = row.get("fecha").toString();
								nuevoVuelo = new Vuelo(origen, destino, fecha);

								auxhm.put(id, nuevoVuelo);
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

			return auxhm;
		}
		
		public HashMap<Integer, Vuelo> buscarVuelo(Vuelo auxVuelo) {
			HashMap<Integer, Vuelo> auxhm = new HashMap<Integer, Vuelo>();
			
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
				
				//System.out.println(response); // Traza para pruebas
				//System.exit(-1);
				
				// Parseamos la respuesta y la convertimos en un JSONObject
				JSONArray respuesta = (JSONArray) JSONValue.parse(response);
				
				//System.out.println(respuesta);
				for (int i = 0; i < respuesta.size(); i++) {
					JSONObject vueloJson = (JSONObject) respuesta.get(i);
					
					Vuelo vueloAux = new Vuelo(Integer.parseInt(vueloJson.get("idVuelo").toString()), vueloJson.get("origen").toString(), vueloJson.get("destino").toString(), vueloJson.get("diayhora").toString(), Double.parseDouble(vueloJson.get("precio").toString()), Integer.parseInt(vueloJson.get("plazas_totales").toString()), Integer.parseInt(vueloJson.get("plazas_libres").toString()));
					//System.out.println(vueloAux.toString());
					auxhm.put(Integer.parseInt(vueloJson.get("idVuelo").toString()), vueloAux);
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
			return auxhm;

		}	
				
	}
