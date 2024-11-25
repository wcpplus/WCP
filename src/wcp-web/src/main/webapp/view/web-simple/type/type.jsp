<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<PF:basePath/>" />
<title><c:if test="${type!=null}">${type.name}</c:if><c:if test="${type==null}">分类</c:if>-<PF:ParameterValue
		key="config.sys.title" /></title>
<meta name="description"
	content='<PF:ParameterValue key="config.sys.mate.description"/>'>
<meta name="keywords"
	content='<PF:ParameterValue key="config.sys.mate.keywords"/>'>
<meta name="author"
	content='<PF:ParameterValue key="config.sys.mate.author"/>'>
<meta name="robots" content="index,follow">
<jsp:include page="../atext/include-web.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="../commons/head.jsp"></jsp:include>
	<jsp:include page="../commons/superContent.jsp"></jsp:include>
	<div class="containerbox">
		<div class="container ">
			<div class="row">
				<div class="col-md-3 hidden-xs">
					<jsp:include page="commons/includePubType.jsp"></jsp:include>
				</div>
				<div class="col-md-9">
					<div class="row column_box">
						<div class="col-sm-12">
							<c:if test="${type!=null}">
								<span class="glyphicon glyphicon-fire  column_title">当前分类:<c:forEach
										items="${typepath}" var="node">/<a style="color: #d9534f"
											href="webtype/view${node.id}/Pub1.html" class="active">${node.name}</a></c:forEach></span>
							</c:if>
							<c:if test="${type==null}">
								<span class="glyphicon glyphicon-fire  column_title">分类</span>
							</c:if>
						</div>
					</div>
					<div style="margin-top: 16px;"></div>
					<c:if test="${type!=null}"><jsp:include
							page="../operation/includeCreatOperate.jsp"></jsp:include>
					</c:if>
					<div class="panel panel-default">
						<div class="panel-body">
							<jsp:include page="commons/includeTypeDocs.jsp"></jsp:include></div>
					</div>

				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../commons/footServer.jsp"></jsp:include>
	<jsp:include page="../commons/foot.jsp"></jsp:include>
</body>
</html>