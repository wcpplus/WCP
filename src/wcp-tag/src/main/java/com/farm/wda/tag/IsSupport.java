package com.farm.wda.tag;

import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.farm.doc.domain.FarmDocfile;
import com.farm.doc.server.FarmFileManagerInter;
import com.farm.parameter.FarmParameterService;
import com.farm.util.spring.BeanFactory;

public class IsSupport extends TagSupport {
	private String fileid;
	static final Logger log = Logger.getLogger(IsSupport.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static FarmFileManagerInter aloneIMP = (FarmFileManagerInter) BeanFactory
			.getBean("farmFileManagerImpl");

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws JspException {
		// EVAL_BODY_INCLUDE
		// 则执行自定义标签的标签体；
		// 返回SKIP_BODY则忽略自定义标签的标签体，直接解释执行自定义标签的结果标记。
		if (!FarmParameterService.getInstance().getParameter("config.wda.rmi.state").equals("true")) {
			return SKIP_BODY;
		}
		FarmDocfile file = aloneIMP.getFile(fileid);
		String typename = null;
		if (file.getExname() != null && !file.getExname().isEmpty()) {
			typename = file.getExname().replace(".", "");
		}
		Set<String> types = null;
		try {
			types = RmiServer.getInstance().getSupportTypes();
		} catch (Exception e) {
			log.error(e.getMessage());
			return SKIP_BODY;
		}
		if (types.contains(typename)) {
			return EVAL_BODY_INCLUDE;
		} else {
			return SKIP_BODY;
		}
	}

	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}
}
