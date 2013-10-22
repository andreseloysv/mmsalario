SELECT	
		*
  
FROM
	public.empresa
	 
WHERE
	empresa_id is not null and nombre_empresa!=''

ORDER BY nombre_empresa ASC;
	
	${filter}