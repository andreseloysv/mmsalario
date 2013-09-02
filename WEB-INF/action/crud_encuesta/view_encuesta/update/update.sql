UPDATE encuesta SET
	descripcion_encuesta=${fld:descripcion_encuesta},
	nombre_encuesta=${fld:nombre_encuesta}
WHERE
	encuesta_id=${fld:id}