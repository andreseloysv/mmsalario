package domain.mmcontrolclass;

import java.io.PrintWriter;
import java.util.regex.Matcher;

import dinamica.Db;
import dinamica.GenericTransaction;
import dinamica.Recordset;

public class busca_empresa extends GenericTransaction {

	@Override
	public int service(Recordset inputParams) throws Throwable {

		String nombre_tamano = null;
		String nombre_sector = null;
		String rango_fecha_ini_registro = null;
		String rango_fecha_fin_registro = null;

		String resultado = "";
		Recordset aux_recordset = (Recordset) getRequest().getSession()
		.getAttribute("viewchart.filter");
	
//		System.out.print(aux_recordset.getString("nombre_tamano"));
		
		if (inputParams.containsField("nombre_tamano")){
			nombre_tamano = inputParams.getString("nombre_tamano");
		}
		
		if (inputParams.containsField("nombre_sector")){
			nombre_sector = inputParams.getString("nombre_sector");
		}
		
		if (inputParams.containsField("rango_fecha_ini_registro")){
			rango_fecha_ini_registro = inputParams.getString("rango_fecha_ini_registro");
		}
		
		if (inputParams.containsField("rango_fecha_fin_registro")){			
			rango_fecha_fin_registro = inputParams.getString("rango_fecha_fin_registro");
		}
		
		Recordset rs_empresa = null;
		
		//Muestra por ahora no esta puesto

//		String query = "select e.nombre_empresa,to_char(p.fecha_datos,'dd-mm-yyyy') as fecha from procedencia_datos p,empresa as e, tamano as t, sector as s where e.sector_fk=s.sector_id and e.tamano_fk=t.tamano_id and p.empresa_fk=e.empresa_id ";
		
		String query = "SELECT	e.nombre_empresa,p.fecha_datos as fecha FROM procedencia_datos p, empresa as e,	tamano as t, sector as s WHERE e.sector_fk=s.sector_id and e.tamano_fk=t.tamano_id and	p.empresa_fk=e.empresa_id ";
		
		if((rango_fecha_ini_registro!=null)&&(rango_fecha_fin_registro!=null)){
			String[] vector = rango_fecha_ini_registro.split("-");
			rango_fecha_ini_registro=vector[2]+"-"+vector[1]+"-"+vector[0];
			
			vector = rango_fecha_fin_registro.split("-");
			rango_fecha_fin_registro=vector[2]+"-"+vector[1]+"-"+vector[0];
			
			query+=" and p.fecha_datos BETWEEN ' "+rango_fecha_ini_registro+"' and '"+rango_fecha_fin_registro+"'";
		} 
		
		if(nombre_tamano!=null){
			query+=" and upper(t.nombre)=upper('"+nombre_tamano+"')";
		}
		if(nombre_sector!=null){
			query+=" and upper(s.nombre_sector)=upper('"+nombre_sector+"')";
		}

		query+=" order by p.fecha_datos desc limit 1";
		
		rs_empresa = this.getDb().get(query);
		rs_empresa.top();
		resultado+="<table class='grid' style='width: 500px;' cellspacing='1'>";
		resultado+="<tr>";
		resultado+="<th style='text-align: center;width: 300px;'>Empresa</th>";
		resultado+="<th style='text-align: center;width: 100px;'>Fecha de datos</th>";
		resultado+="<th style='text-align: center;width: 50px;'>Empresa patrocinante</th>";
		resultado+="<th style='text-align: center;width: 50px;'>Empresa participante</th>";
		int contador=1;
		
		while (rs_empresa.next()) {
			resultado+="<tr>";
			
			resultado+="<td style='width: 500px;' colspan='4' >";
			
			resultado+="<form name='form_resultado_busca_empresa__"+contador+"' id='form_resultado_busca_empresa__"+contador+"' onsubmit='return false;'>";
			
			resultado+="<div class='div_horizontal' style=' width: 244px;' >";
				resultado+="<input type='text' id='nombre_empresa' value='"+rs_empresa.getString("nombre_empresa")+"' name='nombre_empresa' style='width: 180;border: none;background-color: white;' disabled>";
			resultado+="</div>";
			
			resultado+="<div class='div_horizontal' >";
				resultado+="<input   id='fecha_ini_registro' name='fecha_ini_registro' disabled value='"+rs_empresa.getString("fecha")+"' style='width: 85px;height: 20px;border: none;text-align: center;background-color: white;margin-left: 10px;'>";
			resultado+="</div>";
				
			resultado+="<div class='div_horizontal' style=' width: 90px;text-align: center;'>";
				resultado+="<input type='hidden' name='rowindex'>";
				resultado+="  <input type='radio' id='patrocinante' name='tipo_cliente' value='0'  style='text-align: center; ' ><br> ";
			resultado+="</div>";
				
			resultado+="<div class='div_horizontal' style=' width: 60px;text-align: center;'>";
				resultado+="  <input type='radio' id='participante' name='tipo_cliente' value='1'  style='text-align: center;' ><br> ";
			resultado+="</div>";
			
			resultado+="</form>";
			
			resultado+="</td>";
			resultado+="</tr>";
			contador++;
		}
		resultado+="<tr>";
		resultado+="<td style='width: 500px;text-align: right;' colspan='4'>";
		resultado+="<a href='#' onClick='llenaListDestailInsert()' style='background: none;'>Agregar empresas a la muestra </a><img	src='def:context/images/Add.png' class='button'	onClick='llenaListDestailInsert()' title='Seleccionar item' />";
		resultado+="</td>";
		resultado+="</tr>";
		resultado+="</table>";

		getResponse().setContentType("text/html; charset=iso-8859-1");
		PrintWriter out = getResponse().getWriter();

		String template = resultado;

		template = template.replaceAll("(def:context)", this.getContext()
				.getContextPath());
		out.println(template);
		out.close();
		return 0;
	}
}