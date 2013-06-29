select
	content_type as content_type, 
	filename as filename
from public.plantilla_excel
where id_plantilla_excel = ${fld:id}
