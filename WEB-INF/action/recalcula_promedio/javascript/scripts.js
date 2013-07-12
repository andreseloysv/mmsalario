var timeoutID = 0;
var nombre_empresa="";
var someElement = $("#cargando"), overlay;

// inicializa cuando se carga la pagina
onload = function() {

	addNew(); //prepara formulario para ingresar nuevo registro 
	search(); //mostrar registros en vista paginada
	
	$("#nombre_empresa").focusin(function () {
		if(nombre_empresa!=$(this).val()){
			var str=$("#nombre_empresa").val();
			var n=str.split(" - ");
			$("#nombre_empresa").val(n[0]);
			$("#fecha_ini_registro").val(n[1]);
		}
		});

}

function capaProtectora() {
	var docHeight = $(document).height();
	$("body").append("<div id='cargando'></div>");
	$("#cargando").height(docHeight).css({
		'opacity' : 0.4,
		'position' : 'absolute',
		'top' : 0,
		'left' : 0,
		'background-color' : 'black',
		'width' : '100%',
		'z-index' : 5000
	});
}

function inizializaTabla() {
	$(document.getElementById("max_page")).text("/" + $("#PageCount").val());
	// setTimeout(function() {
	$('input.celda').autoGrowInput({
		comfortZone : 50,
		minWidth : 100,
		maxWidth : 2000
	});
	// $('input.celda').trigger('keyup');
	// document.getElementById('campo_busqueda').focus();
	// }, 3000);
	// overlay.destroy();
	$("#cargando").css({
		'display' : 'none'
	});
	// cargaDialogo();

}

// document.form2.elements[0].focus();

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

function cargaDialogo() {
	var name = $("#name"), email = $("#email"), password = $("#password"), allFields = $(
			[]).add(name).add(email).add(password), tips = $(".validateTips");

	// //////////////////Control de dialogos

	function updateTips(t) {
		tips.text(t).addClass("ui-state-highlight");
		setTimeout(function() {
			tips.removeClass("ui-state-highlight", 1500);
		}, 500);
	}

	function checkLength(o, n, min, max) {
		if (o.val().length > max || o.val().length < min) {
			o.addClass("ui-state-error");
			updateTips("Length of " + n + " must be between " + min + " and "
					+ max + ".");
			return false;
		} else {
			return true;
		}
	}

	function checkRegexp(o, regexp, n) {
		if (!(regexp.test(o.val()))) {
			o.addClass("ui-state-error");
			updateTips(n);
			return false;
		} else {
			return true;
		}
	}
	$("#dialog-form")
			.dialog(
					{
						autoOpen : false,
						height : 400,
						width : 550,
						modal : true,
						buttons : {
							"Create an account" : function() {
								var bValid = true;
								allFields.removeClass("ui-state-error");

								bValid = bValid
										&& checkLength(name, "username", 3, 16);
								bValid = bValid
										&& checkLength(email, "email", 6, 80);
								bValid = bValid
										&& checkLength(password, "password", 5,
												16);

								bValid = bValid
										&& checkRegexp(name,
												/^[a-z]([0-9a-z_])+$/i,
												"Username may consist of a-z, 0-9, underscores, begin with a letter.");
								// From jquery.validate.js (by joern),
								// contributed
								// by Scott Gonzalez:
								// http://projects.scottsplayground.com/email_address_validation/
								bValid = bValid
										&& checkRegexp(
												email,
												/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i,
												"eg. ui@jquery.com");
								bValid = bValid
										&& checkRegexp(password,
												/^([0-9a-zA-Z])+$/,
												"Password field only allow : a-z 0-9");

								if (bValid) {
									$("#users tbody").append(
											"<tr>" + "<td>" + name.val()
													+ "</td>" + "<td>"
													+ email.val() + "</td>"
													+ "<td>" + password.val()
													+ "</td>" + "</tr>");
									$(this).dialog("close");
								}
							},
							Cancel : function() {
								$(this).dialog("close");
							}
						},
						close : function() {
							allFields.val("").removeClass("ui-state-error");
						}
					});
	// $("#create-user").button().click(function() {
	$("#dialog-form").dialog("open");
	// });

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
	clearForm("form2");
	document.getElementById("formTitle").innerHTML = "Crear cuenta de usuario";
	document.getElementById("grabar").disabled = false;
	setFocusOnForm("form2");
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
	$('#pagina').val(pagina);
	limpiarTiempo();
	tiempo(pagina);
}
function tiempo(pagina) {
	timeoutID = setTimeout(function() {
		ajaxBusquedaPagina(pagina, null)
	}, 1000);
}

