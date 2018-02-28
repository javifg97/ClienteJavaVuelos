<?php

require 'bbdd.php'; // Incluimos fichero en la que está la coenxión con la BBDD
//require 'jsonEsperado.php';

/*
 * Se mostrará siempre la información en formato json para que se pueda leer desde un html (via js)
 * o una aplicación móvil o de escritorio realizada en java o en otro lenguajes
 */

$arrMensaje = array();  // Este array es el codificaremos como JSON tanto si hay resultado como si hay error

/*
 * Lo primero es comprobar que nos han enviado la información via JSON
 */

$parameters = file_get_contents("php://input");

if(isset($parameters)){

	// Parseamos el string json y lo convertimos a objeto JSON
	$mensajeRecibido = json_decode($parameters, true);
	// Comprobamos que están todos los datos en el json que hemos recibido
	// Funcion declarada en jsonEsperado.php
	//if(JSONCorrectoAnnadir($mensajeRecibido)){

		$jugador = $mensajeRecibido["vueloBuscar"]; 
		
		$nombre = $jugador["origen"];
		$equipo = $jugador["destino"];
		$numero = $jugador["fecha"];
		
        $query = "SELECT * FROM vuelos WHERE origen = '$nombre' AND destino = '$equipo' AND diayhora = '$numero'";
		
		$result = $conn->query ( $query );
        
    /*}else{ // Nos ha llegado un json no tiene los campos necesarios
		
        $arrMensaje["estado"] = "error";
		$arrMensaje["mensaje"] = "EL JSON NO CONTIENE LOS CAMPOS ESPERADOS";
		$arrMensaje["recibido"] = $mensajeRecibido;
		$arrMensaje["esperado"] = $arrEsperado;
	}*/
        
}else{	// No nos han enviado el json correctamente
	
	$arrMensaje["estado"] = "error";
	$arrMensaje["mensaje"] = "EL JSON NO SE HA ENVIADO CORRECTAMENTE";
	
}


if (isset ( $result ) && $result) { // Si pasa por este if, la query está está bien y se obtiene resultado
	
	if ($result->num_rows > 0) { // Aunque la query esté bien puede no obtenerse resultado (tabla vacía). Comprobamos antes de recorrer
		
		$arrVuelos = array();
		
		while ( $row = $result->fetch_assoc () ) {
			
			// Por cada vuelta del bucle creamos un vuelo. Como es un objeto hacemos un array asociativo
			$arrVuelo = array();
			// Por cada columna de la tabla creamos una propiedad para el objeto
            $arrVuelo["idVuelo"] = $row["idVuelo"];
			$arrVuelo["origen"] = $row["origen"];
			$arrVuelo["destino"] = $row["destino"];
			$arrVuelo["diayhora"] = $row["diayhora"];
			$arrVuelo["precio"] = $row["precio"];
			$arrVuelo["plazas_totales"] = $row["plazas_totales"];
			$arrVuelo["plazas_libres"] = $row["plazas_libres"];
			// Por último, añadimos el nuevo jugador al array de vuelos
			$arrVuelos[] = $arrVuelo;
			
		}
		
		// Añadimos al $arrMensaje el array de vuelos y añadimos un campo para indicar que todo ha ido OK
		$arrMensaje["estado"] = "ok";
		$arrMensaje["vuelos"] = $arrVuelos;
		
		
	} else {
		
		$arrMensaje["estado"] = "ok";
		$arrMensaje["vuelos"] = []; // Array vacío si no hay resultados
	}
	
}

$mensajeJSON = json_encode($arrMensaje,JSON_PRETTY_PRINT);

//echo "<pre>";  // Descomentar si se quiere ver resultado "bonito" en navegador. Solo para pruebas 
echo $mensajeJSON;
//echo "</pre>"; // Descomentar si se quiere ver resultado "bonito" en navegador

$conn->close ();

?>