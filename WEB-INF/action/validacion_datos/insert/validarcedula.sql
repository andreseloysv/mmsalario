select per.cedula 
	from public.persona as per
		where per.cedula = ${fld:cedula}