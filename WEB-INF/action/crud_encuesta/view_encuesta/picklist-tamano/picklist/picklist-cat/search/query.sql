select 
	categoryid, 
	categoryname
from 
	demo.categories c
where
	upper (categoryname) like upper (${fld:name})
order by
	categoryname
