select SUM( count(puesto_id) ) OVER (ORDER BY puesto_id) as contador,
	puesto_id, 				 
            nombre_empresa , 
				 codigo_completo ,
				 codigo_disciplina ,
				 codigo_nivel ,
				 codigo_diferenciador ,
				 codigo_area ,
	             titulo_puesto ,
	             sbm ,
	             comision_target , 
	             fondo_ahorro_extra ,
	             bono_variable ,
	             fondo_ahorro_ordinario , 
	             asignacion_no_salarial ,
	             asignacion_vehiculo ,
	             fecha_ingreso

from public.empresa_puesto, empresa

where

empresa_id=fk_empresa_id
group by puesto_id, 				 
            fk_empresa_id , 
				 codigo_completo ,
				 codigo_disciplina ,
				 codigo_nivel ,
				 codigo_diferenciador ,
				 codigo_area ,
	             titulo_puesto ,
	             sbm ,
	             comision_target , 
	             fondo_ahorro_extra ,
	             bono_variable ,
	             fondo_ahorro_ordinario , 
	             asignacion_no_salarial ,
	             asignacion_vehiculo ,
	             nombre_empresa,
	             fecha_ingreso
