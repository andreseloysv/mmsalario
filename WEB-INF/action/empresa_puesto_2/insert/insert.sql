insert into ${schema}s_user 
(
	user_id,
	userlogin,
	passwd,
	lname,
	fname,
	email,
	enabled,
	pwd_policy,
	force_newpass,
	locale
)
values 
(
	${seq:nextval@${schema}seq_user},
	${fld:userlogin},
	${fld:passwd},
	(select apellido from persona where id_persona = ${fld:empleado}),
	(select nombre from persona where id_persona = ${fld:empleado}),
	(select correo from persona where id_persona = ${fld:empleado}),
	1,
        -2,
	0,
        'es'
)