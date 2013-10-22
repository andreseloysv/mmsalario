package domain.mmcontrolclass;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import dinamica.Db;
import dinamica.GenericTransaction;
import dinamica.Recordset;
import dinamica.RecordsetException;

public class reCalculaPromedio extends GenericTransaction {
	
	
	private void agrega_empresas_a_encuesta(Recordset encuesta,Recordset empresas,Recordset rs_es_patrocinante) throws Throwable{
		
		int es_patrocinante;
		es_patrocinante=0;
		
		encuesta.top();
		rs_es_patrocinante.first();

		while (empresas.next()) {
			
			try{
				
				Recordset rs_encuesta_empresa=null;
				rs_encuesta_empresa=new Recordset();
				
				rs_encuesta_empresa.append("fk_empresa_id", java.sql.Types.INTEGER);
				rs_encuesta_empresa.append("fk_encuesta_id", java.sql.Types.INTEGER);
				rs_encuesta_empresa.append("es_patrocinante", java.sql.Types.INTEGER);
				
				rs_encuesta_empresa.addNew();
				rs_encuesta_empresa.setValue("fk_empresa_id", new Integer(empresas.getString("empresa_id") ));
				
				encuesta.first();
				rs_encuesta_empresa.setValue("fk_encuesta_id", new Integer(encuesta.getString("encuesta_id")));
				
				if(rs_es_patrocinante.getInt("tipo_cliente")==0){
					es_patrocinante=1;
				}
				else if(rs_es_patrocinante.getInt("tipo_cliente")==1){
					es_patrocinante=0;
				}
				
				rs_encuesta_empresa.setValue("es_patrocinante", es_patrocinante);
				String[] campos_encuesta_empresa={"fk_empresa_id","fk_encuesta_id","es_patrocinante"};
				getDb().execBatch(getResource("insert_encuesta_empresa.sql"), rs_encuesta_empresa,campos_encuesta_empresa);
				
				rs_es_patrocinante.next();
				
			}catch(RecordsetException e){
				e.printStackTrace();
			}
		}
	}

	
	// Elimina las encuestas que no tienen nombre
	private boolean limpia_encuesta(){
		
		String query="DELETE FROM encuesta WHERE nombre_encuesta is null and encuesta_id!=(select MAX(encuesta_id) from encuesta);";
		
		try{
			this.getDb().exec(query);
			
		}catch(Throwable e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public int service(Recordset inputParams) throws Throwable {
		
		// Elimina las encuestas que no tienen nombre
//		limpia_encuesta();
		
		Recordset aux_recordset = (Recordset) getRequest().getSession().getAttribute("detail.sql");

		aux_recordset.first();
		int dimension_empresa=aux_recordset.getRecordCount();
		
		Recordset rs_encuesta=null;
		rs_encuesta=new Recordset();
		rs_encuesta.append("nombre_encuesta", java.sql.Types.VARCHAR);
		rs_encuesta.append("descripcion_encuesta", java.sql.Types.VARCHAR);
		
		rs_encuesta.addNew();
		rs_encuesta.setValue("nombre_encuesta", new String (aux_recordset.getString("nombre_muestra_form")));
		rs_encuesta.setValue("descripcion_encuesta", new String ("Mi muestrica"));
		String[] campos_encuesta={"nombre_encuesta","descripcion_encuesta"};
		
		String query_max_encuesta = "SELECT max(encuesta_id) as encuesta_id FROM encuesta";
		Recordset rs_max_encuesta = this.getDb().get(query_max_encuesta);
		
		rs_max_encuesta.first();
		int id_encuesta= new  Integer (rs_max_encuesta.getString("encuesta_id"));
		
		
		String query_update_encuesta = "UPDATE encuesta SET nombre_encuesta='"+aux_recordset.getString("nombre_muestra_form")+"', descripcion_encuesta='mi encuestica' WHERE encuesta_id="+id_encuesta+";";

		this.getDb().exec(query_update_encuesta);
		
		// Inserto la tupla promedio relacionada a la encuesta que acabo de crear
		
		Recordset rs_promedio=null;
		rs_promedio=new Recordset();
		rs_promedio.append("encuesta_fk", java.sql.Types.INTEGER);
		
		///  cargo_fk,encuesta_fk
		
		rs_promedio.addNew();
		rs_max_encuesta.first();
		rs_promedio.setValue("encuesta_fk", new  Integer (rs_max_encuesta.getString("encuesta_id")));
		String[] campos_promedio={"encuesta_fk"};
		getDb().execBatch(getResource("insert_promedio.sql"), rs_promedio,campos_promedio);
		String query_max_promedio = "SELECT max(promedio_id) as promedio_id FROM promedio";
		Recordset rs_max_promedio = this.getDb().get(query_max_promedio);
		rs_max_promedio.first();

		
		Recordset aux = null;
		Recordset rs_busqueda = null;
		Recordset empresa_puesto = null;
		String campo_busqueda = "";

		String query_sector = "SELECT sector_id, nombre_sector FROM sector";
		String query_tamano = "SELECT tamano_id, nombre FROM tamano";
		String query_empresa = "SELECT e.empresa_id, e.nombre_empresa ,pd.fecha_datos, s.nombre_sector as sector, t.nombre as tamano, s.sector_id, t.tamano_id FROM empresa as e, procedencia_datos as pd, sector as s, tamano as t WHERE  pd.empresa_fk=e.empresa_id and e.sector_fk=s.sector_id and e.tamano_fk=t.tamano_id ";
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");		
		
		int flag=0;
		aux_recordset.first();
		for (int i = 0; i < dimension_empresa; i++) {
			
			String newstring = aux_recordset.getString("fecha_ini_registro");
			
			if((i+1)<dimension_empresa){
				if(flag==0){
					query_empresa+=" and ( ";
				}
				query_empresa+="  (UPPER(e.nombre_empresa)=UPPER('"+aux_recordset.getString("nombre_empresa")+"') and pd.fecha_datos ='"+newstring+"') or ";
				flag=1;
			}else
			{
				if(flag==0){
					query_empresa+=" and (";
				}
				query_empresa+="   (UPPER(e.nombre_empresa)=UPPER('"+aux_recordset.getString("nombre_empresa")+"')  and pd.fecha_datos ='"+newstring+"')  ";
				flag=1;
			}
			aux_recordset.next();
		}
		
		query_empresa+=" ) ";
		
		Recordset rs_sector = null;
		Recordset rs_tamano = null;
		Recordset rs_empresa = null;

		
		rs_promedio = new Recordset();
		rs_promedio.append("sector_fk", java.sql.Types.INTEGER);
		rs_promedio.append("tamano_empresa_fk", java.sql.Types.INTEGER);
		rs_promedio.append("empresa_puesto_fk", java.sql.Types.INTEGER);
		rs_promedio.append("empresa_fk", java.sql.Types.INTEGER);
		rs_promedio.append("valor", java.sql.Types.DOUBLE);

		rs_sector = this.getDb().get(query_sector);
		rs_tamano = this.getDb().get(query_tamano);
		rs_empresa = this.getDb().get(query_empresa);
		
		
		agrega_empresas_a_encuesta(rs_max_encuesta,rs_empresa,aux_recordset);
		

		rs_sector.top();
		
		String query_cargo = "select  DISTINCT c.cargo_id, c.codigo_completo from empresa as e, empresa_puesto as ep, cargo as c where e.empresa_id=ep.fk_empresa_id and c.codigo_completo=ep.codigo_completo";
		Recordset rs_cargo = null;
		rs_cargo = this.getDb().get(query_cargo);
		rs_cargo.top();
		while (rs_cargo.next()) {
			rs_empresa.top();
			
			double total = 0;
			double promedio = 0;
			int contador = 0;
			
			List<Double> list_valores =  new ArrayList<Double>();
			double desviacion_estandar=0;
			Integer es_patrocinante=0;
			aux_recordset.first();
				while (rs_empresa.next()) {
					
						String query_lista_empleados = "select ep.sbm from procedencia_datos as pd, empresa_puesto as ep, tamano_empresa as te , empresa as e, sector as s, tamano as t, cargo as c where te.empresa_fk=ep.fk_empresa_id and e.empresa_id=ep.fk_empresa_id and te.empresa_fk=e.empresa_id  and e.sector_fk=s.sector_id and t.tamano_id=te.tamano_fk and ep.fk_empresa_id=e.empresa_id and ep.codigo_completo=c.codigo_completo  and pd.empresa_fk=ep.fk_empresa_id and pd.fecha_datos='"+rs_empresa.getString("fecha_datos")+"' 	"
								+ " and e.empresa_id="
								+ rs_empresa.getString("empresa_id")								
								+ " and c.cargo_id="
								+ rs_cargo.getString("cargo_id");
						
						Recordset rs_lista_empleados = null;
						
						rs_lista_empleados = this.getDb().get(query_lista_empleados);
						
						rs_lista_empleados.top();
						
						while (rs_lista_empleados.next()) {
							list_valores.add(rs_lista_empleados.getDouble("sbm"));
							total += rs_lista_empleados.getDouble("sbm");
							contador++;
						}
						aux_recordset.next();
					}
				
					promedio = (total / contador);

					Double[] valores = list_valores.toArray(new Double[list_valores.size()]);
					calcula CalculoDesviacion = new calcula();
					desviacion_estandar=CalculoDesviacion.desviacionStandar(valores,promedio);
					
					rs_max_promedio.first();
					
					rs_promedio = null;
					rs_promedio = new Recordset();
					
					rs_promedio.append("desviacion_estandar",java.sql.Types.DOUBLE);
					rs_promedio.append("promedio_fk", java.sql.Types.INTEGER);
					rs_promedio.append("cargo_fk", java.sql.Types.INTEGER);
					rs_promedio.append("promedio", java.sql.Types.DOUBLE);

					rs_promedio.addNew();

					rs_promedio.setValue("desviacion_estandar", desviacion_estandar);

					rs_promedio.setValue("promedio_fk",	new Integer(rs_max_promedio.getString("promedio_id")));
					
					rs_promedio.setValue("cargo_fk",	new Integer(rs_cargo.getString("cargo_id")));
					
					rs_promedio.setValue("promedio", promedio);
					
					String[] campos = { "desviacion_estandar","promedio_fk","cargo_fk", "promedio" };
					
					getDb().execBatch(getResource("insert_promedio_empresa_puesto.sql"),rs_promedio, campos);
				}

		getResponse().setContentType("text/html; charset=iso-8859-1");
		// PrintWriter out = getResponse().getWriter();
		// String template = this.getResource("template.htm");
		// template = template.replaceAll("(def:context)", this.getContext()
		// .getContextPath());
		// out.println(template);
		// out.close();
		return 0;
	}
}