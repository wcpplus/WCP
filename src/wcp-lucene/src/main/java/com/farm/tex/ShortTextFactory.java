package com.farm.tex;

import com.farm.tex.service.ShorttextServiceInter;
import com.farm.util.spring.BeanFactory;

public class ShortTextFactory {

	/**
	 * 获得服务实例
	 * 
	 * @return
	 */
	public static ShorttextServiceInter getInctance() {
		return (ShorttextServiceInter)BeanFactory.getBean("shorttextServiceImpl");
	}
}
