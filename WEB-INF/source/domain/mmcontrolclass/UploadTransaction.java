package domain.mmcontrolclass;

import java.io.File;
import dinamica.GenericTransaction;
import dinamica.Recordset;

/**
 * Dejar un blob grabado como archivo temporal y publicar un recordset con los datos
 * del blob para retornarlo al cliente (ruta del temporal, nombre original, tamaño y formato),
 * de tal manera que estos datos puedan ser luego posteados con el resto del formulario via Ajax. 
 * Asume que el campo que contiene al blob se llama "file".<br>
 * Debe activar el filtro y el mapping al Action en cuestión en web.xml para esto funcione.<br>
 * El validator.xml de un action que use esta clase se verá así:<br><br>
 * <xmp>
 * <?xml version='1.0' encoding='ISO-8859-1'?>
 * <validator>
 * 	<parameter id="file" type="varchar" required="false" label="Archivo temporal" maxlen="500"/>
 * 	<parameter id="file.content-type" type="varchar" required="false" label="Formato" maxlen="150"/>
 * 	<parameter id="file.filename" type="varchar" required="false" label="Archivo" maxlen="400"/>
 * 	<parameter id="image_size" type="integer" required="false" label="Tamaño del archivo"/>
 * </validator>
 * </xmp>
 * @author mcordova
 *
 */
public class UploadTransaction extends GenericTransaction 
{

	public int service(Recordset inputParams) throws Throwable
	{
		int rc = super.service(inputParams);
		
		//validar si la ruta representa un archivo
		if (inputParams.isNull("file.filename"))
			throw new Throwable("No se puede cargar el archivo, bien sea porque la ruta no es válida o porque la carga no está activada en la configuración de la aplicación (web.xml)");
		
		//patch 2010-09-02: get real content-type
		String mimeType = getContext().getMimeType(inputParams.getString("file.filename").toLowerCase());
		if (mimeType!=null)
			inputParams.setValue("file.content-type", mimeType);		
		
		//get temp file
		String path = (String)inputParams.getValue("file");
		File f = new File(path);
		
		//get file size
		Integer size = new Integer((int)f.length()); 
		inputParams.setValue("image_size", size);

		if (size.intValue()==0)
			throw new Throwable("¡No se puede cargar un archivo vacío!");
		
		return rc;
		
	}
	
}
