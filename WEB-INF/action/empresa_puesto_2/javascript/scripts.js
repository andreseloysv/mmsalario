//inicializa cuando se carga la pagina
onload = function() {

	var key_codes = [ 8, 13, 37, 38, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57 ];
	var key_codes_num = new Array();
	key_codes_num[48] = 0;
	key_codes_num[49] = 1;
	key_codes_num[50] = 2;
	key_codes_num[51] = 3;
	key_codes_num[52] = 4;
	key_codes_num[53] = 5;
	key_codes_num[54] = 6;
	key_codes_num[55] = 7;
	key_codes_num[56] = 8;
	key_codes_num[57] = 9;
	// addNew(); //prepara formulario para ingresar nuevo registro
	// search(); //mostrar registros en vista paginada

	// $(this).bind('keyup keydown blur update', checkMyDate);

//	$('#fecha').bind(
//			'keydown update',
//			(function(event) {
//				// si es distinto de 8 backspace entonces ejecuto la validacion
//				// de la
//				// fecha.
//
//				if (event.keyCode != 8) {
//					if ($.inArray(event.keyCode, key_codes) < 0) {
//						event.preventDefault();
//					} else {
//						if (checkMyDate(document.getElementById("fecha"),
//								$('#fecha'), event, event.keyCode,
//								key_codes_num) == 1) {
//							if ($('#fecha').val().length == 2) {
//								$('#fecha').val($('#fecha').val() + "-");
//							}
//							if ($('#fecha').val().length == 5) {
//								$('#fecha').val($('#fecha').val() + "-");
//							}
//						}
//					}
//				}
//
//			}));
	$( "#fecha" ).datepicker();
}
function checkMyDate(campo, elementoJquery, event, keycode, key_codes_num) {
	var re = /^(\d{1,2})\-(\d{1,2})\-(\d{4})$/;

	if (elementoJquery.val().length == 0) {
		if ((keycode > 51) || (keycode < 48)) {
			event.preventDefault();
			return 0;
		}
	}
	if (elementoJquery.val().length == 1) {
		if ((key_codes_num[keycode] + (elementoJquery.val() * 10)) > 31) {
			event.preventDefault();
			return 0;
		}
	}
	var str = elementoJquery.val();
	var n = str.split("-");

	if (elementoJquery.val().length == 3) {
		if (key_codes_num[keycode] > 1) {
			event.preventDefault();
			return 0;
		}
	}
	if (elementoJquery.val().length == 4) {
		if ((key_codes_num[keycode] + (n[1] * 10)) > 12) {
			event.preventDefault();
			return 0;
		}
	}
	if (elementoJquery.val().length == 10) {

		if (false) {
			event.preventDefault();
			return 0;
		} else {
			if (!checkDate(campo)) {
				elementoJquery.val("");
			}
		}
	}
	if (elementoJquery.val().length > 10) {
		elementoJquery.val("");
	}
	return 1;

}

function pickIdEmpresa() {
	var url = "${def:context}${def:actionroot}/picklist-empresa/form";
	pickOpen1('nombre_empresa', 'empresa', url, '400', '300', 'nombre_unidad',
			'unidad');
}

// desplegar picklist de seleccion simple
function pickIdUnidad() {
	if (document.getElementById("empresa").value == '') {
		alert('Primero debe seleccionar una empresa');
	} else {
		var url = "${def:context}${def:actionroot}/picklist-unidad/form?empresa="
				+ document.getElementById("empresa").value;
		pickOpen1('nombre_unidad', 'unidad', url, '400', '300',
				'nombre_empleado', 'empleado');
	}
}

function pickIdEmpleado() {
	if (document.getElementById("unidad").value == '') {
		alert('Primero debe seleccionar una unidad');
	} else {
		var url = "${def:context}${def:actionroot}/picklist-empleado/form?unidad="
				+ document.getElementById("unidad").value;
		pickOpen('nombre_empleado', 'empleado', url, '400', '300');
	}
}

// configurar formulario para ingresar registro nuevo
function addNew() {
	clearForm("form1");
	document.getElementById("formTitle").innerHTML = "Crear cuenta de usuario";
	document.getElementById("grabar").disabled = false;
	setFocusOnForm("form1");
}

// invoca un popup para descargar los documentos
function infoExcel() {
	var url = "${def:context}${def:actionroot}/download?id=" + 27;
	var features = "height=500,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	window.location = url;
}

// invoca la generacion de un PDF en un popup
function openExcel() {
	var features = "height=500,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	var url = "${def:context}${def:actionroot}/excel?random=" + Math.random();
	var w = window.open(url, null, features);
}



function afterResponseAjax() {
	$("#cargando").css({
		'display' : 'none'
	});
	document.form2.submit.disabled = true;
	$('#fila_carga_excel').animate(
			{
				height : '198px'
			},
			function() {
				$('#imagen_archivo_excel').css("display", "block");
				var options = {};
				$("#imagen_archivo_excel").hide("highlight", options, 6000,
						pequeno);
			});
	return true;
}
function reCalculaPromedioAjax() {
	ajaxCall(httpMethod = "GET",
			uri = "/action/recalcula_promedio/form",
			divResponse = null, divProgress = "status2", formName = null,
			afterResponseFn = afterResponseAjax, onErrorFn = null);
}


// enviar POST del formulario
function upload() {
	// valido la fecha
	if (checkDate(document.getElementById("fecha"))) {
		// valido el formulario
		if (document.form2.file.value == "") {
			document.form2.file.focus();
			return false;
		}
		capaProtectora();
		document.getElementById("status2").style.display = "";
		reCalculaPromedioAjax();
		return true;
	} else {
		return false;
	}
}

function pequeno() {
	$('#fila_carga_excel').animate({
		height : '102px'
	});
}

// funcion de callback del iframe cuando se carga bien el documento
function uploadOK(total) {
	efecto_hide();
	$("#cargando").css({
		'display' : 'none'
	});
	document.getElementById("status2").style.display = "none";
	document.form2.file.value = "";
	document.form2.submit.disabled = false;
	// alert("Se cargaron exitosamente " + total + " registros.");
	document.form2.file.focus();
	// redirect();
}

// funcion de callback del iframe cuando no carga correctamente el
// documento
function uploadError(msg) {
	document.getElementById("status2").style.display = "none";
	document.form2.submit.disabled = false;
	alert(msg);
}

// ejecutar consulta de errores del archivo Excel
function uploadErrorGrid(msg) {
	// esconder el DIV de progreso para mostrar el alert del error
	document.getElementById("status2").style.display = "none";
	alert(msg);

	// definir funcion goBack()
	_goBackFn = showFilter;

	// llamada Ajax...
	ajaxCall(httpMethod = "GET",
			uri = "${def:actionroot}/importexcel/errorgrid",
			divResponse = "responseError", divProgress = "status2",
			formName = null, afterResponseFn = null, onErrorFn = null);
}
