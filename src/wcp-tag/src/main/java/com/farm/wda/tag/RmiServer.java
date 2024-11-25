package com.farm.wda.tag;

import java.rmi.Naming;
import com.farm.parameter.FarmParameterService;
import com.farm.wda.inter.WdaAppInter;

public class RmiServer {
	private static WdaAppInter personService = null;

	public static WdaAppInter getInstance() {
		try {
			if (personService == null) {
				personService = (WdaAppInter) Naming
						.lookup(FarmParameterService.getInstance().getParameter("config.wda.rmi.url"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return personService;
	}
}
