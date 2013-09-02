insert into encuesta
(
	encuesta_id,
	filename,
	content_type,
	image_size,
	image_data
)
values
(
	${seq:nextval@public.encuesta_encuesta_id_seq},
	${fld:file.filename},
	${fld:file.content-type},
	${fld:image_size},
	?
)
