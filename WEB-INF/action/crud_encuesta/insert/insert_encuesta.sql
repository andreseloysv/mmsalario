INSERT INTO encuesta(
					 encuesta_id,  
					 nombre_encuesta,
					 descripcion_encuesta,
					 image_data,
					 filename,
					 content_type
					 )
    VALUES (NEXTVAL('public.encuesta_encuesta_id_seq'), 
    				 ?,
    				 ?,
    				 ?,
    				 ?,
    			     ?
    			     );