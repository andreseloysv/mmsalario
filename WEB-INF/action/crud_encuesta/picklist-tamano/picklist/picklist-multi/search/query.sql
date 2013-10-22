select 
	tamano_id, 
	nombre,
	volumen_ventas_max,
	volumen_ventas_min,
	numero_empleados_max,
	numero_empleados_min
from 
	public.tamano
where
	upper (nombre) like upper (${fld:nombre})
order by
	nombre
