package com.farm.wda.tag;

import java.io.IOException;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.farm.doc.domain.FarmDocfile;
import com.farm.doc.server.FarmFileManagerInter;
import com.farm.parameter.FarmParameterService;
import com.farm.util.spring.BeanFactory;
import com.farm.wda.exception.ErrorTypeException;

public class DocWebViewUrl extends TagSupport {
	/**
	 * 
	 */
	private String fileid;
	private String docid;
	private final static FarmFileManagerInter aloneIMP = (FarmFileManagerInter) BeanFactory
			.getBean("farmFileManagerImpl");
	private static final long serialVersionUID = 1895493540131375513L;

	@Override
	public int doEndTag() throws JspException {
		JspWriter jspw = this.pageContext.getOut();
		try {
			FarmDocfile file = aloneIMP.getFile(fileid);
			String typename = null;
			if (file.getExname() != null && !file.getExname().isEmpty()) {
				typename = file.getExname().replace(".", "");
			}
			Set<String> types = RmiServer.getInstance().getSupportTypes();
			if (types.contains(typename)) {
				if (!RmiServer.getInstance().isLoged(file.getId())) {
					RmiServer.getInstance().generateDoc(file.getId(), file.getFile(), typename, file.getName(),docid);
				}
				jspw.println(FarmParameterService.getInstance().getParameter("config.wda.view.url")
						.replaceAll("PARA_KEY", fileid));
			} else {
				jspw.println("");
			}
		} catch (IOException | ErrorTypeException e) {
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

	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}
		
}
