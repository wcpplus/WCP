package com.farm.core.sql.query;

import com.farm.core.Context;
import com.farm.core.auth.util.KeyUtil;

public class CoreHandle {
	public static boolean runLce() {
		Context.MK = KeyUtil.getMKey();
		return true;
	}
}
