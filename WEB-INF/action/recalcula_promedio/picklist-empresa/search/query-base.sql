	select	
	distinct  e.nombre_empresa,p.fecha_datos, p.procedencia_datos_id
from
	public.empresa as e , empresa_puesto as ep, procedencia_datos as p
where
	empresa_id is not null and e.empresa_id=ep.fk_empresa_id
	and
	empresa_id is not null and p.empresa_fk=ep.fk_empresa_id
	${filter}