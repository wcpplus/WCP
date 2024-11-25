package com.farm.doc.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class DocDescribe extends TagSupport {
	/**
	 * 
	 */
	private String text;
	private int maxnum;
	private static final long serialVersionUID = 1895493540131375513L;

	@Override
	public int doEndTag() throws JspException {
		JspWriter jspw = this.pageContext.getOut();
		try {
			if (text != null && text.trim().length() > 0) {
				jspw.println(text.trim().length() > maxnum ? text.substring(0,
						maxnum)
						+ "..." : text);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int doStartTag() throws JspException {

		return 0;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getMaxnum() {
		return maxnum;
	}

	public void setMaxnum(int maxnum) {
		this.maxnum = maxnum;
	}

}
