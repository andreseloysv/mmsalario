UPDATE demo.afiliado SET
	nombre=${fld:nombre},
	cedula=${fld:cedula},
	telefono=${fld:telefono},
	email=${fld:email},
	sexo=${fld:sexo},
	ciudad_id=${fld:ciudad_id}
WHERE
	afiliado_id=${fld:id}
