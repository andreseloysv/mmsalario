UPDATE public.imagen SET
	description=${fld:description},
	id_empresa=${fld:id_empresa},
	image_size=${fld:_filesize},
	image_data=?,
	filename=${fld:_filename},
	content_type=${fld:_contenttype}

WHERE
	id_imagen=${fld:id}

