select
	content_type as content_type, 
	filename as filename
from public.imagen
where id_imagen = ${fld:id}
