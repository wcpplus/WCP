<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div class="row">
	<div class="col-sm-12">
		<c:forEach var="node" items="${typesons}">
			<c:if test="${node.parentid==typeid}">
				<h4 style="float: left; margin: 4px;">
					<a style="color: #428BCA;" href="webtype/view${node.id}/Pub1.html"> ${node.name} </a>
					<c:if test="${node.num>0}">
						<span style="color: #D9534F; font-weight: bold;">${node.num}</span>
					</c:if>
				</h4>
			</c:if>
		</c:forEach>
	</div>
</div>