var timeoutID = 0;
var nombre_empresa="";
var nombre_tamano="";
var nombre_sector="";
var rango_fecha_ini_registro="";
var rango_fecha_fin_registro="";
var input_seleccionado="";
var text_area_seleccionado="";
var td_seleccionado=new Array();
var color_input_seleccionado="";
var fila_seleccionada=null;

var someElement = $("#cargando"), overlay;

// inicializa cuando se carga la pagina
onload = function() {
	
	 
		    $( "button" )
		      .button()
		      .click(function( event ) {
		    	  upload_empresa();
		      });
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
		//llamada Ajax...
		return ajaxCall(	httpMethod="POST", 
							uri="${def:actionroot}/search", 
							divResponse="response", 
							divProgress="status", 
							formName=null, 
							afterResponseFn="inicializaBotones", 
							onErrorFn="showFilter");
	
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
// return update();
}

function inicializaGrabar(){
	document.getElementById("grabar").disabled=false;
	alert("hey you see");
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
		document.getElementById("grabar").disabled=true;
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
		
		$("#form2").find("#nombre_muestra_form").val($("#nombre_empresa").val());
		
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
	if($("#nombre_empresa").val()==""){
		$('<div>').attr({
		    id: 'nombre_muestra_form_error',
		    'class': 'errormsg'
		}).appendTo($("#nombre_empresa").parent());
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
			    value: $("#nombre_empresa").val()
			}).appendTo(value);
			}else{
				$(value).find("#nombre_muestra_form").val($("#nombre_empresa").val());
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
	ajaxCall(httpMethod="POST", 
			uri="/action/buscar_empresa/form",
			divResponse="empresa_muestra_resultado", 
			divProgress="status", 
			formName="form_filtro_busca_empresa", 
			afterResponseFn=null, 
			onErrorFn=null);	
}

      	
$( document ).tooltip({
    position: {
      my: "center bottom-20",
      at: "center top",
      using: function( position, feedback ) {
        $( this ).css( position );
        $( "<div>" )
          .addClass( "arrow" )
          .addClass( feedback.vertical )
          .addClass( feedback.horizontal )
          .appendTo( this );
      }
    }
  });










	
//enviar POST del formulario
function upload()
{
	
	if (document.form1.description.value=="") {
		alert("Por favor ingrese la descripción del documento.");
		document.form1.description.focus();
		return false;
	}
	
	if (document.form1.file.value=="") {
		alert("Por favor indique el archivo a ser cargado.");
		document.form1.file.focus();
		return false;
	}


	document.form1.submit.disabled = true;
	document.getElementById("status").style.display="";
	return true;		
}
			
//funcion de callback del iframe cuando se carga bien el documento
function uploadOK()
{
	document.getElementById("status").style.display="none";	
	document.form1.description.value="";
	document.form1.file.value="";
	document.form1.submit.disabled = false;
	alert("El documento se cargó exitosamente.");
	search();
	document.form1.description.focus();
}

//funcion de callback del iframe cuando no carga correctamente el documento
function uploadError(msg)
{
	document.getElementById("status").style.display="none";	
	document.form1.submit.disabled = false;
	alert(msg);
	document.form1.description.focus();
}



//define la url en donde se mostrara el resultado de consulta
function viewPage()
{
	var url = "${def:actionroot}/view";
	//llama a la funcion para ir a la pagina indicada 
	gotoPage(url);
}	
	
//invoca un popup para mostrar los documentos
function openBlob(id,url)
{
	var features = "height=500,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	var w = window.open(url, id, features);
}

//invoca un popup para descargar los documentos
function downloadBlob(id,url)
{
	var features = "height=500,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	var w = window.open(url, id, features);
}

//eliminar un documento
function onDelete(url)
{
	if (window.confirm("¿Está seguro que desea ELIMINAR este documento?")==false)
	{
		return false;
	}
	
	window.location= url;
}

//invoca la generacion de un PDF en un popup
function openPDF()
{
	var features = "height=500,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	var url = "${def:context}${def:actionroot}/pdf?random=" + Math.random();
	var w = window.open(url, null, features);
}


