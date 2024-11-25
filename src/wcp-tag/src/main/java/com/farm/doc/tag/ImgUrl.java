package com.farm.doc.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.farm.doc.server.FarmFileManagerInter;
import com.farm.util.spring.BeanFactory;

public class ImgUrl extends TagSupport {
	/**
	 * 
	 */
	private String fileid;
	private final static FarmFileManagerInter aloneIMP = (FarmFileManagerInter) BeanFactory
			.getBean("farmFileManagerImpl");
	private static final long serialVersionUID = 1895493540131375513L;

	@Override
	public int doEndTag() throws JspException {
		JspWriter jspw = this.pageContext.getOut();
		try {
			jspw.println(aloneIMP.getFileURL(fileid));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int doStartTag() throws JspException {

		return 0;
	}

	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}
}
