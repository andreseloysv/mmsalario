select 
	productid, 
	productname, 
	categoryname
from 
	demo.products p,
	demo.categories c
where
	upper (productname) like upper (${fld:name})
	and c.categoryid = p.categoryid
order by
	productname
