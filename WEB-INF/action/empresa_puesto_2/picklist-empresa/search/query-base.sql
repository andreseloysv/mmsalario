SELECT	
		empresa_id ,
  		codigo_empresa ,
	 	nombre_empresa ,
	  	rif ,
	  	fecha_registro ,
	  	telefono ,
	  	direccion ,
	  	tamano_fk ,
	  	fk_origen_capital_id ,
	  	vent_ejercicio_actual ,
	  	vent_ejercicio_proximo ,
	  	vent_moneda_funcional ,
	  	vent_fecha_ultimo_cierre_fiscal ,
	  	vent_posee_manufactura ,
	  	pais_origen ,
	  	porcentaje_ventas ,
	  	muestra ,
	  	sector_fk 
  
FROM
	public.empresa
	 
WHERE
	empresa_id is not null and nombre_empresa!=''

ORDER BY nombre_empresa ASC;
	
	${filter}