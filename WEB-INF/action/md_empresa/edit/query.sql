select	
	demo.ciudad.ciudad_id, 
	demo.ciudad.ciudad as ciudad_pl0,
	demo.afiliado.*
from
	demo.ciudad,
	demo.afiliado 
where
	demo.ciudad.ciudad_id=demo.afiliado.ciudad_id and
	afiliado_id = ${fld:id}


