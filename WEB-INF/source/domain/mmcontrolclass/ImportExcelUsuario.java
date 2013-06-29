package domain.mmcontrolclass;

import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

import jxl.*;
import dinamica.*;
import domain.importexcel.calculaPromedio;

/**
 * Clase que lee una hoja de calculo de archivo Excel, la valida contra la base
 * de datos, verifica que los registros sean del tipo Date, Integer, Double,
 * character varying. Graba los registros en batch usando una tecnica muy
 * avanzada que incluye el framework. Además para el manejo de excepciones
 * genera un recordset con los errores. <br>
 * <br>
 * (c) 2008 Martin Cordova y Asociados<br>
 * This code is released under the LGPL license<br>
 * Dinamica Framework - http://www.martincordova.com<br>
 * */

public class ImportExcelUsuario extends GenericTableManager {

	// variable que contendra el nombre de la columna que produjo la excepcion
	String columna = null;
	String[] nombre_campos_empresa_puesto;

	@Override
	public int service(Recordset inputParams) throws Throwable {

		ActualizaPromedio();

		String concoma = "";
		Number number = 0;
		int rc = super.service(inputParams);
		int id_empresa = 0;
		int id_empresa_variacion = 0;
		NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);

		// crea un recordset para el manejo de los errores
		Recordset rsError = new Recordset();

		// define la estructura del recordset que contendra los errores
		rsError.append("columna", java.sql.Types.VARCHAR);
		rsError.append("fila", java.sql.Types.INTEGER);
		rsError.append("error", java.sql.Types.VARCHAR);

		// se alamacena en sesion el recordset de errores validacion
		getSession().setAttribute("error.excel", rsError);

		// registrar el numero de errores
		inputParams.setValue("total_errores",
				new Integer(rsError.getRecordCount()));

		// archivo nulo sera considerado un error
		if (inputParams.isNull("file.filename")) {
			throw new Throwable(
					"¡Por favor indique una ruta válida de archivo!");
		}

		// obtener parametros del validator
		String file = inputParams.getString("file");

		// obtener el archivo Excel
		Workbook wb;

		try {
			wb = Workbook.getWorkbook(new File(file));
		} catch (JXLException e1) {
			throw new Throwable(
					"Formato Excel no reconocido; use Excel 97, XP o 2003", e1);
		}

		// obtener la hoja de calculo del archivo
		Sheet sheet = wb.getSheet(0);

		// obtener el numero de registros
		int numOfRows = sheet.getRows();

		// crea un recordset que contendra los registro leidos del archivo Excel
		Recordset rs = new Recordset();
		this.nombre_campos_empresa_puesto = new String[15];
		this.nombre_campos_empresa_puesto[0] = "fk_empresa_id";
		this.nombre_campos_empresa_puesto[1] = "codigo_completo";
		this.nombre_campos_empresa_puesto[2] = "codigo_disciplina";
		this.nombre_campos_empresa_puesto[3] = "codigo_nivel";
		this.nombre_campos_empresa_puesto[4] = "codigo_diferenciador";
		this.nombre_campos_empresa_puesto[5] = "codigo_area";
		this.nombre_campos_empresa_puesto[6] = "titulo_puesto";
		this.nombre_campos_empresa_puesto[7] = "sbm";
		this.nombre_campos_empresa_puesto[8] = "comision_target";
		this.nombre_campos_empresa_puesto[9] = "fondo_ahorro_extra";
		this.nombre_campos_empresa_puesto[10] = "bono_variable";
		this.nombre_campos_empresa_puesto[11] = "fondo_ahorro_ordinario";
		this.nombre_campos_empresa_puesto[12] = "asignacion_no_salarial";
		this.nombre_campos_empresa_puesto[13] = "asignacion_vehiculo";
		this.nombre_campos_empresa_puesto[14] = "fecha_ingreso";

