insert into security.s_passlog  
(
	passlog_id,
	last_change,
	hash,
	user_id
)
values 
(
	NEXTVAL('security.seq_passlog'),
	current_date,
	${fld:passwd},
	CURRVAL('security.seq_user')
)