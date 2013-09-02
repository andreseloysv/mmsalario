package domain.importexcel;

import java.io.PrintWriter;
import java.util.regex.Matcher;

import dinamica.Db;
import dinamica.GenericTransaction;
import dinamica.Recordset;

public class calculaPromedio extends GenericTransaction {

	@Override
	public int service(Recordset inputParams) throws Throwable {

		Recordset aux = null;
		Recordset rs_busqueda = null;
		Recordset empresa_puesto = null;
		String campo_busqueda = "";


		String query_sector = "select sector_id, nombre_sector from sector";
		String query_tamano = "select tamano_id, nombre from tamano";
		String query_empresa = "select empresa_id, nombre_empresa from empresa";

		Recordset rs_sector = null;
		Recordset rs_tamano = null;
		Recordset rs_empresa = null;
		Recordset rs_promedio = null;
		//
		// promedio_id, sector_fk, tamano_empresa_fk, empresa_puesto_fk,
		// valor
		rs_promedio= new Recordset();
		rs_promedio.append("sector_fk", java.sql.Types.INTEGER);
		rs_promedio.append("tamano_empresa_fk", java.sql.Types.INTEGER);
		rs_promedio.append("empresa_puesto_fk", java.sql.Types.INTEGER);
		rs_promedio.append("valor", java.sql.Types.DOUBLE);

		rs_sector = this.getDb().get(query_sector);
		rs_tamano = this.getDb().get(query_tamano);
		rs_empresa = this.getDb().get(query_empresa);

		rs_sector.top();
		while (rs_sector.next()) {
			rs_tamano.top();
			while (rs_tamano.next()) {
				rs_empresa.top();
				while (rs_empresa.next()) {
					Recordset rs_cargo = null;
					// String
					// query_cargo="select * from empresa_puesto as ep, tamano_empresa as te , empresa as e, sector s, tamano t, cargo c  where te.empresa_fk=ep.fk_empresa_id and e.empresa_id=ep.fk_empresa_id and te.empresa_fk=e.empresa_id  and e.sector_fk=s.sector_id and s.sector_id=3 and t.tamano_id=te.tamano_fk and t.tamano_id=1 and ep.fk_empresa_id=e.empresa_id and ep.codigo_completo=c.codigo_completo and e.empresa_id=1";
					String query_cargo = "select DISTINCT c.cargo_id, c.codigo_completo from empresa as e, empresa_puesto as ep, cargo as c where e.empresa_id=ep.fk_empresa_id and c.codigo_completo=ep.codigo_completo";
					rs_cargo = this.getDb().get(query_cargo);
					rs_cargo.top();
					while (rs_cargo.next()) {
						String query_lista_empleados = "select ep.sbm from empresa_puesto as ep, tamano_empresa as te , empresa as e, sector as s, tamano as t, cargo as c where te.empresa_fk=ep.fk_empresa_id and e.empresa_id=ep.fk_empresa_id and te.empresa_fk=e.empresa_id  and e.sector_fk=s.sector_id and t.tamano_id=te.tamano_fk and ep.fk_empresa_id=e.empresa_id and ep.codigo_completo=c.codigo_completo and "
								+ "  s.sector_id="
								+ rs_sector.getString("sector_id")
								+ " and t.tamano_id="
								+ rs_tamano.getString("tamano_id")
								+ " and e.empresa_id="
								+ rs_empresa.getString("empresa_id")
								+ " and c.cargo_id="
								+ rs_cargo.getString("cargo_id");
						Recordset rs_lista_empleados = null;
						double total = 0;
						double promedio = 0;
						int contador = 0;
						rs_lista_empleados = this.getDb().get(
								query_lista_empleados);
						rs_lista_empleados.top();
						while (rs_lista_empleados.next()) {
							total += rs_lista_empleados.getDouble("sbm");
							contador++;
						}
						promedio = (total / contador);
						rs_promedio=null;
						rs_promedio=new Recordset();
						rs_promedio.append("sector_fk", java.sql.Types.INTEGER);
						rs_promedio.append("tamano_empresa_fk", java.sql.Types.INTEGER);
						rs_promedio.append("cargo_fk", java.sql.Types.INTEGER);
						rs_promedio.append("valor", java.sql.Types.DOUBLE);
						rs_promedio.addNew();
						rs_promedio.setValue("sector_fk",new Integer(rs_sector.getString("sector_id")));
						rs_promedio.setValue("tamano_empresa_fk", new Integer(
								rs_empresa.getString("empresa_id")));
						rs_promedio.setValue("cargo_fk", new Integer(
								rs_cargo.getString("cargo_id")));
						rs_promedio.setValue("valor", promedio);
						
						String[] campos={"sector_fk","tamano_empresa_fk","cargo_fk","valor"};
						getDb().execBatch(getResource("insert.sql"), rs_promedio,campos);
					}
				}
			}
		}
		getResponse().setContentType("text/html; charset=iso-8859-1");
//		PrintWriter out = getResponse().getWriter();
//		String template = this.getResource("template.htm");
//		template = template.replaceAll("(def:context)", this.getContext()
//				.getContextPath());
//		out.println(template);
//		out.close();
		return 0;
	}
}