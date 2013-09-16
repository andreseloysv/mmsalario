UPDATE empresa SET
	descripcion_empresa=${fld:descripcion_empresa},
	nombre_empresa=${fld:nombre_empresa}
WHERE
	empresa_id=${fld:id}