<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div>
	<div
		style="font-weight: 600; font-size: 20px; letter-spacing: -0.35px; color: #D9534F;">
		<i class=" glyphicon glyphicon-unchecked"></i>
		知识分类
	</div>
</div>
<div class="row"
	style="background-color: #ffffff; border: 1px solid #eee; border-radius: 5px; padding: 20px; padding-top: 4px; margin: 0px; margin-top: 20px;">
	<c:forEach items="${typesons}" var="node">
		<c:if test="${node.parentid=='NONE'}">
			<div class="col-sm-3 " style="margin-top: 20px;">
				<div style="font-size: 14px;">
					<a href="webtype/view${node.id}/Pub1.html?typeDomainId=${node.id}">
						${node.name} </a>
					<c:if test="${node.num>0}">
						<span style="color: #D9534F; font-weight: bold; font-size: 13px;">
							&nbsp;${node.num}
						</span>
					</c:if>
				</div>
			</div>
		</c:if>
	</c:forEach>
</div>