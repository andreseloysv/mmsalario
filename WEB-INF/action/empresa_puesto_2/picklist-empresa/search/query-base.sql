select	
	public.empresa.*
from
	public.empresa 
where
	empresa_id is not null
	${filter}