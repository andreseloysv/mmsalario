UPDATE empresa SET
	description=${fld:descripcion_empresa},
	image_size=${fld:_filesize},
	image_data=?,
	filename=${fld:_filename},
	content_type=${fld:_contenttype},
	nombre_empresa=${fld:nombre_empresa}
WHERE
	empresa_id=${fld:id}