package com.farm.doc.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.farm.doc.domain.ex.DocEntire;
import com.farm.doc.server.FarmDocManagerInter;
import com.farm.util.spring.BeanFactory;

public class DocContent extends TagSupport {
	/**
	 * 
	 */
	private String id;
	private final static FarmDocManagerInter aloneIMP = (FarmDocManagerInter) BeanFactory
			.getBean("farmDocManagerImpl");
	private static final long serialVersionUID = 1895493540131375513L;

	@SuppressWarnings("deprecation")
	@Override
	public int doEndTag() throws JspException {
		JspWriter jspw = this.pageContext.getOut();
		try {
			DocEntire doc = aloneIMP.getDoc(id);
			jspw
					.println(doc.getTexts().getText1()
							+ (doc.getTexts().getText2() != null ? doc
							.getTexts().getText2() : "")
							+ (doc.getTexts().getText3() != null ? doc
							.getTexts().getText3() : ""));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int doStartTag() throws JspException {

		return 0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
