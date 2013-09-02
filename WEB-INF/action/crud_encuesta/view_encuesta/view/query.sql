Insert into calificacion_final_evaluacion
(
	id_calificacion_final_evaluacion,
	id_tipo_indicador,
	total_tipo_indicador,
	id_indicador,
	total_indicador,
	id_evaluacion
)
Values
(
	NEXTVAL('calificacion_final_evaluacion_id_calificacion_final_evaluac_seq'),
	${fld:id_tipo_indicador},
	${fld:total_tipo_indicador},
	${fld:id_indicador},
	${fld:total_indicador},
	${fld:id_evaluacion}
);