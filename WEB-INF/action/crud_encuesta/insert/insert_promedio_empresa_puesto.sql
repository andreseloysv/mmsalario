INSERT INTO promedio_empresa_puesto(
															
            id_promedio_empresa_puesto  , desviacion_estandar , promedio_fk , cargo_fk , promedio , fecha_ini)
    VALUES (NEXTVAL('public.promedio_promedio_id'),  ?,  ? ,  ? ,  ? ,  NOW() );