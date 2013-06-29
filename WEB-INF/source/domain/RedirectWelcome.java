package domain;

import dinamica.GenericTransaction;
import dinamica.Recordset;

public class RedirectWelcome extends GenericTransaction {

	@Override
	public int service(Recordset inputParams) throws Throwable {

		super.service(inputParams);
		if (this.isUserInRole("adminmaster")) {
			getRequest().setAttribute("ruta",
					"action/security/home/welcome/adminmaster");
		} else {
			if (this.isUserInRole("user")) {
				getRequest().setAttribute("ruta",
						"action/security/home/welcome/user");
			} else {
				if (this.isUserInRole("adminempresarial")) {
					getRequest().setAttribute("ruta",
							"action/security/home/welcome/adminempresarial");
				} else {
					if (this.isUserInRole("gerente")) {
						getRequest().setAttribute("ruta",
								"action/mod_administrador/semaforo");
					} else {
						getRequest().setAttribute("ruta",
								"action/mod_administrador/v_global/form");

						// getRequest().setAttribute("ruta",
						// "action/security/home/welcome/welcome4");

					}

				}
			}
		}
		return 0;
	}
}
