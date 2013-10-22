	SELECT 	 e.nombre_empresa , MAX(p.fecha_datos) as fecha_datos
from
	empresa as e , empresa_puesto as ep, procedencia_datos as p
where
	empresa_id is not null and e.empresa_id=ep.fk_empresa_id
	and
	empresa_id is not null and p.empresa_fk=ep.fk_empresa_id
	GROUP BY (e.nombre_empresa)