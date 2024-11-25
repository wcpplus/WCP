<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!-- Single button -->
<div class="btn-group hidden-xs" style="float: right;">
	<button type="button" class="btn btn-default btn-sm dropdown-toggle" style="margin-left: 4px;"
		data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
		创建知识 <span class="caret"></span>
	</button>
	<ul class="dropdown-menu">
		<li><a href="know/add.do?typeid=${typeid}&groupid=${groupid}">创建文档知识</a></li>
		<li><a
			href="know/webdown.do?typeid=${typeid}&groupid=${groupid}">下载网页知识
		</a></li>
		<!-- 
		<li><a
			href="index/webSiteshowPage.htm?typeid=${typeid}&docgroup=${groupid}">上传HTML站点
		</a></li> -->
		<li><a href="webfile/add.do?typeid=${typeid}&groupid=${groupid}">上传文件资源
		</a></li>
	</ul>
</div>
