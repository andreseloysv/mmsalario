select	
	public.empresa.*
from
	public.empresa 
where
	id_empresa is not null
	${filter}