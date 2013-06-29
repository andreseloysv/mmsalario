insert into cta_usuario
(
	id_usuario,
	id_persona
)
values
(
	CURRVAL('security.seq_user'),
	${fld:empleado}
);