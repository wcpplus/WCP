<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div class="row column_box">
	<div class="col-sm-12">
		<span class="glyphicon glyphicon-star  column_title">分类</span>
	</div>
</div>
<div class="row">
	<div class="col-sm-12">
		<div class="row">
			<c:forEach items="${typesons}" var="node">
				<c:if test="${node.parentid=='NONE'}">
					<div class="col-sm-3 docTypeBox">
						<h1>
							<a href="webtype/view${node.id}/Pub1.html"> ${node.name} <c:if
									test="${node.num>0}">
									<span style="color: #D9534F; font-weight: bold;">${node.num}</span>
								</c:if>
							</a>
						</h1>
						<ul>
							<c:forEach items="${typesons}" var="node1">
								<c:if test="${node1.parentid==node.id}">
									<li><a href="webtype/view${node1.id}/Pub1.html">${node1.name}</a></li>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</c:if>
			</c:forEach>
		</div>
	</div>
</div>