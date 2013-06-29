select	
	public.persona.*
from
	public.persona 
where
	id_u_org = ${fld:unidad}
	${filter}