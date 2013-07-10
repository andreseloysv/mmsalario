INSERT INTO demo.familiar
(
	familiar_id,
	nombre,
	filiacion,
	fnacimiento,
	telefono,
	afiliado_id
)
VALUES
(
	${seq:nextval@demo.seq_familiar},
	${fld:nombre_familiar},
	${fld:filiacion},
	${fld:fnacimiento},
	${fld:telefono_familiar},
	${fld:id}
)

