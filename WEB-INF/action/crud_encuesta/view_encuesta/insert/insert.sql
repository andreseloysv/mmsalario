INSERT INTO promedio(
            promedio_id, sector_fk, tamano_empresa_fk, cargo_fk, empresa_fk, valor,desviacion_estandar,fk_muestra)
    VALUES (NEXTVAL('public.promedio_promedio_id'), ?,  ?,  ? ,  ? ,  ?,  ?,  ? );
