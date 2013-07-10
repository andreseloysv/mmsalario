select 
	*
from 
	demo.ciudad
where
	upper (ciudad) like upper (${fld:name})
order by
	ciudad
