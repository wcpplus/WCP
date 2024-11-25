<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%><%@taglib
	uri="http://java.sun.com/jsp/jstl/functions" prefix="fun"%>
<div class="row column_box">
	<div class="col-sm-12"><jsp:include
			page="../../operation/includeGroupOperate.jsp"></jsp:include>
		<span class="glyphicon glyphicon-lock  column_title">小组 </span>
	</div>
</div>
<div style="margin-top: 6px;"></div>
<div class="row ">
	<c:forEach var="node" items="${groups.resultList}">
		<div class="col-md-3">
			<div class="panel panel-default">
				<div class="panel-body">
					<div class="media">
						<a class="pull-right" href="webgroup/join.do?groupId=${node.ID}"><img
							alt="${node.GROUPNAME}" class="img-rounded side_unit_group_img"
							src="${node.IMGURL}"></a>
						<div class="media-body">
							<div style="margin-left: 4px;" class="pull-left">
								<div class="doc_node_title_box">
									<c:if test="${node.JOINCHECK=='1'}">
										<a href="webgroup/join.do?groupId=${node.ID}">${node.GROUPNAME}&nbsp;<span
											class="glyphicon glyphicon-lock"></span></a>
									</c:if>
									<c:if test="${node.JOINCHECK=='0'}">
										<a href="webgroup/Pubshow.do?groupid=${node.ID}">
											${node.GROUPNAME}</a>
									</c:if>
								</div>
								<div class="side_unit_info side_unit_info_left_box">
									成员人数(${node.USERNUM})<br />
									<PF:FormatTime date="${node.CTIME}" yyyyMMddHHmmss="yyyy-MM-dd" />
								</div>
							</div>
						</div>
						<div style="height: 48px;overflow:hidden;font-size: 14px; ">
							<hr style="margin: 4px;" />
							${node.GROUPNOTE}
						</div>
					</div>
				</div>
			</div>
		</div>
	</c:forEach>
</div>
<c:if test="${fun:length(groups.resultList) > 0}">
	<div class="row">
		<div class="col-xs-12">
			<ul class="pagination pagination-sm">
				<c:forEach var="page" begin="${groups.startPage}"
					end="${groups.endPage}" step="1">
					<c:if test="${page==groups.currentPage}">
						<li class="active"><a>${page} </a></li>
					</c:if>
					<c:if test="${page!=groups.currentPage}">
						<li><a href="webgroup/PubHome.html.do?pagenum=${page}">${page}</a></li>
					</c:if>
				</c:forEach>
			</ul>
		</div>
	</div>
</c:if>