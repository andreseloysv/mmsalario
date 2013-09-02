select 
	nombre
from 
	demo.afiliado  
where 
	nombre = ${fld:nombre}
and 
	afiliado_id <> ${fld:id}
