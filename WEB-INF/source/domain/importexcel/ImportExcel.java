package domain.importexcel;

import java.io.File;
import java.util.Date;
import jxl.*;
import dinamica.*;

/**
 * Clase que lee una hoja de calculo de archivo Excel, la valida contra la
 * base de datos, verifica que los registros sean del tipo Date, Integer,
 * Double, character varying. Graba los registros en batch usando una tecnica muy
 * avanzada que incluye el framework. Además para el manejo de excepciones 
 * genera un recordset con los errores.
 * <br><br>
 * (c) 2008 Martin Cordova y Asociados<br>
 * This code is released under the LGPL license<br>
 * Dinamica Framework - http://www.martincordova.com<br>
 * */

public class ImportExcel extends GenericTableManager  {

	//variable que contendra el nombre de la columna que produjo la excepcion
	String columna = null;
	
	@Override
	public int service(Recordset inputParams) throws Throwable {
		
		
		
		int rc = super.service(inputParams);
		// para leer la fecha
		inputParams.getDate("fecha");
		//crea un recordset para el manejo de los errores
		Recordset rsError = new Recordset();
				
		//define la estructura del recordset que contendra los errores
		rsError.append("columna", java.sql.Types.VARCHAR);
		rsError.append("fila", java.sql.Types.INTEGER);
		rsError.append("error", java.sql.Types.VARCHAR);
				
		//se alamacena en sesion el recordset de errores validacion
		getSession().setAttribute("error.excel",rsError);
		
		//registrar el numero de errores
		inputParams.setValue("total_errores", new Integer(rsError.getRecordCount()));
		
		//archivo nulo sera considerado un error
		if (inputParams.isNull("file.filename"))
			throw new Throwable("¡Por favor indique una ruta válida de archivo!");
		
		//obtener parametros del validator
		String file = inputParams.getString("file");
		
		//obtener el archivo Excel
		Workbook wb;
		
		try {
			wb = Workbook.getWorkbook(new File(file));
		} catch (JXLException e1) {
			throw new Throwable ("Formato Excel no reconocido; use Excel 97, XP o 2003",e1);
		}
		
		//obtener la hoja de calculo del archivo
		Sheet sheet = wb.getSheet(0);
		
		//obtener el numero de registros
		int numOfRows = sheet.getRows();
		
		//crea un recordset que contendra los registro leidos del archivo Excel
		Recordset rs = new Recordset();
					 
		//define la estructura del recordset
		rs.append("customerid", java.sql.Types.VARCHAR);
		rs.append("orderdate", java.sql.Types.DATE);
		rs.append("productid", java.sql.Types.INTEGER);
		rs.append("unitprice", java.sql.Types.DOUBLE);
		rs.append("discount", java.sql.Types.DOUBLE);
		
		//obtener el numero de columnas
		int columnas = sheet.getColumns();
		    
		//numero de columnas del archivo es igual a 10?
		if (columnas == 5)
		{
			//mientras exista registros
			for(int i = 1; i<numOfRows;i++)
			{ 
				try
				{
					//añadir un record
					rs.addNew();

					//obtener la data de cada celda
					Cell columna1 = sheet.getCell(0,i);   
					Cell columna2 = sheet.getCell(1,i);   
					Cell columna3 = sheet.getCell(2,i);   
					Cell columna4 = sheet.getCell(3,i);   
					Cell columna5 = sheet.getCell(4,i);  
					
					//la celda esta vacia?
					if (columna1.getContents() == null || columna1.getContents().equals(""))
					{
						columna = "Cliente";
						throw new Throwable ("La celda no puede estar vacia.");
					}
					else
					{
						columna = "Cliente";
						rs.setValue("customerid", findCliente(columna1.getContents()));			
					}
					
					//lee y valida celda de tipo date
					Date dcolum2 = ValidatorUtil.testDate(columna2.getContents(), "dd-MM-yy");
					if (dcolum2!=null)
						rs.setValue("orderdate", dcolum2);
					else
					{
						columna = "Fecha de Orden";
						throw new Throwable ("La fecha no fue ingresada correctamente. Tipee una fecha en formato Día-Mes-Año y use como separador el guión (-)");
					}
					
					//lee y valida celda de tipo Integer
					Integer dcolum3 = ValidatorUtil.testInteger(columna3.getContents());
					if (dcolum3!=null)
					{
						columna = "Producto";
						rs.setValue("productid", findProducto(dcolum3));
					}
					else
					{
						columna = "Producto";
						throw new Throwable ("El dato ingresado no representa un número.");
					}
					
					//lee y valida celda de tipo double
					Double dcolum4 = ValidatorUtil.testDouble(columna4.getContents().replace(",", "."));
					if (dcolum4!=null)
						rs.setValue("unitprice", dcolum4);
					else
					{
						columna = "Precio Unitario";
						throw new Throwable ("El dato ingresado no representa un número. Tipee un número y use la coma (,) como separador de decimales.");
					}
						
					//lee y valida celda de tipo double
					Double dcolum5 = ValidatorUtil.testDouble(columna5.getContents().replace(",", "."));
					if (dcolum5!=null)
						rs.setValue("discount", dcolum5);
					else
					{
						columna = "Descuento";
						throw new Throwable ("El dato ingresado no representa un número. Tipee un número y use la coma (,) como separador de decimales.");
					}

				}
				catch (Throwable a)
				{
					//añadir un nuevo record
					rsError.addNew();
					rsError.setValue("columna", columna);
					rsError.setValue("fila", (i+1));
					rsError.setValue("error", a.getMessage());
					
					//registrar el numero de errores
					inputParams.setValue("total_errores", new Integer(rsError.getRecordCount()));
					
					//recordset de errores tienes 20 registros?
					if (rsError.getRecordCount()==20)
					{
						getSession().setAttribute("error.excel",rsError);
						throw new Throwable("El archivo de Excel contiene más de 20 errores.");
					}
				}
			}
		}
		else
		{
			throw new Throwable("El archivo de Excel no contiene el formato especifícado.");   
		}
		
		//recordset de errores tiene registro?
		if (rsError.getRecordCount()>0) 
		{
			getSession().setAttribute("error.excel",rsError);
			throw new Throwable("El archivo de Excel contiene errores.");
		}
						
		//registrar el numero de registros insertados
		inputParams.setValue("total_registros", new Integer(rs.getRecordCount()));
		
		//dar inicio a la transacción
		getDb().beginTrans();
		
		//ejecutar el insert de lotes
		getDb().exec(getSQL(getResource("insert_lote.sql"), inputParams));
		
		//definir parametros
		String[] params =
		{
			"customerid",
			"orderdate",
			"productid",
			"unitprice",
			"discount"
		};

		//obtener el Resource
		String sql = getSQL(getResource("insert.sql"), inputParams);
		
		//ejecutar en Batch
		getDb().execBatch(sql, rs, params);
		
		//colocar en sesion el recordset
		getSession().setAttribute("query.sql", rs);
		
		return rc;
	}
	
	
	
	// Esta funcion valida los SBM de cada empresa segun los promedios de cada cargo.
	void verificacionDeValores(Recordset DatosNuevos){
//		DatosNuevos
		
	}
	
	/**
	 * Verifica que el cliente existe en base de datos
	 * @param cliente ID del cliente
	 * @return ID del cliente
	 * @throws Throwable
	 */
	String findCliente (String cliente) throws Throwable
	{
		Recordset rs = getRecordset("cliente.sql");
		
		if (rs.findRecord("customerid", cliente) == -1)
			throw new Throwable("El Código del Cliente [" + cliente + "] no está registrado.");
		else
			return cliente;			
	}
	
	/**
	 * Verifica que el producto existe en base de datos
	 * @param producto ID del producto
	 * @return ID del producto
	 * @throws Throwable
	 */
	Integer findProducto (Integer producto) throws Throwable
	{
		Recordset rs = getRecordset("producto.sql");
		
		if (rs.findRecord("productid", producto) == -1)
			throw new Throwable("El Código del Producto [" + producto + "] no está registrado.");	
		else
			return producto;
	}

}
