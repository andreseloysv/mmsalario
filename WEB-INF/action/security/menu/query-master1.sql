select distinct
	m.title,
	m.menu_id,
	m.position       
from
	${schema}s_menu as m,
	${schema}s_menu_role as mr,
	${schema}s_user as u,
	${schema}s_user_role as ur,
	${schema}s_application as a,
	${schema}s_role as r

where
	--a.app_alias = '${req:dinamica.security.application}'
	a.app_alias = 'mmsalario'
and
	--u.userlogin = '${def:user}'
	u.userlogin = 'admin'
and
	r.app_id = a.app_id
and
	ur.role_id = r.role_id
and
	ur.user_id = u.user_id
and
	mr.role_id = ur.role_id
and
	m.menu_id = mr.menu_id
and
	m.master_menu_id is null
order by 
	m.position
