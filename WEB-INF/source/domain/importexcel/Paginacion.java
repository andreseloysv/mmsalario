package domain.importexcel;

import java.io.PrintWriter;
import java.util.regex.Matcher;

import dinamica.Db;
import dinamica.GenericTransaction;
import dinamica.Recordset;

public class Paginacion extends GenericTransaction {
	@Override
	public int service(Recordset inputParams) throws Throwable {


		int pagina = 1;

		Recordset aux_recordset = (Recordset) getRequest().getSession().getAttribute("busca_encuesta.sql");
		System.out.print("PARTY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.print(aux_recordset.toString());
		System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

		getResponse().setContentType("text/html; charset=iso-8859-1");
		PrintWriter out = getResponse().getWriter();
		String template = this.getResource("template.htm");

		
		template = template.replaceAll("(def:context)", this.getContext()
				.getContextPath());
		out.println(template);
		out.close();

		return 0;
	}
}