function editar(fila){
	$("#edita_empresa").remove();
	$("#guardar").css("display","block");

	if(input_seleccionado.length>0){
		if(color_input_seleccionado=="rgb(255, 255, 255)"){
			input_seleccionado.css( "background-color", "rgb(255, 255, 255)");
			text_area_seleccionado.css( "background-color", "rgb(255, 255, 255)");
		}
		else{
			input_seleccionado.css( "background-color", "rgb(247, 247, 247)");
			text_area_seleccionado.css( "background-color", "rgb(247, 247, 247)");
		}
		input_seleccionado.attr("readonly",true);
		text_area_seleccionado.attr("readonly",true);
	}
	
	$(fila).attr("title","");
	
	$("#file").remove();
	$("#fileinputs").remove();
	
	
	
	var currentPosition = $(fila).offset();
	currentPosition.left=(parseInt(currentPosition.left))+700;
	currentPosition.top=(parseInt(currentPosition.top))+60;
	$("#guardar").css({ top: currentPosition.top+"px", left: (currentPosition.left+"px")})
	
	
	var campos = "<form name='edita_empresa' id='edita_empresa' enctype='multipart/form-data' method='POST' action='http://localhost/mmsalario/action/crud_empresa/view_empresa/upload' target='uploadFrame'  style='display: none;' >"; 
	var contador=1;
	var nombre="";
	var descripcion="";
	fila_seleccionada=fila;
	
	$(fila).find("td").each(function( index,value  ) {
		
		if(contador==1){
			$(this).append('<div class="fileinputs" title="Haz click aqui para cambiar la foto" id="fileinputs"><input style="width: 185px;" type="file" id="file_'+fila.id+'" name="file_'+fila.id+'" class="file" onchange="guardaFoto()" /><div class="fakefile"><input /><img class="button" src="/mmsalario/images/search.gif" /></div></div>');
		}
		
		
		
		if(contador==2){
			var str=($(this).find("input")[0]).id;
			var n=str.split("_");
			var mi_input=$('#input_'+n[1]);
			color_input_seleccionado=mi_input.css('background-color');
			mi_input.attr("readonly", false);
			mi_input.css( "background-color", "rgb(255, 246, 210)" );
			nombre=mi_input.val();
			input_seleccionado=mi_input;
		}
		
		if(contador==3){
			var str=($(this).find("textarea")[0]).id;
			var n=str.split("_");
			var mi_text_area=$('#textarea_'+n[1]);
			mi_text_area.attr('readonly', false);
			mi_text_area.css( "background-color", "rgb(255, 246, 210)" );
			descripcion=mi_text_area.val();
			text_area_seleccionado=mi_text_area;
		}
		
		
		  contador++;
	});
	
	campos+='<input type="hidden" id="nombre_empresa" name="nombre_empresa" value="'+nombre+'" >';
	campos+='<input type="hidden" id="descripcion_empresa" name="descripcion_empresa" value="'+descripcion+'" >';
	campos+='<input type="hidden" name="id" value="'+fila.id+'">';
	
	campos+='<input type="hidden" name="_tempfile" id="_tempfile" value="">';
	campos+='<input type="hidden" name="_filename" id="_filename" value="">';
	campos+='<input type="hidden" name="_contenttype" id="_contenttype" value="">';
	campos+='<input type="hidden" name="_filesize" id="_filesize" value="">';
	
	campos+="</form>";
	$(fila).append(campos);
}




function upload_empresa(){
	document.edita_empresa._tempfile.value="";
	$("#nombre_empresa").val($("#input_"+fila_seleccionada.id).val());
	$("#descripcion_empresa").val($("#textarea_"+fila_seleccionada.id).val());
	save();

}
function guardaFoto(){
	 $("#file_"+fila_seleccionada.id).appendTo(document.getElementById("edita_empresa"));
	 $("#file_"+fila_seleccionada.id).attr("name","file");
	 $("#file_"+fila_seleccionada.id).attr("id","file");
	 $("#nombre_empresa").val($("#input_"+fila_seleccionada.id).val());
	 $("#descripcion_empresa").val($("#textarea_"+fila_seleccionada.id).val());
	 
	 document.getElementById("edita_empresa").submit();
	
}



function eliminaForm(){
	$("#file").remove();
	$("#fileinputs").remove();
	$("#edita_empresa").remove();
	
}

function retryAddnewOrEdit() {
    alert("Error");
}


//enviar POST del formulario
function upload()
{
	
	//determinar si es un insert
	if (document.edita_empresa.id.value=="") {
		
		if (document.edita_empresa.file.value=="") {
			alert("Por favor indique el archivo a ser cargado.");
			document.edita_empresa.file.focus();
			return false;
		}
		if (document.edita_empresa._tempfile.value=="") {
			document.edita_empresa.submit.disabled = true;
			document.getElementById("status").style.display="";
			return true;
		} else {
			save();
			return false;
		}		
	} else {
			
		//es un update, determinar si quieren actualizar tambien el blob
		if (document.edita_empresa.file.value=="") {
			save();
			return false;
		} else {
			document.edita_empresa.submit.disabled = true;
			return true;
		}
	}

}

//pone los valores del archivo cargado 
//en los controles del formulario, es llamada por el iframe escondido
function setFileInfo(tempFile, fileName, fileSize, contentType) {
	document.edita_empresa._tempfile.value = tempFile;
	document.edita_empresa._filename.value = fileName;
	document.edita_empresa._filesize.value = fileSize;
	document.edita_empresa._contenttype.value = contentType;
}

//grabar el formulario
function save()
{
	
	var uri = "${def:actionroot}/insert"
	if (document.edita_empresa.id.value!="") {
		if (document.edita_empresa._tempfile.value==""){
				uri = "${def:actionroot}/update"
			}
			
		else{
			uri = "${def:actionroot}/updateblob"
		}
			
	}
	ajaxCall(httpMethod="POST", 
					uri, 
					divResponse=null, 
					divProgress="status", 
					formName="edita_empresa", 
					afterResponseFn=null, 
					onErrorFn=retryAddnewOrEdit);
	
	
}

//restaurar el formulario en caso de error
function retryAddnewOrEdit() {
	document.edita_empresa.submit.disabled=false;
	setFocusOnForm("edita_empresa");		
}

