package domain.mmcontrolclass;

import java.util.Arrays;

public class EmpresaPuestoRelacionCampo {
	String[] nombre_campos_empresa_puesto_obligatorio;
	String[] nombre_campos_empresa_puesto_opcional;
	String[] nombre_campos_recoleccion_excel;

	public String toString() {
		String aux = "";
		for (String cepo : this.nombre_campos_empresa_puesto_obligatorio) {
			if (cepo != (null))
				aux += cepo;
		}
		for (String cepo : this.nombre_campos_empresa_puesto_opcional) {
			if (cepo != (null))
				aux += cepo;
		}
		return aux;
	}

	public void addElemento(String nombre, Boolean mandatorio) {
		if (mandatorio) {
			return;
		} else {
			return;
		}
	}

	public String[] getNombreCamposEmpresaPuesto() {
		String[] aux = new String[15];
		for (String cepo : this.nombre_campos_empresa_puesto_obligatorio) {
			if (cepo != (null))
				aux[Arrays
						.asList(this.nombre_campos_empresa_puesto_obligatorio)
						.indexOf(cepo)] = cepo;
		}
		for (String cepo : this.nombre_campos_empresa_puesto_opcional) {
			if (cepo != (null))
				aux[Arrays.asList(this.nombre_campos_empresa_puesto_opcional)
						.indexOf(cepo)] = cepo;
		}
		return aux;
	}

	public Boolean esMandatorio(String elemento) {
		for (String cepo : this.nombre_campos_empresa_puesto_obligatorio) {
			if (cepo != (null))
				if (cepo.equals(elemento))
					return true;
		}
		for (String cepo : this.nombre_campos_empresa_puesto_opcional) {
			if (cepo != (null))
				if (cepo.equals(elemento))
					return false;
		}
		return false;
	}

	public EmpresaPuestoRelacionCampo() {
		// defino el numero de campos que va a tener el arreglo basado en la
		// tabla
		// empresa_puesto
		this.nombre_campos_empresa_puesto_obligatorio = new String[15];
		this.nombre_campos_empresa_puesto_opcional = new String[15];

		this.nombre_campos_empresa_puesto_obligatorio[0] = "fk_empresa_id";
		this.nombre_campos_empresa_puesto_obligatorio[1] = "codigo_nivel_modificador";
		this.nombre_campos_empresa_puesto_obligatorio[2] = "codigo_area";
		this.nombre_campos_empresa_puesto_obligatorio[3] = "codigo_nivel";
			
			
		this.nombre_campos_empresa_puesto_opcional[4] = "codigo_diferenciador";
		
		
		this.nombre_campos_empresa_puesto_obligatorio[5] = "area_fk";
		this.nombre_campos_empresa_puesto_obligatorio[6] = "titulo_puesto";
		this.nombre_campos_empresa_puesto_obligatorio[7] = "sbm";
		
		
		this.nombre_campos_empresa_puesto_opcional[8] = "comision_target";
		this.nombre_campos_empresa_puesto_opcional[9] = "fondo_ahorro_extra";
		this.nombre_campos_empresa_puesto_opcional[10] = "bono_variable";
		this.nombre_campos_empresa_puesto_opcional[11] = "fondo_ahorro_ordinario";
		
		
		this.nombre_campos_empresa_puesto_obligatorio[12] = "asignacion_no_salarial";
		
		
		this.nombre_campos_empresa_puesto_opcional[13] = "asignacion_vehiculo";
		this.nombre_campos_empresa_puesto_opcional[14] = "fecha_ingreso";
	}

}
