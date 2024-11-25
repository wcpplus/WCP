<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib uri="/view/conf/farmdoc.tld" prefix="DOC"%>
<div class="row column_box">
	<div class="col-sm-12">
		<span class="glyphicon glyphicon-star  column_title">常用知识 </span>
	</div>
</div>
<div class="row side_column">
	<div class="col-sm-12">
		<ul style="margin-left: -50px;">
			<c:forEach items="${hotdocs}" varStatus="status" var="node">
				<li 
					style="line-height: 30px;   width: 100%; margin-left: 24px;"><a
					href="webdoc/view/Pub${node.docid}.html"><DOC:Describe maxnum="50" text="${node.title} "></DOC:Describe> </a></li>
			</c:forEach>
		</ul>
	</div>
</div>