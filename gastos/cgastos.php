<?php
$mes = date("m");
//Estableciendo conexion con la base de datos
 	$cnx = new PDO("mysql:host=localhost;dbname=gastos","root","");
	$cadena = "SELECT * FROM gasto WHERE mes = '$mes' ";
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
		echo json_encode($datos);
	}
	else
	{
            
	}   
 ?>
