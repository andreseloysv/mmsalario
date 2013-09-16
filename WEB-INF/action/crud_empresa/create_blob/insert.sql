INSERT INTO empresa
					(empresa_id,image_size,image_data,filename,content_type)
VALUES 
					(NEXTVAL('public.empresa_empresa_id_seq'),${fld:_filesize},?,${fld:_filename},${fld:_contenttype});