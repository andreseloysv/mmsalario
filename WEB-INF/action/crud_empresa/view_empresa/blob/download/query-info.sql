select
	content_type, 
	filename
from demo.imagebank
where id = ${fld:id}
