INSERT INTO encuesta(encuesta_id,  nombre_encuesta,descripcion_encuesta)
    VALUES (NEXTVAL('public.encuesta_encuesta_id_seq'), ?,  ?);