<?php
header('Content-Type: text/html; charset=UTF-8'); 

$nombre=$_REQUEST['nombre'];
$nombre= str_replace("_", " ", $nombre);
$gasto=$_REQUEST['gasto'];
$descripcion=$_REQUEST['descripcion'];
$descripcion= str_replace("_", " ", $descripcion);
     $dia = date("d");
     $mes = date("m");
     $ano = date("Y");

	//Estableciendo conexion con la base de datos
	$cnx = new PDO("mysql:host=localhost;dbname=gastos","root","");
	$cadena = "SELECT * FROM gasto WHERE 1 = 2";
	$res4 = $cnx->prepare($cadena);	
	$res4->execute();
	$datos = array();
	$res4 = $res4->fetchAll();
       
	foreach ($res4 as $row) 
	{
	    $datos[]=$row;
	}

	if($datos != null)
	{
		echo json_encode($datos, JSON_UNESCAPED_UNICODE);
	}
	else
	{               
		$gasto = "INSERT INTO `gastos`.`gasto` (`pk`,`nombre`, `gasto`,`dia`,`mes`, `ano`, `descripcion`) VALUES (NULL, '$nombre', '$gasto', '$dia','$mes', '$ano', '$descripcion')";
		$res = $cnx->prepare($gasto);	
		$res->execute();
		$res->fetchAll();           
	}   
 ?>

