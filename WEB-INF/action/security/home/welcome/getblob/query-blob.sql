SELECT
	image_data,content_type, empresa_id, nombre_empresa
FROM
	empresa
WHERE 
	empresa_id = ${fld:id}

-- select image_data,content_type from empresa where empresa_id = ${fld:id}