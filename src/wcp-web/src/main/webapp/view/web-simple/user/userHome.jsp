<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<PF:basePath/>" />
<title>${USEROBJ.name}-<PF:ParameterValue key="config.sys.title" /></title>
<meta name="description"
	content='<PF:ParameterValue key="config.sys.mate.description"/>'>
<meta name="keywords"
	content='<PF:ParameterValue key="config.sys.mate.keywords"/>'>
<meta name="author"
	content='<PF:ParameterValue key="config.sys.mate.author"/>'>
<meta name="robots" content="noindex,nofllow">
<jsp:include page="../atext/include-web.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="../commons/head.jsp"></jsp:include>
	<jsp:include page="../commons/superContent.jsp"></jsp:include>
	<div class="containerbox">
		<div class="container ">
			<div class="row  column_box">
				<div class="col-md-12">
					<jsp:include page="../statis/commons/includeMyStatis.jsp"></jsp:include>
				</div>
			</div>
			<!-- <div class="row">
				<div class="col-sm-12 putServerBox"
					style="border-left: 0px solid #ccc;">
					<ul style="float: left;">
						<PF:IfParameterEquals key="config.sys.opensource" val="false">
							<li style='width: 100px; height: 60px;'><a
								style="text-align: center;"
								href="resume/view.do?userid=${userid}"><img
									src="text/img/userinfo.png" alt="个人档案" class="img-thumbnail"
									style="width: 48px; height: 48px;" /></a></li>
						</PF:IfParameterEquals>
					</ul>
				</div>
			</div> -->
			<div class="row ">
				<div class="col-md-12">
					<div class="panel panel-default "
						style="background-color: #FCFCFA;">
						<div class="panel-body">
							<p>
								<jsp:include page="../operation/includeUserOperate.jsp"></jsp:include>
							</p>
							<div class="table-responsive">
								<hr style="margin: 8px;" />
								<c:if test="${type=='know'}">
									<jsp:include page="commons/includeUserKnows.jsp"></jsp:include>
								</c:if>
								<c:if test="${type=='file'}">
									<jsp:include page="commons/includeUserKnows.jsp"></jsp:include>
								</c:if>
								<c:if test="${type=='joy'}">
									<jsp:include page="commons/includeUserKnows.jsp"></jsp:include>
								</c:if>
								<c:if test="${type=='group'}">
									<jsp:include page="commons/includeUserGroups.jsp"></jsp:include>
								</c:if>
								<c:if test="${type=='audit'}">
									<jsp:include page="commons/includeUserMyAudit.jsp"></jsp:include>
								</c:if>
								<c:if test="${type=='audith'}">
									<jsp:include page="commons/includeUserMyAuditH.jsp"></jsp:include>
								</c:if>
								<c:if test="${type=='audito'}">
									<jsp:include page="commons/includeUserMyAuditO.jsp"></jsp:include>
								</c:if>
								<c:if test="${type=='usermessage'}">
									<jsp:include page="commons/includeUserMessage.jsp"></jsp:include>
								</c:if>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../commons/footServer.jsp"></jsp:include>
	<jsp:include page="../commons/foot.jsp"></jsp:include>
</body>
<script type="text/javascript">
	function knowBoxGoPage(page) {
		window.location = basePath
				+ "webuser/PubHome.do?userid=${id}&query.currentPage=" + page;
	}
</script>
</html>