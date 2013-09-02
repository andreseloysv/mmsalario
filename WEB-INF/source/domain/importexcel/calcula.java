package domain.importexcel;

public class calcula {

	/**
	 * Return el promedio de los numero de un arreglo.
	 */

	private static double promedio(double[] a, int dimension) {
		double sum = 0;
		for (int i = 0; i < dimension; i++) {
			sum += a[i];
		}
		return (sum / dimension);
	}

	/**
	 * Calculod interno de la raiz.
	 */
	private static double calculoInternoRaiz(double[] a,double promedio) {
		int dimension = a.length;
//		double promedio = promedio(a, dimension);
		double sum = 0;
		for (int i = 0; i < dimension; i++) {
			sum += Math.pow((a[i] - promedio), 2);
		}
		return (sum / dimension);
	}

	/**
	 * Calculo de la desviacion estandar.
	 */
	public static double desviacionStandar(double[] a,double promedio) {
		if (a.equals(null))
			return -1;
		else
			return Math.sqrt(calculoInternoRaiz(a, promedio));
	}
}