		// define la estructura del recordset
		rs.append(this.nombre_campos_empresa_puesto[0], java.sql.Types.INTEGER); // Nombre
																					// de
																					// la
																					// empresa
		rs.append(this.nombre_campos_empresa_puesto[1], java.sql.Types.VARCHAR); // Encadenado
		rs.append(this.nombre_campos_empresa_puesto[2], java.sql.Types.VARCHAR); // Codigo
																					// -
																					// diciplina
		rs.append(this.nombre_campos_empresa_puesto[3], java.sql.Types.VARCHAR); // Nivel
		rs.append(this.nombre_campos_empresa_puesto[4], java.sql.Types.VARCHAR); // Modificador
		rs.append(this.nombre_campos_empresa_puesto[5], java.sql.Types.VARCHAR); // Familia
		rs.append(this.nombre_campos_empresa_puesto[6], java.sql.Types.VARCHAR); // Titulo
																					// del
																					// puesto
		rs.append(this.nombre_campos_empresa_puesto[7], java.sql.Types.DOUBLE); // SBM
		rs.append(this.nombre_campos_empresa_puesto[8], java.sql.Types.DOUBLE); // Comision
		rs.append(this.nombre_campos_empresa_puesto[9], java.sql.Types.DOUBLE); // Fondo
																				// ahorro
																				// extra
		rs.append(this.nombre_campos_empresa_puesto[10], java.sql.Types.DOUBLE); // Bono
																					// variable
		rs.append(this.nombre_campos_empresa_puesto[11], java.sql.Types.DOUBLE); // Fondo
																					// de
																					// ahorro
																					// ordinario
		rs.append(this.nombre_campos_empresa_puesto[12], java.sql.Types.DOUBLE); // Asignacion
																					// no
																					// salarial
		rs.append(this.nombre_campos_empresa_puesto[13], java.sql.Types.DOUBLE); // Asignacion
																					// de
																					// vehiculo
		rs.append(this.nombre_campos_empresa_puesto[14], java.sql.Types.DATE); // Año
																				// de
																				// ingreso

		// obtener el numero de columnas
		int numero_columnas_excel = sheet.getColumns();
		int numero_columnas_rs = rs.getFieldCount();

		// numero de columnas del archivo es igual a 7?

		// if(true){
		// throw new Throwable(Integer.toString(numero_columnas_rs)+" hola");
		// }

		EmpresaPuestoRelacionCampo eprc = new EmpresaPuestoRelacionCampo();
		String columna = "";

		// mientras exista registros

		if (numero_columnas_excel == (numero_columnas_rs)) {
			for (int i = 5; i < numOfRows; i++) {
				try {
					columna = "";
					rs.addNew();
					Cell[] arreglo_cell = new Cell[(numero_columnas_rs)];
					Cell aux = null;
					// Iterador 0 Nombre
					int iterador = 0;
					aux = sheet.getCell(iterador, i);
					if ((aux.getContents() == null || aux.getContents().equals(
							""))) {
						columna += "_ERROR!!! en  Fila: " + (i + 1)
								+ " Columna: " + iterador + " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Valor: " + aux.getContents() + "--";
						rsError.addNew();
						rsError.setValue("columna",
								this.nombre_campos_empresa_puesto[iterador]);
						rsError.setValue("fila", i);
						rsError.setValue("error",
								"Este campo debe estar lleno y esta vacio.");
						throw new Throwable("Campo vacio: " + columna);
					} else {

						if ((id_empresa == 0)
								|| (id_empresa != id_empresa_variacion)) {
							Recordset rsIdEmpresa = new Recordset();
							this.getDb().beginTrans();
							rsIdEmpresa = this.getDb().get(
									"Select empresa_id from empresa where upper(nombre_empresa) LIKE upper('%"
											+ aux.getContents() + "%');", 1);
							rsIdEmpresa.first();
							id_empresa = rsIdEmpresa.getInt("empresa_id");
							id_empresa_variacion = id_empresa;
							columna += "_Exito  sFila: "
									+ (i + 1)
									+ " Columna: "
									+ iterador
									+ " Nombre: "
									+ this.nombre_campos_empresa_puesto[iterador]
									+ " Es mandatorio? "
									+ eprc.esMandatorio(this.nombre_campos_empresa_puesto[iterador])
									+ " Valor: " + aux.getContents() + "--";
						}
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								id_empresa);
						arreglo_cell[iterador] = aux;

					}
					// Iterador 1 Encadenado
					iterador++;
					aux = sheet.getCell(iterador, i);
					if ((aux.getContents() == null || aux.getContents().equals(
							""))) {
						columna += "_ERROR!!! en  Fila: " + (i + 1)
								+ " Columna: " + iterador + " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Valor: " + aux.getContents() + "--";
						rsError.addNew();
						rsError.setValue("columna",
								this.nombre_campos_empresa_puesto[iterador]);
						rsError.setValue("fila", i);
						rsError.setValue("error",
								"Este campo debe estar lleno y esta vacio.");
						throw new Throwable("Campo vacio: " + columna);
					} else {
						columna += "_Exito  Fila: "
								+ (i + 1)
								+ " Columna: "
								+ iterador
								+ " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Es mandatorio? "
								+ eprc.esMandatorio(this.nombre_campos_empresa_puesto[iterador])
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								aux.getContents());
						arreglo_cell[iterador] = aux;
					}
					// Iterador 2 Codigo
					iterador++;
					aux = sheet.getCell(iterador, i);
					if ((aux.getContents() == null || aux.getContents().equals(
							""))) {
						columna += "_ERROR!!! en  Fila: " + (i + 1)
								+ " Columna: " + iterador + " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Valor: " + aux.getContents() + "--";
						rsError.addNew();
						rsError.setValue("columna",
								this.nombre_campos_empresa_puesto[iterador]);
						rsError.setValue("fila", i);
						rsError.setValue("error",
								"Este campo debe estar lleno y esta vacio.");
						throw new Throwable("Campo vacio: " + columna);
					} else {
						columna += "_Exito  Fila: "
								+ (i + 1)
								+ " Columna: "
								+ iterador
								+ " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Es mandatorio? "
								+ eprc.esMandatorio(this.nombre_campos_empresa_puesto[iterador])
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								aux.getContents());
						arreglo_cell[iterador] = aux;
					}

