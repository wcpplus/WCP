<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div class="widget-box shadow-box">
	<div class="title">
		<h3>
			<i class="glyphicon glyphicon-lock"></i> 知识小组
		</h3>
	</div>
	<c:forEach items="${groups}" var="node">
		<div class="title">
			<h3>
				<div class="side_unit_group_box">
					<div class="side_unit_info_left_box">
						<c:if test="${node.joincheck=='0'}">
							<a href="webgroup/Pubshow.do?groupid=${node.id}"> <img
								alt="${node.groupname}" class="img-rounded side_unit_group_img"
								src="${node.imgurl}">
							</a>
						</c:if>
						<c:if test="${node.joincheck=='1'}">
							<c:if test="${node.userjoin=='1'}">
								<a href="webgroup/join.do?groupId=${node.id}"><img
									alt="${node.groupname}" class="img-rounded side_unit_group_img"
									src="${node.imgurl}"></a>
							</c:if>
							<c:if test="${node.userjoin=='0'}">
								<img class="img-rounded side_unit_group_img"
									alt="${node.groupname}" src="${node.imgurl}">
							</c:if>

						</c:if>
					</div>
					<div class="side_unit_info_left_box">
						<div class="side_unit_title">
							<c:if test="${node.joincheck=='0'}">
								<a href="webgroup/Pubshow.do?groupid=${node.id}">${node.groupname}</a>
							</c:if>
							<c:if test="${node.joincheck=='1'}">
								<c:if test="${node.userjoin=='1'}">
									<a href="webgroup/join.do?groupId=${node.id}">${node.groupname}&nbsp;<span
										class="glyphicon glyphicon-lock"></span></a>
								</c:if>
								<c:if test="${node.userjoin=='0'}">
								${node.groupname}&nbsp;<span class="glyphicon glyphicon-lock"></span>
								</c:if>
							</c:if>
						</div>
						<div class="doc_node_info">
							${node.usernum}个成员 &nbsp;&nbsp;&nbsp;&nbsp;
							<c:if test="${node.userjoin=='0'}">
								<a class="btn btn-primary btn-xs"
									href="webgroup/join.do?groupId=${node.id}">+加入小组</a>
							</c:if>
						</div>
					</div>
				</div>
			</h3>
		</div>
	</c:forEach>
</div>

