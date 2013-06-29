var timeoutID=0;
//inicializa cuando se carga la pagina
onload = function() {
	$('input.celda').autoGrowInput({
		comfortZone : 50,
		minWidth : 100,
		maxWidth : 2000
	});
	$('input.celda').trigger('keyup');
}

function inizializaTabla(){
	setTimeout(function(){$('input.celda').autoGrowInput({
		comfortZone : 50,
		minWidth : 100,
		maxWidth : 2000
	});
	$('input.celda').trigger('keyup');}, 500);
		

}

// document.form1.elements[0].focus();

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

// enviar POST del formulario
function upload() {
	if (document.form2.file.value == "") {
		alert("Por favor indique el archivo a ser cargado.");
		document.form2.file.focus();
		return false;
	}

	document.form2.submit.disabled = true;
	document.getElementById("status2").style.display = "";
	return true;
}

// funcion de callback del iframe cuando se carga bien el documento
function uploadOK(total) {
	document.getElementById("status2").style.display = "none";
	document.form2.file.value = "";
	document.form2.submit.disabled = false;
	alert("Se cargaron exitosamente " + total + " registros.");
	document.form2.file.focus();
	redirect();
}

// funcion de callback del iframe cuando no carga correctamente el documento
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


function paginacion(pagina) {
	limpiarTiempo();
	tiempo(pagina);
}
function tiempo(pagina) {
	timeoutID = setTimeout(function(){ajaxBusquedaPagina(pagina)}, 1000);
}

function ajaxBusquedaPagina(pagina){
	ajaxCall(httpMethod = "GET",
			uri = "${def:actionroot}/form?pagina="+pagina,
			divResponse = "tabla_excel", divProgress = "status2",
			formName = null, afterResponseFn = inizializaTabla(), onErrorFn = null);
	
	return true;
}

function limpiarTiempo() {
	  window.clearTimeout(timeoutID);
	}
