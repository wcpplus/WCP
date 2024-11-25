package com.farm.web.init;

import java.text.DecimalFormat;

import javax.servlet.ServletContext;

import com.farm.parameter.service.impl.ConstantVarService;
import com.farm.parameter.service.impl.PropertiesFileService;
import com.farm.web.constant.FarmConstant;
import com.farm.web.task.ServletInitJobInter;

public class InitParameter implements ServletInitJobInter {

	@Override
	public void execute(ServletContext context) {
		// 注册常量
		ConstantVarService.registConstant("farm.constant.session.key.logintime.", FarmConstant.SESSION_LOGINTIME);
		ConstantVarService.registConstant("farm.constant.session.key.current.org", FarmConstant.SESSION_ORG);
		ConstantVarService.registConstant("farm.constant.session.key.current.roles", FarmConstant.SESSION_ROLES);
		ConstantVarService.registConstant("farm.constant.session.key.current.actions", FarmConstant.SESSION_USERACTION);
		ConstantVarService.registConstant("farm.constant.session.key.current.menu", FarmConstant.SESSION_USERMENU);
		ConstantVarService.registConstant("farm.constant.session.key.current.user", FarmConstant.SESSION_USEROBJ);
		ConstantVarService.registConstant("farm.constant.session.key.current.userphoto",
				FarmConstant.SESSION_USERPHOTO);
		ConstantVarService.registConstant("farm.constant.session.key.go.url", FarmConstant.SESSION_GO_URL);
		ConstantVarService.registConstant("farm.constant.session.key.from.url", FarmConstant.SESSION_FROM_URL);
		ConstantVarService.registConstant("farm.constant.app.treecodelen",
				String.valueOf(FarmConstant.MENU_TREECODE_UNIT_LENGTH));
		ConstantVarService.registConstant("farm.constant.webroot.path", context.getRealPath(""));
		// 注册配置文件
		PropertiesFileService.registConstant("config");
		PropertiesFileService.registConstant("llm");
		PropertiesFileService.registConstant("wda");
		PropertiesFileService.registConstant("indexConfig");
		PropertiesFileService.registConstant("jdbc");
		PropertiesFileService.registConstant("document");
		PropertiesFileService.registConstant("cache");
		PropertiesFileService.registConstant("webapp");
		PropertiesFileService.registConstant("i18n");
		PropertiesFileService.registConstant("about");
		PropertiesFileService.registConstant("rmi");
		PropertiesFileService.registConstant("email");
		memory();
	}
	private static void memory() {
		DecimalFormat df = new DecimalFormat("0.00");
		// Display the total amount of memory in the Java virtual machine.
		long totalMem = Runtime.getRuntime().totalMemory();
		ConstantVarService.registConstant("farm.jvm.memory.total", df.format(totalMem/1024/1024) + " MB");
		// Display the maximum amount of memory that the Java virtual machine
		// will attempt to use.
		long maxMem = Runtime.getRuntime().maxMemory();
		ConstantVarService.registConstant("farm.jvm.memory.max", df.format(maxMem/1024/1024) + " MB");
		// Display the amount of free memory in the Java Virtual Machine.
		long freeMem = Runtime.getRuntime().freeMemory();
		ConstantVarService.registConstant("farm.jvm.memory.free", df.format(freeMem/1024/1024) + " MB");
	}
}
