select	
	e.id_empresa, 
	e.nombre as nombre_pl0,
	i.id_imagen,
	i.filename,
	i.content_type,
	i.description,
	i.image_size

from
	empresa as e,
	imagen as i 
where
	e.id_empresa=i.id_empresa
	
	${filter} 

order by 
	i.id_imagen
