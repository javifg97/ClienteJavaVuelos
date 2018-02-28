<?php

/*  Formato JSON esperado */

$arrEsperado = array();
$arrJugadorEsperado = array();

$arrEsperado["peticion"] = "buscar";

$arrJugadorEsperado["origen"] = "Madrid (Un string)";
$arrJugadorEsperado["destino"] = "Barcelona (Un string)";
$arrJugadorEsperado["fecha"] = "2018-02-23 (Un string)";

$arrEsperado["vueloBuscar"] = $arrJugadorEsperado;


/* Funcion para comprobar si el recibido es igual al esperado */

function JSONCorrectoAnnadir($recibido){
	
	$auxCorrecto = false;
	
	if(isset($recibido["peticion"]) && $recibido["peticion"] ="add" && isset($recibido["vueloBuscar"])){
		
		$auxJugador = $recibido["vueloBuscar"];
		if(isset($auxJugador["origen"]) && isset($auxJugador["destino"]) && isset($auxJugador["fecha"])){
			$auxCorrecto = true;
		}
		
	}
	
	
	return $auxCorrecto;
	
}
