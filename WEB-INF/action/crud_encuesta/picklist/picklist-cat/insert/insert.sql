insert into demo.categories 
(
	categoryid,
	categoryname
)
values 
(
	${seq:currval@demo.seq_category},
	${fld:newname}
)
