var timeoutID = 0;
var nombre_empresa="";
var nombre_tamano="";
var nombre_sector="";
var rango_fecha_ini_registro="";
var rango_fecha_fin_registro="";

var someElement = $("#cargando"), overlay;

// inicializa cuando se carga la pagina
onload = function() {
	addNew(); // prepara formulario para ingresar nuevo registro
	search(); // mostrar registros en vista paginada
	
	$("#nombre_empresa").focusin(function () {
		if(nombre_empresa!=$(this).val()){
			var str=$("#nombre_empresa").val();
			var n=str.split(" - ");
			$("#nombre_empresa").val(n[0]);
			$("#fecha_ini_registro").val(n[1]);
			
			$("#tabla_nombre_muestra").css( "display", "block" );
		}
		});
	$("#nombre_tamano").focusin(function () {
		if(nombre_tamano!=$(this).val()){
			nombre_tamano=$(this).val();
			buscar_empresas();
			$("#tabla_nombre_muestra").css( "display", "block" );
		}
		});
	$("#nombre_sector").focusin(function () {
		if(nombre_sector!=$(this).val()){
			nombre_sector=$(this).val();
			buscar_empresas();
			$("#tabla_nombre_muestra").css( "display", "block" );
		}
		});

	
	$( "#rango_fecha_ini_registro" ).datepicker({
		onClose: function( selectedDate ) {
	        $( "#rango_fecha_fin_registro" ).datepicker( "option", "minDate", selectedDate );
	        buscar_empresas();
	        $("#tabla_nombre_muestra").css( "display", "block" );
	      }
	});
	$( "#rango_fecha_fin_registro" ).datepicker({
		onClose: function( selectedDate ) {
	        $( "#rango_fecha_ini_registro" ).datepicker( "option", "maxDate", selectedDate );
	        buscar_empresas();
	        $("#tabla_nombre_muestra").css( "display", "block" );
	      }
	});
	$('#profile-image').on('click', function() {
        $('#profile-image-upload').click();
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

	$("#cargando").css({
		'display' : 'none'
	});


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
















/** picklist de seleccion simple * */

// desplegar picklist de seleccion simple
function pickCiudadId(){
	var url = "${def:context}${def:actionroot}/picklist/ciudad/form";
	pickOpen ('ciudad_id_name','ciudad_id',url,'450','235');	
}

// ejecutar consulta y mostrar grid, si hay data viewPage() sera invocada
// automaticamente
function search()
{

}

// traer la pagina N del grid
function viewPage()
{
	var url = "${def:actionroot}/view";
	gotoPage(url);
}	

// grabar el formulario
function save()
{
	if(!validaNombreMuestra()){
		return 0;
	}
	
	$("#nombre_muestra_form_error").remove();
	
	$.each( $("#nombre_muestra_form"), function( key, value ) {
		$(value).attr('value', $("#nombre_muestra").val());
	});
	
	// determina si es un insert o un update
	if (document.form2.id.value=="")
		return insert();
	else
		return insert();

}

function inicializaGrabar(){
	alert("afterFunction");
}

// grabar registro nuevo en BD
function insert()
{
		clearErrorMessages();
		if($("#form_detail input").length==1){
			alert("Debe agregar al menos una empresa a la muestra.");
			return 0;
		}
		inicializaNombreMuestra();

		var mi_form=document.getElementsByName("form_detail");
		
		$('<input>').attr({
		    type: 'hidden',
		    id: 'nombre_muestra_form',
		    name: 'nombre_muestra_form',
		    value: $("#nombre_muestra").val()
		}).appendTo(mi_form);
		

		
		// llamada Ajax...
		return ajaxCall(httpMethod="POST", 
						uri="${def:actionroot}/insert", 
						divResponse=null, 
						divProgress="status", 
						formName="form_detail", 
						afterResponseFn=inicializaGrabar, 
						onErrorFn=retryAddnewOrEdit);	    	
}

// restaurar el formulario en caso de error
function retryAddnewOrEdit() {
	document.getElementById("grabar").disabled=false;
	setFocusOnForm("form2");		
}

// editar registro en formulario
function edit(id)
{
		clearErrorMessages();

		// llamada Ajax...
		ajaxCall(httpMethod="GET", 
						uri="${def:actionroot}/edit?id=" + id , 
						divResponse=null, 
						divProgress="status", 
						formName=null, 
						afterResponseFn=null, 
						onErrorFn=null);	
}

// actualizar registro en BD
function update()
{		
		clearErrorMessages();
		document.getElementById("grabar").disabled=true;
		
		// llamada Ajax...
		return ajaxCall(httpMethod="POST", 
						uri="${def:actionroot}/update", 
						divResponse=null, 
						divProgress="status", 
						formName="form2", 
						afterResponseFn=null, 
						onErrorFn=retryAddnewOrEdit);	
}

// eliminar un registro
function deleteRecord(id)
{
	if (window.confirm("¿Está seguro que desea ELIMINAR este registro?")==false)
	{
		return false;
	}
	
		// llamada Ajax...
		ajaxCall(httpMethod="GET", 
						uri="${def:actionroot}/delete?id=" + id,
						divResponse=null, 
						divProgress="status", 
						formName=null, 
						afterResponseFn=null, 
						onErrorFn=null);	
					
}

// configurar formulario para ingresar registro nuevo
function addNew()
{

	ajaxCall(httpMethod="GET", 
					uri="${def:actionroot}/addnew",
					divResponse=null, 
					divProgress="status", 
					formName=null, 
					afterResponseFn=null, 
					onErrorFn=null);
							
// clearForm("form2");
	clearForm("form2");
// document.getElementById("formTitle").innerHTML = "Añadir registro";
	document.getElementById("saveDetail").value = "Añadir";
	document.getElementById("grabar").disabled=false;
	document.getElementById("saveDetail").disabled=false;
	document.getElementById("detail").innerHTML = "";
	setFocusOnForm("form2");
}

// invoca la generacion de un PDF en un popup
function openPDF()
{
	var features = "height=500,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	var url = "${def:context}${def:actionroot}/pdf?random=" + Math.random();
	var w = window.open(url, null, features);
}

// invoca la generacion de un Excel en un popup
function openExcel()
{
	var features = "height=500,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	var url = "${def:context}${def:actionroot}/excel?random=" + Math.random();
	var w = window.open(url, null, features);
}

// grabar registro nuevo del detalle en BD o actualizarlo en memoria
function detailInsert()
{
	if(validaNombreMuestra()){
		clearErrorMessages();
		document.getElementById("saveDetail").disabled=true;
		
		$("#form2").find("#nombre_muestra_form").val($("#nombre_encuesta").val());
		
		return ajaxCall(httpMethod="POST", 
				uri="${def:actionroot}/detail/insert", 
				divResponse="detail", 
				divProgress="status", 
				formName="form2", 
				afterResponseFn=detailAddNew, 
				onErrorFn=detailReady);
	}
}	


// grabar registro nuevo del detalle en BD o actualizarlo en memoria
function listDetailInsert(form_name,form_objeto)
{
		clearErrorMessages();
		document.getElementById("saveDetail").disabled=true;

		// registro nuevo o un update?
		
			return ajaxCall(httpMethod="POST", 
							uri="${def:actionroot}/detail/insert", 
							divResponse="detail", 
							divProgress="status", 
							formName=form_name, 
							afterResponseFn=detailAddNew, 
							onErrorFn=detailReady);
}	
function validaNombreMuestra(){
	if($("#nombre_encuesta").val()==""){
		$('<div>').attr({
		    id: 'nombre_muestra_form_error',
		    'class': 'errormsg'
		}).appendTo($("#nombre_encuesta").parent());
		$("#nombre_muestra_form_error").text("Este valor DEBE ser ingresado.");
		return false;
	}
	return true;
}
function llenaListDestailInsert(){
	if(!validaNombreMuestra()){
		return 0;
	}
	
	$.each( $("#empresa_muestra_resultado form"), function( key, value ) {
		if($(value).find("input:checked").length>0){
			if($(value).find("#nombre_muestra_form").length<=0){
			$('<input>').attr({
			    type: 'hidden',
			    id: 'nombre_muestra_form',
			    name: 'nombre_muestra_form',
			    value: $("#nombre_encuesta").val()
			}).appendTo(value);
			}else{
				$(value).find("#nombre_muestra_form").val($("#nombre_encuesta").val());
			}
			listDetailInsert($(value).attr('name'),value);
		}
});

}
// en caso de error colocar focus y mostrar boton
function detailReady() {
	document.getElementById("saveDetail").disabled=false;
	setFocusOnForm("form2");
}

// prepara el formulario de detalle para ingreso de datos
function detailAddNew() {
	$("#empresa_muestra_resultado table").html('');
	
	document.getElementById("saveDetail").disabled=false;
	clearForm("form2");
	setFocusOnForm("form2");
	document.getElementById("saveDetail").value = "Añadir";
}

// editar registro en formulario
function detailEdit(id)
{
		document.getElementById("saveDetail").value = "Modificar";
		clearErrorMessages();

		// llamada Ajax...
		ajaxCall(httpMethod="GET", 
						uri="${def:actionroot}/detail/edit?id=" + id , 
						divResponse=null, 
						divProgress="status", 
						formName=null, 
						afterResponseFn=null, 
						onErrorFn=null);	
}		

// eliminar un registro del detalle
function detailDelete(id)
{
	if (window.confirm("¿Está seguro que desea ELIMINAR este registro?")==false)
	{
		return false;
	}
	
		// llamada Ajax...
		ajaxCall(httpMethod="GET", 
						uri="${def:actionroot}/detail/delete?id=" + id,
						divResponse="detail", 
						divProgress="status", 
						formName="form2", 
						afterResponseFn=detailAddNew, 
						onErrorFn=detailReady);	
					
}

// mostrar el grid del detalle
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

function pickTamano() {
	var url = "${def:context}/action/muestra/picklist-tamano/form";
	pickOpen('nombre_tamano', 'tamano_id', url, '450', '300');
}

function pickSector() {
	var url = "${def:context}/action/muestra/picklist-sector/form";
	pickOpen('nombre_sector', 'sector_id', url, '450', '300');
}

function buscar_empresas(){
	// alert("/action/buscar_empresa/form?nombre_tamano="+$("#nombre_tamano").val()+"&nombre_sector="+$("#nombre_sector").val());
	ajaxCall(httpMethod="POST", 
// uri="/action/buscar_empresa/form?tamano="+$("#nombre_tamano").val()+"&sector="+$("#nombre_sector").val(),
			uri="/action/buscar_empresa/form",
			divResponse="empresa_muestra_resultado", 
			divProgress="status", 
			formName="form_filtro_busca_empresa", 
			afterResponseFn=null, 
			onErrorFn=null);	
}
function inicializaNombreMuestra(){
//	var elemento=$("#tabla_nombre_muestra");
//	elemento.css( "display", "none" );
//	elemento.find("input")[0].value="";
}





















function upload_encuesta(){
	document.getElementById("form1").submit();
//	save();

}
function guardaFoto(){
	 $("#file_"+fila_seleccionada.id).appendTo(document.getElementById("edita_encuesta"));
	 $("#file_"+fila_seleccionada.id).attr("name","file");
	 $("#file_"+fila_seleccionada.id).attr("id","file");
	 $("#nombre_encuesta").val($("#input_"+fila_seleccionada.id).val());
	 $("#descripcion_encuesta").val($("#textarea_"+fila_seleccionada.id).val());
	 document.getElementById("form1").submit();
}



function eliminaForm(){
	$("#file").remove();
	$("#fileinputs").remove();
	$("#edita_encuesta").remove();
}


//enviar POST del formulario
function upload()
{
	
	//determinar si es un insert
	if (document.form1.id.value=="") {
		
		if (document.form1.file.value=="") {
			alert("Por favor indique el archivo a ser cargado.");
			document.form1.file.focus();
			return false;
		}
		if (document.form1._tempfile.value=="") {
			document.form1.submit.disabled = true;
			document.getElementById("status").style.display="";
			return true;
		} else {
			save_foto();
			return false;
		}		
	} else {
			
		//es un update, determinar si quieren actualizar tambien el blob
		if (document.form1.file.value=="") {
			save_foto();
			return false;
		} else {
			document.form1.submit.disabled = true;
			return true;
		}
	}

}

//pone los valores del archivo cargado 
//en los controles del formulario, es llamada por el iframe escondido
function setFileInfo(tempFile, fileName, fileSize, contentType) {
 
	document.form1._tempfile.value = tempFile;
	document.form1._filename.value = fileName;
	document.form1._filesize.value = fileSize;
	document.form1._contenttype.value = contentType;
}

//grabar el formulario
function save_foto()
{
	
	var uri = "${def:actionroot}/create_blob";
	ajaxCall(httpMethod="POST", 
					uri, 
					divResponse="profile-image", 
					divProgress="status", 
					formName="form1", 
					afterResponseFn=null, 
					onErrorFn=null);
}