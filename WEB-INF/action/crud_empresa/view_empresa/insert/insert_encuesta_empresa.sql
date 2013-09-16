INSERT INTO encuesta_empresa(empresa_encuesta_id,fk_empresa_id,fk_encuesta_id,  fecha_ini,es_patrocinante)
    VALUES (NEXTVAL('public.muestra_muestra_id'), ?,?,  NOW(),  ?);
    
