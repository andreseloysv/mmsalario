INSERT INTO encuesta
					(image_size,image_data,filename,content_type)
VALUES 
					(${fld:_filesize},?,${fld:_filename},${fld:_contenttype});