					// Iterador 3 Nivel
					iterador++;
					aux = sheet.getCell(iterador, i);
					if ((aux.getContents() == null || aux.getContents().equals(
							""))) {
						columna += "_ERROR!!! en  Fila: " + (i + 1)
								+ " Columna: " + iterador + " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Valor: " + aux.getContents() + "--";
						rsError.addNew();
						rsError.setValue("columna",
								this.nombre_campos_empresa_puesto[iterador]);
						rsError.setValue("fila", i);
						rsError.setValue("error",
								"Este campo debe estar lleno y esta vacio.");
						throw new Throwable("Campo vacio: " + columna);
					} else {
						columna += "_Exito  Fila: "
								+ (i + 1)
								+ " Columna: "
								+ iterador
								+ " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Es mandatorio? "
								+ eprc.esMandatorio(this.nombre_campos_empresa_puesto[iterador])
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								aux.getContents());
						arreglo_cell[iterador] = aux;
					}

					// Iterador 4 Modificador
					// //////////////////OPCIONAL
					iterador++;
					aux = sheet.getCell(iterador, i);
					if ((aux.getContents() == null || aux.getContents().equals(
							""))) {
						columna += "_opcional!!! en  Fila: " + (i + 1)
								+ " Columna: " + iterador + " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador], "");
						arreglo_cell[iterador] = aux;
					} else {
						columna += "_Exito  Fila: "
								+ (i + 1)
								+ " Columna: "
								+ iterador
								+ " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Es mandatorio? "
								+ eprc.esMandatorio(this.nombre_campos_empresa_puesto[iterador])
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								aux.getContents());
						arreglo_cell[iterador] = aux;
					}
					// /////////////////////////////////

					// Iterador 5 Familia
					iterador++;
					aux = sheet.getCell(iterador, i);
					if ((aux.getContents() == null || aux.getContents().equals(
							""))) {
						columna += "_ERROR!!! en  Fila: " + (i + 1)
								+ " Columna: " + iterador + " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Valor: " + aux.getContents() + "--";
						rsError.addNew();
						rsError.setValue("columna",
								this.nombre_campos_empresa_puesto[iterador]);
						rsError.setValue("fila", i);
						rsError.setValue("error",
								"Este campo debe estar lleno y esta vacio.");
						throw new Throwable("Campo vacio: " + columna);
					} else {
						columna += "_Exito  Fila: " + (i + 1) + " Columna: "
								+ iterador + " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								aux.getContents());
						arreglo_cell[iterador] = aux;
					}
					// Iterador 6 Titulo del puesto
					iterador++;
					aux = sheet.getCell(iterador, i);
					if ((aux.getContents() == null || aux.getContents().equals(
							""))) {
						columna += "_ERROR!!! en  Fila: " + (i + 1)
								+ " Columna: " + iterador + " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Valor: " + aux.getContents() + "--";
						rsError.addNew();
						rsError.setValue("columna",
								this.nombre_campos_empresa_puesto[iterador]);
						rsError.setValue("fila", i);
						rsError.setValue("error",
								"Este campo debe estar lleno y esta vacio.");
						throw new Throwable("Campo vacio: " + columna);
					} else {
						columna += "_Exito  Fila: "
								+ (i + 1)
								+ " Columna: "
								+ iterador
								+ " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Es mandatorio? "
								+ eprc.esMandatorio(this.nombre_campos_empresa_puesto[iterador])
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								aux.getContents());
						arreglo_cell[iterador] = aux;
					}

					// ///////// OPCIONAL 8 - 11

					// Iterador 7 SBM
					iterador++;
					aux = sheet.getCell(iterador, i);
					if ((aux.getContents() == null || aux.getContents().equals(
							""))) {
						columna += "_ERROR!!! en  Fila: " + (i + 1)
								+ " Columna: " + iterador + " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Valor: " + aux.getContents() + "--";
						rsError.addNew();
						rsError.setValue("columna",
								this.nombre_campos_empresa_puesto[iterador]);
						rsError.setValue("fila", i);
						rsError.setValue("error",
								"Este campo debe estar lleno y esta vacio.");
						throw new Throwable("Campo vacio: " + columna);
					} else {
						double sbm = 0;

						concoma = aux.getContents();
						concoma = concoma.replace(',', '.');
						number = format.parse(concoma);
						sbm = number.doubleValue();

						columna += "_Exito  Fila: "
								+ (i + 1)
								+ " Columna: "
								+ iterador
								+ " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Es mandatorio? "
								+ eprc.esMandatorio(this.nombre_campos_empresa_puesto[iterador])
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								sbm);
						arreglo_cell[iterador] = aux;
					}

					// Iterador 8 Comisiones
					iterador++;
					aux = sheet.getCell(iterador, i);
					double comisiones = 0;
					if ((aux.getContents() == null || aux.getContents().equals(
							""))) {
						columna += "_ERROR!!! en  Fila: " + (i + 1)
								+ " Columna: " + iterador + " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								0.0);
					} else {
						concoma = aux.getContents();
						concoma = concoma.replace(',', '.');
						number = format.parse(concoma);
						comisiones = number.doubleValue();

						columna += "_Exito  Fila: "
								+ (i + 1)
								+ " Columna: "
								+ iterador
								+ " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Es mandatorio? "
								+ eprc.esMandatorio(this.nombre_campos_empresa_puesto[iterador])
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								comisiones);
						arreglo_cell[iterador] = aux;
					}
					// Iterador 9 Fondo de ahorro extra
					iterador++;
					aux = sheet.getCell(iterador, i);
					double fondo_ahorro_extra = 0;
					if ((aux.getContents() == null || aux.getContents().equals(
							""))) {
						columna += "_ERROR!!! en  Fila: " + (i + 1)
								+ " Columna: " + iterador + " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								0.0);
					} else {
						concoma = aux.getContents();
						concoma = concoma.replace(',', '.');
						number = format.parse(concoma);
						fondo_ahorro_extra = number.doubleValue();

						columna += "_Exito  Fila: "
								+ (i + 1)
								+ " Columna: "
								+ iterador
								+ " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Es mandatorio? "
								+ eprc.esMandatorio(this.nombre_campos_empresa_puesto[iterador])
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								fondo_ahorro_extra);
						arreglo_cell[iterador] = aux;
					}
					// Iterador 10 Bonos Variables
					iterador++;
					aux = sheet.getCell(iterador, i);
					double bonos_variables = 0;
					if ((aux.getContents() == null || aux.getContents().equals(
							""))) {
						columna += "_ERROR!!! en  Fila: " + (i + 1)
								+ " Columna: " + iterador + " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								0.0);
					} else {
						concoma = aux.getContents();
						concoma = concoma.replace(',', '.');
						number = format.parse(concoma);
						bonos_variables = number.doubleValue();
						columna += "_Exito  Fila: "
								+ (i + 1)
								+ " Columna: "
								+ iterador
								+ " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Es mandatorio? "
								+ eprc.esMandatorio(this.nombre_campos_empresa_puesto[iterador])
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								bonos_variables);
						arreglo_cell[iterador] = aux;
					}
					// /////////////////////////
					// Iterador 11 Fondo de ahorro ordinario
					iterador++;
					aux = sheet.getCell(iterador, i);
					double fondo_ahorro_ordinario = 0;
					if ((aux.getContents() == null || aux.getContents().equals(
							""))) {
						columna += "_ERROR!!! en  Fila: " + (i + 1)
								+ " Columna: " + iterador + " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								0.0);
						arreglo_cell[iterador] = aux;

					} else {
						concoma = aux.getContents();
						concoma = concoma.replace(',', '.');
						number = format.parse(concoma);
						fondo_ahorro_ordinario = number.doubleValue();
						columna += "_Exito  Fila: "
								+ (i + 1)
								+ " Columna: "
								+ iterador
								+ " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Es mandatorio? "
								+ eprc.esMandatorio(this.nombre_campos_empresa_puesto[iterador])
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								fondo_ahorro_ordinario);
						arreglo_cell[iterador] = aux;
					}
					// ///////// FIN DE LOS OPCIONAL 8 - 11
					// Iterador 12 Asignacion no salarial
					iterador++;
					aux = sheet.getCell(iterador, i);
					double asignacion_no_salarial = 0;
					if ((aux.getContents() == null || aux.getContents().equals(
							""))) {
						columna += "_ERROR!!! en  Fila: " + (i + 1)
								+ " Columna: " + iterador + " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								0.0);
					} else {
						concoma = aux.getContents();
						concoma = concoma.replace(',', '.');
						number = format.parse(concoma);
						asignacion_no_salarial = number.doubleValue();
						aux = sheet.getCell(iterador, i);
						columna += "_Exito  Fila: "
								+ (i + 1)
								+ " Columna: "
								+ iterador
								+ " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Es mandatorio? "
								+ eprc.esMandatorio(this.nombre_campos_empresa_puesto[iterador])
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								asignacion_no_salarial);
						arreglo_cell[iterador] = aux;
					}
					// Iterador 13 Asignacion de vehiculo
					iterador++;
					aux = sheet.getCell(iterador, i);
					double asignacion_vehiculo = 0;
					if ((aux.getContents() == null || aux.getContents().equals(
							""))) {
						columna += "_ERROR!!! en  Fila: " + (i + 1)
								+ " Columna: " + iterador + " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								0.0);
					} else {
						concoma = aux.getContents();
						concoma = concoma.replace(',', '.');
						number = format.parse(concoma);
						asignacion_vehiculo = number.doubleValue();
						columna += "_Exito  Fila: "
								+ (i + 1)
								+ " Columna: "
								+ iterador
								+ " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Es mandatorio? "
								+ eprc.esMandatorio(this.nombre_campos_empresa_puesto[iterador])
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								asignacion_vehiculo);
						arreglo_cell[iterador] = aux;
					}
					// Iterador 14 Añor de ingreso
					iterador++;
					aux = sheet.getCell(iterador, i);
					if ((aux.getContents() == null || aux.getContents().equals(
							""))) {
						columna += "_ERROR!!! en  Fila: " + (i + 1)
								+ " Columna: " + iterador + " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Valor: " + aux.getContents() + "--";
						rsError.setValue("columna",
								this.nombre_campos_empresa_puesto[iterador]);
						rsError.setValue("fila", i);
						rsError.setValue("error",
								"Este campo debe estar lleno y esta vacio.");
						throw new Throwable("Campo vacio: " + columna);
					} else {
						columna += "_Exito  Fila: "
								+ (i + 1)
								+ " Columna: "
								+ iterador
								+ " Nombre: "
								+ this.nombre_campos_empresa_puesto[iterador]
								+ " Es mandatorio? "
								+ eprc.esMandatorio(this.nombre_campos_empresa_puesto[iterador])
								+ " Valor: " + aux.getContents() + "--";
						rs.setValue(
								this.nombre_campos_empresa_puesto[iterador],
								(StringUtil.getDateObject(
										(aux.getContents() + "-01-01"),
										"yyyy-MM-dd")));
						arreglo_cell[iterador] = aux;

					}
					// ///////////////////////// Fin
				} catch (Throwable a) {
					// añadir un nuevo record
					rsError.addNew();
					rsError.setValue("columna", columna);
					rsError.setValue("fila", (i + 1));
					rsError.setValue("error", a.getMessage());
					// registrar el numero de errores
					inputParams.setValue("total_errores",
							new Integer(rsError.getRecordCount()));

					if (true) {
						// throw new Throwable(columna);
						throw new Throwable("BigError en la columna "
								+ rsError.getString("fila")
								+ rsError.getString("columna")
								+ rsError.getString("error"));
					}

				}
			}
			// Si llego hasta aqui el archivo Excel tiene el formato correcto
		} else {
			// El archivo Excel no tene el formato correcto
			// recordset de errores tiene registro?
			if (rsError.getRecordCount() > 0) {
				getSession().setAttribute("error.excel", rsError);
				throw new Throwable("El archivo de Excel contiene errores.");
			}
			throw new Throwable(
					"El archivo de Excel no contiene el formato especifícado.");
		}
		System.out
				.print("Funcionaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		System.out.print(rs.getRecordCount());
		rs.top();
		int contador_aux = 0;
		while (rs.next()) {
			System.out.print(rs.getString(this.nombre_campos_empresa_puesto[0])
					+ " | ");
			System.out.print(rs.getString(this.nombre_campos_empresa_puesto[1])
					+ " | ");
			System.out.print(rs.getString(this.nombre_campos_empresa_puesto[2])
					+ " | ");
			System.out.print(rs.getString(this.nombre_campos_empresa_puesto[3])
					+ " | ");
			System.out.print(rs.getString(this.nombre_campos_empresa_puesto[4])
					+ " | ");
			System.out.print(rs.getString(this.nombre_campos_empresa_puesto[5])
					+ " | ");
			System.out.print(rs.getString(this.nombre_campos_empresa_puesto[6])
					+ " | ");
			System.out.print(rs.getString(this.nombre_campos_empresa_puesto[7])
					+ " | ");
			System.out.print(rs.getString(this.nombre_campos_empresa_puesto[8])
					+ " | ");
			System.out.print(rs.getString(this.nombre_campos_empresa_puesto[9])
					+ " | ");
			System.out.print(rs
					.getString(this.nombre_campos_empresa_puesto[10]) + " | ");
			System.out.print(rs
					.getString(this.nombre_campos_empresa_puesto[11]) + " | ");
			System.out.print(rs
					.getString(this.nombre_campos_empresa_puesto[12]) + " | ");
			System.out.print(rs
					.getString(this.nombre_campos_empresa_puesto[13]) + " | ");
			System.out.print(rs
					.getString(this.nombre_campos_empresa_puesto[14]) + " | ");
			System.out.println();
		}
		// registrar el numero de registros insertados
		inputParams.setValue("total_registros",
				new Integer(rs.getRecordCount()));
		// dar inicio a la transacción
		getDb().beginTrans();
		String query = getResource("insert.sql");
		String[] params = { "fk_empresa_id", "codigo_completo",
				"codigo_disciplina", "codigo_nivel", "codigo_diferenciador",
				"codigo_area", "titulo_puesto", "sbm", "comision_target",
				"fondo_ahorro_extra", "bono_variable",
				"fondo_ahorro_ordinario", "asignacion_no_salarial",
				"asignacion_vehiculo", "fecha_ingreso" };

		getDb().execBatch(query, rs, params);
		// getDb().commit();
		// ejecutar el insert de lotes
		// getDb().exec(getSQL(getResource("insert_lote.sql"), inputParams));

		// definir parametros
		/*
		 * String[] params = { "userlogin", "passwd", "lname", "fname", "email",
		 * "id_role" };
		 * 
		 * //obtener el Resource String sql = getSQL(getResource("insert.sql"),
		 * inputParams);
		 * 
		 * //ejecutar en Batch getDb().execBatch(sql, rs, params);
		 */

		// colocar en sesion el recordset
		// getSession().setAttribute("query.sql", rs1);
		this.getDb().commit();

		return rc;
	}

	public void ActualizaPromedio() throws Throwable {
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
		rs_promedio = new Recordset();
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
						rs_promedio = null;
						rs_promedio = new Recordset();
						rs_promedio.append("sector_fk", java.sql.Types.INTEGER);
						rs_promedio.append("tamano_empresa_fk",
								java.sql.Types.INTEGER);
						rs_promedio.append("cargo_fk", java.sql.Types.INTEGER);
						rs_promedio.append("valor", java.sql.Types.DOUBLE);
						rs_promedio.addNew();
						rs_promedio.setValue("sector_fk",
								new Integer(rs_sector.getString("sector_id")));
						rs_promedio.setValue("tamano_empresa_fk", new Integer(
								rs_empresa.getString("empresa_id")));
						rs_promedio.setValue("cargo_fk",
								new Integer(rs_cargo.getString("cargo_id")));
						rs_promedio.setValue("valor", promedio);

						String[] campos = { "sector_fk", "tamano_empresa_fk",
								"cargo_fk", "valor" };
						getDb().execBatch(getResource("insert_promedio.sql"),
								rs_promedio, campos);
					}
				}
			}
		}
	}
}
