select
	p.id_persona,
	p.nombre,
	p.apellido,
	p.correo,
	u.nombre as unidad_organizativa,
	e.nombre as empresa
from
	persona p,
	u_org u,
	empresa e
where
	p.id_u_org = u.id_u_org
and
	u.id_empresa = e.id_empresa