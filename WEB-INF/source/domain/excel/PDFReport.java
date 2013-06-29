package domain.excel;

import java.io.ByteArrayOutputStream;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import dinamica.*;

/**
 * Reporte PDF (IText) para BLOB con logo, titulo, tabla con los tipos de documentos cargados, 
 * y pie de pagina hecho con codigo, con estilo "pagina X de Y". 
 * Para el manejo de los eventos que imprimen el pie de pagina, esta clase usa
 * una clase generica del framework llamada "PDFPageEvents". Puede proveer
 * su propio manejador de eventos si tiene necesidades mas elaboradas.
 * <br><br>
 * Actualizado: 2007-05-29<br>
 * Framework Dinamica - Distribuido bajo licencia LGPL<br>
 * @author mcordova (martin.cordova@gmail.com)
 */
public class PDFReport extends AbstractPDFOutput
{

	//parametros requeridos para el footer
    PdfTemplate tpl = null;
    BaseFont bf = null;
    PdfContentByte cb = null;
    Image img = null;
    Font tblHeaderFont = null;
    Font tblBodyFont = null;
    
    //cambiar: parametros generales del reporte	
    String reportTitle = ""; //lo lee de config.xml por defecto
    String footerText = ""; //lo lee de web.xml o config.xml por defecto
    String logoPath = "/images/logo-dinamica.png"; //ubicacion del logotipo
    String pageXofY = " de ";  //texto por defecto para Pagina X de Y
    
    @Override
    protected void createPDF(GenericTransaction data, ByteArrayOutputStream buf)
            throws Throwable
    {

		//inicializar documento: tamano de pagina, orientacion, margenes
		Document doc = new Document();
		PdfWriter docWriter = PdfWriter.getInstance(doc, buf);
		doc.setPageSize(PageSize.LETTER.rotate());
		doc.setMargins(30,30,30,40);
		
		doc.open();

		//crear fonts por defecto
		tblHeaderFont = new Font(Font.HELVETICA, 10f, Font.BOLD);
		tblBodyFont = new Font(Font.HELVETICA, 10f, Font.NORMAL);
		
		//definir pie de pagina del lado izquierdo
		String footerText = this.getFooter(); //read it from config.xml or web.xml
		String reportDate = StringUtil.formatDate(new java.util.Date(), "dd-MM-yyyy HH:mm");
		
		//crear template (objeto interno de IText) y manejador de evento 
		//para imprimir el pie de pagina
		bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		cb = docWriter.getDirectContent();
		tpl = cb.createTemplate(20, 14);
		docWriter.setPageEvent(new PDFPageEvents(footerText, pageXofY, tpl, bf, cb, reportDate));

		//titulo - lo lee de config.xml por defecto
		reportTitle = getReportTitle();
		Paragraph t = new Paragraph(reportTitle, new Font(Font.HELVETICA, 14f, Font.BOLD));
		t.setAlignment(Rectangle.ALIGN_RIGHT);
		doc.add(t);

		//logo
		img = Image.getInstance(getImage(this.getServerBaseURL() + logoPath, false));
		img.scalePercent(100);
		float imgY = doc.top() - img.getHeight();
		float imgX = doc.left();
		img.setAbsolutePosition(imgX, imgY);
		doc.add(img);	
		
		doc.add(new Paragraph(" "));
		doc.add(new Paragraph(" "));
		doc.add(new Paragraph(" "));
		doc.add(new Paragraph(" "));
		
		//imprimir tabla de datos
		PdfPTable datatbl = getDataTable(data);
		datatbl.setSpacingBefore(10);
		doc.add(datatbl);

		doc.close();
		docWriter.close();
		
    }

	/**
	 * Retorna una tabla conteniendo los tipos de documentos cargados
	 * @param data Objeto de negocios que suple los recordsets
	 * @return
	 */
	PdfPTable getDataTable(GenericTransaction data) throws Throwable
	{

		//obtener recordset de data
		Recordset rs = data.getRecordset("query.sql");
		rs.top();

		//definir estructura de la tabla
		PdfPTable datatable = new PdfPTable(5);
		datatable.getDefaultCell().setPadding(3);
		datatable.setWidthPercentage(100);
		datatable.setHeaderRows(1);

		PdfPCell c = null;
		String v = "";
		
		//encabezados de columnas
		c = new PdfPCell( new Phrase("Código Cliente", tblHeaderFont) );
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);
		
		c = new PdfPCell( new Phrase("Fecha de la Orden", tblHeaderFont) );
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);

		c = new PdfPCell( new Phrase("Código Producto", tblHeaderFont) );
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);

		c = new PdfPCell( new Phrase("Precio Unitario", tblHeaderFont) );
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);
		
		c = new PdfPCell( new Phrase("Descuento", tblHeaderFont) );
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);

		//imprimir cuerpo de la tabla
		while (rs.next())
		{			
			v = rs.getString("customerid");
			c = new PdfPCell( new Phrase( v, tblBodyFont ) );
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable.addCell(c);
			
			v = rs.getString("orderdate");
			c = new PdfPCell( new Phrase( v, tblBodyFont ) );
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable.addCell(c);
			
			v = rs.getString("productid");
			c = new PdfPCell( new Phrase( v, tblBodyFont ) );
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable.addCell(c);

			v = StringUtil.formatNumber(rs.getValue("unitprice"),"#,###,##0.00");
			c = new PdfPCell( new Phrase( v , tblBodyFont ) );
			c.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable.addCell(c);
			
			v = StringUtil.formatNumber(rs.getValue("discount"),"#,###,##0.00");
			c = new PdfPCell( new Phrase( v , tblBodyFont ) );
			c.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable.addCell(c);
			
		}

		return datatable;
		
	}

	
}
