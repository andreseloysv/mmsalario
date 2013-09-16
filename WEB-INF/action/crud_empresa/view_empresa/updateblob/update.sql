UPDATE encuesta SET
	descripcion_encuesta=${fld:descripcion_encuesta},
	encuesta_id=${fld:id},
	image_size=${fld:_filesize},
	image_data=?,
	filename=${fld:_filename},
	content_type=${fld:_contenttype},
	nombre_encuesta=${fld:nombre_encuesta}
WHERE
	encuesta_id=${fld:id}