function ajaxBusquedaPagina(pagina, campo_busqueda) {
	capaProtectora();
	// overlay = new $.ui.dialog.overlay($("#cargando"));
	tabla = document.getElementById('tabla_excel');
	tamano = tabla.rows.length;
	if (tamano > 4) {
		for ( var int = 4; int < tamano; tamano--) {
			tabla.deleteRow((tamano - 1));
		}
	}

	var res = ajaxCall(httpMethod = "GET",
			uri = "${def:actionroot}/search?pagina=" + pagina
					+ "&campo_busqueda=" + campo_busqueda, divResponse = null,
			divProgress = "status2", formName = null,
			afterResponseFn = inizializaTabla, onErrorFn = null);
	// $('#tabla_excel tr:last').after(text);
}

function cargaEmpresa(){
	ajaxCall(httpMethod = "GET",
			uri = "${def:actionroot}/search?pagina=" + pagina
					+ "&campo_busqueda=" + campo_busqueda, divResponse = response,
			divProgress = "status2", formName = null,
			afterResponseFn = inizializaTabla, onErrorFn = null);
}

function limpiarTiempo() {
	window.clearTimeout(timeoutID);
}

function buscar(elemento) {
	limpiarTiempo();
	timeoutID = setTimeout(function() {
		if (elemento.value != "") {
			ajaxBusquedaPagina(1, elemento.value);
		} else {
			ajaxBusquedaPagina(1, null);
		}
	}, 1000);

}














// funciones del datail
//inicializa cuando se carga la pagina
//onload = function()
//{
//	addNew(); //prepara formulario para ingresar nuevo registro 
//	search(); //mostrar registros en vista paginada
//	document.form2.fnacimiento.onkeypress = keypressDate;
//} 

/** picklist de seleccion simple **/

//desplegar picklist de seleccion simple
function pickCiudadId(){
	var url = "${def:context}${def:actionroot}/picklist/ciudad/form";
	pickOpen ('ciudad_id_name','ciudad_id',url,'450','235');	
}

//ejecutar consulta y mostrar grid, si hay data viewPage() sera invocada automaticamente
function search()
{
//	var url = "${def:actionroot}/search";
//	
//	//determina si refresca y se para en la pagina actual o en la pagina 1
//	if (currentPage > 0)
//		url = "${def:actionroot}/search?currentpage=" + currentPage;
//
//	//llamada Ajax...
//	ajaxCall(httpMethod="GET", 
//					uri= url, 
//					divResponse="response", 
//					divProgress="status", 
//					formName=null, 
//					afterResponseFn=null, 
//					onErrorFn=null);
}

//traer la pagina N del grid
function viewPage()
{
	var url = "${def:actionroot}/view";
	gotoPage(url);
}	

//grabar el formulario
function save()
{
	//determina si es un insert o un update
	if (document.form2.id.value=="")
		return insert();
	else
		return insert();
//		return update();
}

//grabar registro nuevo en BD
function insert()
{
		clearErrorMessages();
		document.getElementById("grabar").disabled=true;
		console.log(document.getElementsByName("form_detail"));
		//llamada Ajax...
		return ajaxCall(httpMethod="POST", 
						uri="${def:actionroot}/insert", 
						divResponse=null, 
						divProgress="status", 
						formName="form_detail", 
						afterResponseFn=null, 
						onErrorFn=retryAddnewOrEdit);	    	
}

//restaurar el formulario en caso de error
function retryAddnewOrEdit() {
	document.getElementById("grabar").disabled=false;
	setFocusOnForm("form2");		
}

//editar registro en formulario
function edit(id)
{
		clearErrorMessages();

		//llamada Ajax...
		ajaxCall(httpMethod="GET", 
						uri="${def:actionroot}/edit?id=" + id , 
						divResponse=null, 
						divProgress="status", 
						formName=null, 
						afterResponseFn=null, 
						onErrorFn=null);	
}

//actualizar registro en BD
function update()
{		
		clearErrorMessages();
		document.getElementById("grabar").disabled=true;
		
		//llamada Ajax...
		return ajaxCall(httpMethod="POST", 
						uri="${def:actionroot}/update", 
						divResponse=null, 
						divProgress="status", 
						formName="form2", 
						afterResponseFn=null, 
						onErrorFn=retryAddnewOrEdit);	
}

