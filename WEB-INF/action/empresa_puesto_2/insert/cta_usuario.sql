insert into cta_usuario
(
	id_usuario,
	id_persona
)
values
(
	${seq:currval@${schema}seq_user},
	${fld:empleado}
);