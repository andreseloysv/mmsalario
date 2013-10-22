select 
	sector_id, 
	nombre_sector
from 
	public.sector
where
	upper (nombre_sector) like upper (${fld:nombre})
order by
	nombre_sector
