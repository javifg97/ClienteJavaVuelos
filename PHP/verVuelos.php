<?php

require 'bbdd.php'; // Incluimos fichero en la que est� la coenxi�n con la BBDD

/*
 * Se mostrar� siempre la informaci�n en formato json para que se pueda leer desde un html (via js)
 * o una aplicaci�n m�vil o de escritorio realizada en java o en otro lenguajes
 */

$arrMensaje = array();  // Este array es el codificaremos como JSON tanto si hay resultado como si hay error



$query = "SELECT * FROM vuelos";

$result = $conn->query ( $query );

if (isset ( $result ) && $result) { // Si pasa por este if, la query est� est� bien y se obtiene resultado
	
	if ($result->num_rows > 0) { // Aunque la query est� bien puede no obtenerse resultado (tabla vac�a). Comprobamos antes de recorrer
		
		$arrVuelos = array();
		
		while ( $row = $result->fetch_assoc () ) {
			
			// Por cada vuelta del bucle creamos un vuelo. Como es un objeto hacemos un array asociativo
			$arrVuelo = array();
			// Por cada columna de la tabla creamos una propiedad para el objeto
            $arrVuelo["id"] = $row["idVuelo"];
			$arrVuelo["origen"] = $row["origen"];
			$arrVuelo["destino"] = $row["destino"];
			$arrVuelo["fecha"] = $row["diayhora"];
			$arrVuelo["precio"] = $row["precio"];
			$arrVuelo["plazas_totales"] = $row["plazas_totales"];
			$arrVuelo["plazas_libres"] = $row["plazas_libres"];
			// Por �ltimo, a�adimos el nuevo jugador al array de vuelos
			$arrVuelos[] = $arrVuelo;
			
		}
		
		// A�adimos al $arrMensaje el array de vuelos y a�adimos un campo para indicar que todo ha ido OK
		$arrMensaje["estado"] = "ok";
		$arrMensaje["vuelos"] = $arrVuelos;
		
		
	} else {
		
		$arrMensaje["estado"] = "ok";
		$arrMensaje["vuelos"] = []; // Array vac�o si no hay resultados
	}
	
} else {
	
	$arrMensaje["estado"] = "error";
	$arrMensaje["mensaje"] = "SE HA PRODUCIDO UN ERROR AL ACCEDER A LA BASE DE DATOS";
	$arrMensaje["error"] = $conn->error;
	$arrMensaje["query"] = $query;
	
}

$mensajeJSON = json_encode($arrMensaje,JSON_PRETTY_PRINT);

//echo "<pre>";  // Descomentar si se quiere ver resultado "bonito" en navegador. Solo para pruebas 
echo $mensajeJSON;
//echo "</pre>"; // Descomentar si se quiere ver resultado "bonito" en navegador

$conn->close ();

?>