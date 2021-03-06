INSERT INTO empresa_puesto(
            puesto_id, 				 
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
	             fecha_registro,
	             fecha_ingreso )
values 
(
	NEXTVAL('public.empresa_puesto_puesto_id_seq'),
	?,
	?,
	?,
	?,
	?,
	?,
	?,
	?,
	?,
	?,
	?,
	?,
	?,
	?,
	(select current_date),
	?
);