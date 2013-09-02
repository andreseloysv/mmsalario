select	
	public.empresa.id_empresa, 
	public.empresa.nombre as nombre_pl0,
	public.imagen.description,
	public.imagen.id_imagen

from
	public.empresa,
	public.imagen 
where
	public.empresa.id_empresa=public.imagen.id_empresa and
	id_imagen = ${fld:id} 
	


