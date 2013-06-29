select	
	public.u_org.*
from
	public.u_org 
where
	id_empresa = ${fld:empresa}
	${filter}