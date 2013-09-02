var timeoutID = 0;
var someElement = $("#cargando"), overlay;

// inicializa cuando se carga la pagina
onload = function() {
	paginacion(1);
	$('input.celda').autoGrowInput({
		comfortZone : 500,
		minWidth : 100,
		maxWidth : 2000
	});
	$('input.celda').trigger('keyup');
	cargaDialogo();
	$("#tabla_excel").tablesorter({
		 textExtraction: function(node) {
	            return node.childNodes[0].value; 
	        } 
	});
	
	
	
	
	 $( "#porcentaje_desviacion_slider-range-min" ).slider({
	      range: "min",
	      value: 33,
	      min: 0,
	      max: 100,
	      slide: function( event, ui ) {
	        $( "#porcentaje_desviacion" ).val( "%" + ui.value );
	      }
	    });
	    $( "#porcentaje_desviacion" ).val( "%" + $( "#porcentaje_desviacion_slider-range-min" ).slider( "value" ) );
	    
	    
	    
	    
	    
	    $( "#rango_desviacion_slider-range" ).slider({
	        range: true,
	        min: -3,
	        max: 3,
	        values: [ -1, 1 ],
	        slide: function( event, ui ) {
	          $( "#rango_desviacion" ).val( " " + ui.values[ 0 ] + "  a  " + ui.values[ 1 ] );
	        }
	      });
	      $( "#rango_desviacion" ).val( " " + $( "#rango_desviacion_slider-range" ).slider( "values", 0 ) +
	        "  a  " + $( "#rango_desviacion_slider-range" ).slider( "values", 1 ) );
	      
	      
	      
	      

	      var tipo_tolerancia="";
	      
	      var container = $("#overlay_opcion_porcentaje_desviacion");
	      
	      var overlay_opcion_rango_desviacion = $("#overlay_opcion_rango_desviacion");
	      
	      
	      container.click(function() {
	    	  $("tipo_tolerancia").val("porcentaje_desviacion");
	    	  container.css('z-index', '0');
	    	  container.animate({
	    			    opacity: 0.1
	    			  }, 450
	    			  , function() {
	    				  
	    				  overlay_opcion_rango_desviacion.animate({
		  	    			    opacity: 1
			    			  }, 300, function() {
			    				  overlay_opcion_rango_desviacion.css('z-index', '20');
			    			  });
	    			  });
	         
	      });
	      
	      
	      overlay_opcion_rango_desviacion.click(function() {
	    	  $("tipo_tolerancia").val("numero_desviacion");
	    	  overlay_opcion_rango_desviacion.animate({
	    			    opacity: 0.1
	    			  }, 450, function() {
	    				  overlay_opcion_rango_desviacion.css('z-index', '0');
	    				  container.animate({
	  	    			    opacity: 1
		    			  }, 300, function() {
		    				  container.css('z-index', '20');
		    			  });
	    			  });
	         
	      });
	      
	      container.trigger('click');
}



$.tablesorter.addParser({
	id : 'inputs',
	is : function(s) {
		return false;
	},
	format : function(s, table, cell) {
		var $c = $(cell);
		// return 1 for true, 2 for false, so true sorts before false
		if (!$c.hasClass('updateInput')) {
			$c.addClass('updateInput').bind('keyup', function() {
				$(table).trigger('updateCell', [ cell, false ]); // false to
																	// prevent
																	// resort
			});
		}
		return $c.find('input').val();
	},
	type : 'text'
});

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
	$("#tabla_excel").trigger("update"); 
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
							"Aplicar" : function() {
								// AjaxCall
										ajaxCall(httpMethod="POST", 
										uri="${def:actionroot}/insert", 
										divResponse=null, 
										divProgress="status", 
										formName="rango_tolerancia_form", 
										afterResponseFn=inicializaGrabar, 
										onErrorFn=retryAddnewOrEdit);	    
									$(this).dialog("close");
							},
							Cancelar : function() {
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