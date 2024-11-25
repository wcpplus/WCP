package com.farm.doc.tag;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.farm.core.auth.domain.LoginUser;
import com.farm.doc.server.FarmDocgroupManagerInter;
import com.farm.util.spring.BeanFactory;
import com.farm.web.constant.FarmConstant;

public class DocGroupOption extends TagSupport {
	/**
	 * 
	 */
	private String aroundS;
	private String aroundE;
	private static final long serialVersionUID = 1895493540131375513L;
	private final static FarmDocgroupManagerInter docGroupIMP = (FarmDocgroupManagerInter) BeanFactory
			.getBean("farmDocgroupManagerImpl");
	private static final Logger log = Logger.getLogger(DocGroupOption.class);
	@SuppressWarnings("unchecked")
	@Override
	public int doEndTag() throws JspException {

		HttpServletRequest request = (HttpServletRequest) super.pageContext
				.getRequest();
		LoginUser user = (LoginUser) request.getSession().getAttribute(
				FarmConstant.SESSION_USEROBJ);
		List<Map<String, Object>> list = docGroupIMP.getEditorGroupByUser(user
				.getId());
		JspWriter jspw = this.pageContext.getOut();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Map<String, String> type = (Map<String, String>) iterator.next();
			try {
				jspw.println("<option value='" + type.get("ID") + "'>"
						+ (aroundS != null ? aroundS : "") + type.get("NAME")
						+ (aroundE != null ? aroundE : "") + "</option>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
		}
		return 0;
	}

	@Override
	public int doStartTag() throws JspException {

		return 0;
	}

	public String getAroundS() {
		return aroundS;
	}

	public void setAroundS(String aroundS) {
		this.aroundS = aroundS;
	}

	public String getAroundE() {
		return aroundE;
	}

	public void setAroundE(String aroundE) {
		this.aroundE = aroundE;
	}

}
