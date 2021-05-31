<?php
ini_set('default_charset', 'utf-8');
header('Content-Type: text/html; charset=utf-8');

// setup the WordPress environment
define( "WP_INSTALLING", true );
require_once('../wp-load.php' );

global $domain, $base;

date_default_timezone_set('Europe/Madrid');

if (isset($_POST['funcion']) && $_POST['funcion'] != "") {
	$funcion = $_POST['funcion'];

	if(!validar_funcion($funcion)){
	    echo "0 funcion desconocida";
	    die;
	}

	switch ($funcion) {
		case "login": echo login(); die;
	}
	die;
}

function login(){
    global $wpdb;

    $email = $_POST['email'];
    $clave = $_POST['password'];

    $usuario = get_user_by('email', $email);

    if(!$usuario) return 'usuario inexistente';
    if(!wp_check_password($clave, $usuario->user_pass, $usuario->ID)) return 'clave incorrecta';

	$user = get_user_meta( $usuario->ID );

	$datos = [
		'id' => $usuario->ID,
		'email' => $usuario->user_email,
		'password' => $clave
	];

    return json_encode($datos);
}

function validar_funcion($funcion){
    return ctype_alpha($funcion);
}

?>