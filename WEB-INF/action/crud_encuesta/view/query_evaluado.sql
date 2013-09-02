select
	e.id_empresa,
	e.nombre as empresa,
	uo.nombre as unidad_organizativa,
	eval.id_evaluado,
	p.nombre as nombre_evaluado,
	p.apellido as apellido_evaluado,
	c.id_cargo as id_cargo_evaluado,
	c.nombre as cargo_evaluado,
	eval.id_evaluador,
	p2.nombre as nombre_evaluador,
	p2.apellido as apellido_evaluador,
	c3.id_cargo as id_cargo_evaluador,
	c3.nombre as cargo_evaluador,
	r.id_relacion,
	r.nombre as relacion,
	p3.id_periodo,
	p3.nombre as periodo,
	c2.id_cuestionario,
	c2.nombre as cuestionario,
	eval.id_evaluacion
from
	empresa e,
	u_org uo,
	evaluacion eval,
	persona p,
	persona p2,
	cargo c,
	cargo c3,
	p_cargo pc,
	p_cargo pc2,
	relacion r,
	periodo p3,
	cuestionario c2
where
	eval.id_evaluador = {fld:id_evaluador}
and
	eval.id_evaluado = p.id_persona
and
	eval.id_evaluador = p2.id_persona
and
	p2.id_u_org = uo.id_u_org
and
	eval.estatus = 'finalizado'
and
	p.id_persona = pc.id_persona
and
	pc.id_cargo = c.id_cargo
and
	pc.estatus = 'vigente'
and
	p2.id_persona = pc2.id_persona
and
	pc2.id_cargo = c3.id_cargo
and
	pc2.estatus = 'vigente'
and
	eval.id_relacion = r.id_relacion
and
	eval.id_periodo = p3.id_periodo
and
	eval.id_cuestionario = c2.id_cuestionario
and
	c2.id_empresa = e.id_empresa