insert into empresa
(
	empresa_id,
	filename,
	content_type,
	image_size,
	image_data
)
values
(
	${seq:nextval@public.empresa_empresa_id_seq},
	${fld:file.filename},
	${fld:file.content-type},
	${fld:image_size},
	?
)
