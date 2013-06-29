package domain.importexcel;

import java.io.PrintWriter;
import java.util.regex.Matcher;

import dinamica.Db;
import dinamica.GenericTransaction;
import dinamica.Recordset;

public class ValidaDatosOutput extends GenericTransaction {
	@Override
	public int service(Recordset inputParams) throws Throwable {

		Recordset aux = null;
		Recordset rs_busqueda = null;
		getSession().setAttribute("resultado", "");
		String resultado = "";
		Recordset empresa_puesto = null;
		String campo_busqueda = "";

		Recordset parametros = inputParams;
		int pagina = 1;
		
		Recordset aux_recordset=(Recordset) getRequest().getSession().getAttribute(
		"viewchart.filter");
		
		
		
		if(((Recordset) getRequest().getSession().getAttribute(
		"viewchart.filter")).getString("pagina")!=null)
		pagina = ((Recordset) getRequest().getSession().getAttribute(
				"viewchart.filter")).getInteger("pagina");
		if(aux_recordset.getRecordCount()>1)
		if(((Recordset) getRequest().getSession().getAttribute(
		"viewchart.filter")). getString("campo_busqueda")!=null)
		campo_busqueda = ((Recordset) getRequest().getSession().getAttribute(
				"viewchart.filter")).getString("campo_busqueda");
		
		if ((campo_busqueda == null) || (campo_busqueda.equals("null"))
				|| ((campo_busqueda.equals(null)))) {
			campo_busqueda = "";
		}
		String[] arreglo_busqueda = campo_busqueda.split(" ");

		String mega_query = "select SUM( count(puesto_id) ) OVER (ORDER BY puesto_id) as contador,  puesto_id,"
				+ "nombre_empresa , codigo_completo , codigo_disciplina ,	 codigo_nivel ,	 codigo_diferenciador ,	 codigo_area ,"
				+ "titulo_puesto ,sbm ,comision_target,fondo_ahorro_extra ,bono_variable ,fondo_ahorro_ordinario ,asignacion_no_salarial ,"
				+ "asignacion_vehiculo ,fecha_ingreso from public.empresa_puesto, empresa "
				+ "where empresa_id=fk_empresa_id and (";

		for (int i = 0; i < arreglo_busqueda.length; i++) {
			mega_query += "( upper(codigo_completo) LIKE '%"
					+ arreglo_busqueda[i].toUpperCase() + "%' or" +

					" upper(codigo_diferenciador) LIKE '%"
					+ arreglo_busqueda[i].toUpperCase() + "%' or"
					+ " upper(codigo_area) LIKE '%"
					+ arreglo_busqueda[i].toUpperCase() + "%' or"
					+ " upper(nombre_empresa) LIKE '%"
					+ arreglo_busqueda[i].toUpperCase() + "%' )";
			if((i+1) != arreglo_busqueda.length){
				mega_query +=" and ";
			}
		}
		mega_query += " ) group by puesto_id,fk_empresa_id ,codigo_completo ,codigo_disciplina ,codigo_nivel , codigo_diferenciador ,"
				+ " codigo_area ,titulo_puesto ,sbm ,comision_target ,fondo_ahorro_extra ,bono_variable ,fondo_ahorro_ordinario ,"
				+ "asignacion_no_salarial ,asignacion_vehiculo ,nombre_empresa,fecha_ingreso";
		rs_busqueda = this.getDb().get(mega_query);
System.out.print(mega_query);
		if (aux == null) {
			aux = rs_busqueda;

		}
		if (aux.getRecordCount() > 0) {
			aux.top();
			aux.setPageSize(20);
			empresa_puesto = aux.getPage(pagina);
			empresa_puesto.top();

			getResponse().setContentType("text/html; charset=iso-8859-1");
			PrintWriter out = getResponse().getWriter();
			String template = this.getResource("template.htm");

			if (empresa_puesto != null)
				while (empresa_puesto.next()) {

					resultado += ("<tr>");
					resultado += ("<th class='titulotablas warning'>"
							+ empresa_puesto.getString("contador") + "</th>");
					resultado += ("<th class='titulotablas warning' style='text-align: center;'><img src='def:context/images/warning.png'></th>");
					resultado += ("<th class='titulotablas celda_td'><input value='"
							+ empresa_puesto.getString("nombre_empresa") + "' class='celda'/></th>");
					resultado += ("<th class='titulotablas celda_td'><input value='"
							+ empresa_puesto.getString("codigo_completo") + "' class='celda'/></th>");
					resultado += ("<th class='titulotablas celda_td'><input value='"
							+ empresa_puesto.getString("codigo_disciplina") + "' class='celda'/></th>");
					resultado += ("<th class='titulotablas celda_td'><input value='"
							+ empresa_puesto.getString("codigo_nivel") + "' class='celda'/></th>");
					resultado += ("<th class='titulotablas celda_td'><input value='"
							+ empresa_puesto.getString("codigo_diferenciador") + "' class='celda'/></th>");
					resultado += ("<th class='titulotablas celda_td'><input value='"
							+ empresa_puesto.getString("codigo_area") + "' class='celda'/></th>");
					resultado += ("<th class='titulotablas celda_td'><input value='"
							+ empresa_puesto.getString("titulo_puesto") + "' class='celda'/></th>");
					resultado += ("<th class='titulotablas celda_td'><input value='"
							+ empresa_puesto.getString("sbm") + "' class='celda' /></th>");
					resultado += ("<th class='titulotablas celda_td'><input value='"
							+ empresa_puesto.getString("comision_target") + "' class='celda'/></th>");
					resultado += ("<th class='titulotablas celda_td'><input value='"
							+ empresa_puesto.getString("fondo_ahorro_extra") + "$' class='celda'/></th>");
					resultado += ("<th class='titulotablas celda_td'><input value='"
							+ empresa_puesto.getString("bono_variable") + "' class='celda'/></th>");
					resultado += ("<th class='titulotablas celda_td'><input value='"
							+ empresa_puesto
									.getString("fondo_ahorro_ordinario") + "' class='celda'/></th>");
					resultado += ("<th class='titulotablas celda_td'><input value='"
							+ empresa_puesto
									.getString("asignacion_no_salarial") + "' class='celda'/></th>");
					resultado += ("<th class='titulotablas celda_td'><input value='"
							+ empresa_puesto.getString("asignacion_vehiculo") + "' class='celda'/></th>");
					resultado += ("<th class='titulotablas celda_td'><input value='"
							+ empresa_puesto.getString("fecha_ingreso") + "' class='celda'/></th>");
					resultado += ("</tr>");
				}

			resultado += ("<tr style='display: none; '><th><input type='hidden' id='PageCount' value=\""
					+ aux.getPageCount() + "\" name='PageCount'>");
			if (aux.getPageNumber() == 1) {
				resultado += ("<input type='hidden' id='PagePrev' value=\""
						+ (aux.getPageNumber()) + "\" name='PagePrev'>");
			} else {
				resultado += ("<input type='hidden' id='PagePrev' value=\""
						+ (aux.getPageNumber() - 1) + "\" name='PagePrev'>");
			}
			if ((aux.getPageNumber()) == aux.getPageCount()) {
				resultado += ("<input type='hidden' id='PageNext' value=\""
						+ (aux.getPageNumber()) + "\" name='PageNext'></th></tr>");
			} else {
				resultado += ("<input type='hidden' id='PageNext' value=\""
						+ (aux.getPageNumber() + 1) + "\" name='PageNext'></th></tr>");
			}
			resultado = Matcher.quoteReplacement(resultado);

			// String inicio = "";
			// String fin = "";
			// String[] vector_fin = null;
			// inicio = template.split("<div id='tabla_excel'>")[0];
			// vector_fin = template.split("</div>");
			// for (int i = 1; i < vector_fin.length; i++) {
			// fin += vector_fin[i];
			// }
			template = resultado;
			// template = inicio + resultado + fin;
			template = template.replaceAll("(def:context)", this.getContext()
					.getContextPath());
			out.println(template);
			out.close();
		}

		return 0;
	}
}