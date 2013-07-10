select	
	nombre as nombre_familiar,
	filiacion,
	fnacimiento,
	telefono as telefono_familiar
from
	demo.familiar 
where
	afiliado_id=${fld:id} 
