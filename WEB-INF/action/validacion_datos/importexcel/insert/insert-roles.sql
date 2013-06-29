insert into security.s_user_role
(
	user_role_id,
	user_id,
	role_id
)
values 
(
	NEXTVAL('security.seq_user_role'),
	CURRVAL('security.seq_user'),
	${fld:role_id}
)
