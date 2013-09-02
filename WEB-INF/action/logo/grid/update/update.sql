UPDATE public.imagen SET
	description=${fld:description},
	id_empresa=${fld:id_empresa}

WHERE
	id_imagen=${fld:id}

