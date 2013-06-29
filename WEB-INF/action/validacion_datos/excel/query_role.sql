select
	r.role_id,
	r.rolename,
	r.description,
	a.app_alias
from
	security.s_role r,
	security.s_application a
where
	r.app_id = a.app_id