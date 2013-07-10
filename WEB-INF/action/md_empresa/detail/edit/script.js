document.form2.nombre_familiar.value = "${fld:nombre_familiar@js}";
setComboValue('filiacion','${fld:filiacion}','form2');
document.form2.fnacimiento.value = "${fld:fnacimiento@dd-MM-yyyy}";
document.form2.telefono_familiar.value = "${fld:telefono_familiar@js}";
document.form2.rowindex.value = "${fld:id}";
document.getElementById("saveDetail").value = "Modificar";
setFocusOnForm("form2");
