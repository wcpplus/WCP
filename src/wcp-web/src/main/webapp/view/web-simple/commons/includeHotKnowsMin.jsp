<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div class="widget-box shadow-box">
	<div class="title">
		<h3>
			<i class="glyphicon glyphicon-fire"></i> 常用知识
		</h3>
	</div>
	<ul class="box-list" id="hots">
		<c:forEach items="${hotdocs}" varStatus="status" var="node">
			<li><div class="li-out">
					<span class="last"> </span><span class="name"><c:if
							test="${node.domtype=='3'}">
							<i class="glyphicon glyphicon-home"></i>
						</c:if> <c:if test="${node.domtype=='1'}">
							<i class="glyphicon glyphicon-file"></i>
						</c:if> <c:if test="${node.domtype=='4'}">
							<i class="glyphicon glyphicon-bookmark"></i>
						</c:if> <c:if test="${node.domtype=='5'}">
							<i style="color: #d9534f;"
								class="glyphicon glyphicon-download-alt"></i>
						</c:if><a href="webdoc/view/Pub${node.docid}.html">${node.title}</a></span><span class="nums">${node.visitnum}/${node.answeringnum}</span>
				</div></li>
		</c:forEach>
	</ul>
</div>
