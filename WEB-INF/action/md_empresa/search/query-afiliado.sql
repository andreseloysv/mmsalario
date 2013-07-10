select	
	demo.ciudad.ciudad_id, 
	demo.ciudad.ciudad as ciudad_pl0,
	case
		when demo.afiliado.sexo='M' then 'Masculino'
		when demo.afiliado.sexo='F' then 'Femenino'
	end as sexo_combo,
	demo.afiliado.*
from
	demo.ciudad,
	demo.afiliado 
where
	demo.ciudad.ciudad_id=demo.afiliado.ciudad_id 
order by 
	afiliado_id