//eliminar un registro
function deleteRecord(id)
{
	if (window.confirm("�Est� seguro que desea ELIMINAR este registro?")==false)
	{
		return false;
	}
	
		//llamada Ajax...
		ajaxCall(httpMethod="GET", 
						uri="${def:actionroot}/delete?id=" + id,
						divResponse=null, 
						divProgress="status", 
						formName=null, 
						afterResponseFn=null, 
						onErrorFn=null);	
					
}

//configurar formulario para ingresar registro nuevo
function addNew()
{

	ajaxCall(httpMethod="GET", 
					uri="${def:actionroot}/addnew",
					divResponse=null, 
					divProgress="status", 
					formName=null, 
					afterResponseFn=null, 
					onErrorFn=null);
							
//	clearForm("form2");
	clearForm("form2");
//	document.getElementById("formTitle").innerHTML = "A�adir registro";
	document.getElementById("saveDetail").value = "A�adir";
	document.getElementById("grabar").disabled=false;
	document.getElementById("saveDetail").disabled=false;
	document.getElementById("detail").innerHTML = "";
	setFocusOnForm("form2");
}

//invoca la generacion de un PDF en un popup
function openPDF()
{
	var features = "height=500,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	var url = "${def:context}${def:actionroot}/pdf?random=" + Math.random();
	var w = window.open(url, null, features);
}

//invoca la generacion de un Excel en un popup
function openExcel()
{
	var features = "height=500,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	var url = "${def:context}${def:actionroot}/excel?random=" + Math.random();
	var w = window.open(url, null, features);
}

//grabar registro nuevo del detalle en BD o actualizarlo en memoria
function detailInsert()
{
		clearErrorMessages();
		document.getElementById("saveDetail").disabled=true;

		//registro nuevo o un update?
		if (document.form2.rowindex.value=="") {
			return ajaxCall(httpMethod="POST", 
							uri="${def:actionroot}/detail/insert", 
							divResponse="detail", 
							divProgress="status", 
							formName="form2", 
							afterResponseFn=detailAddNew, 
							onErrorFn=detailReady);
		} else {
			return ajaxCall(httpMethod="POST", 
							uri="${def:actionroot}/detail/update", 
							divResponse="detail", 
							divProgress="status", 
							formName="form2", 
							afterResponseFn=detailAddNew, 
							onErrorFn=detailReady);
		
		}
			    	
}	

//en caso de error colocar focus y mostrar boton 
function detailReady() {
	document.getElementById("saveDetail").disabled=false;
	setFocusOnForm("form2");
}

//prepara el formulario de detalle para ingreso de datos
function detailAddNew() {
	document.getElementById("saveDetail").disabled=false;
	clearForm("form2");
	setFocusOnForm("form2");
	document.getElementById("saveDetail").value = "A�adir";
}

//editar registro en formulario
function detailEdit(id)
{
		document.getElementById("saveDetail").value = "Modificar";
		clearErrorMessages();

		//llamada Ajax...
		ajaxCall(httpMethod="GET", 
						uri="${def:actionroot}/detail/edit?id=" + id , 
						divResponse=null, 
						divProgress="status", 
						formName=null, 
						afterResponseFn=null, 
						onErrorFn=null);	
}		

//eliminar un registro del detalle
function detailDelete(id)
{
	if (window.confirm("�Est� seguro que desea ELIMINAR este registro?")==false)
	{
		return false;
	}
	
		//llamada Ajax...
		ajaxCall(httpMethod="GET", 
						uri="${def:actionroot}/detail/delete?id=" + id,
						divResponse="detail", 
						divProgress="status", 
						formName="form2", 
						afterResponseFn=detailAddNew, 
						onErrorFn=detailReady);	
					
}

//mostrar el grid del detalle
function detailView() {
		ajaxCall(httpMethod="GET", 
						uri="${def:actionroot}/detail/view",
						divResponse="detail", 
						divProgress="status", 
						formName=null, 
						afterResponseFn=null, 
						onErrorFn=null);	
}
function pickEmpresa(){
	var url = "${def:context}/action/recalcula_promedio/picklist-empresa/form";
	pickOpen('nombre_empresa', 'empresa_id', url, '450', '300');
}


