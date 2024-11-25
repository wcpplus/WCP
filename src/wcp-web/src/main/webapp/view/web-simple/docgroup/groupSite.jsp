<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<PF:basePath/>" />
<title>${docgroup.groupname}小组首页-<PF:ParameterValue
		key="config.sys.title" />
</title>
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
			<div class="row column_box">
				<div class="col-md-3  hidden-xs">
					<jsp:include page="commons/includeGroupLeft.jsp"></jsp:include>
				</div>
				<div class="col-md-9">
					<div class="panel panel-default">
						<div class="panel-body">
							<h3 style="text-align: center; font-weight: bold;">
								<c:if test="${docgroup.joincheck=='1'}">
									<span class="glyphicon glyphicon-lock">&nbsp;${docgroup.groupname}&nbsp;</span>
								</c:if>
								<c:if test="${docgroup.joincheck=='0'}">
								${docgroup.groupname}
							</c:if>
							</h3>
							<div class="bg-success">
								<small>小组成立于</small> <strong> <u> <PF:FormatTime
											date="${docgroup.ctime}" yyyyMMddHHmmss="yyyy-MM-dd HH:mm" /></u>
								</strong> <small>,有成员</small> <strong> <u>${docgroup.usernum}</u>
								</strong> <small>人,共发布文档</small> <strong> <u>${docnum}</u>
								</strong> <small>篇</small>
							</div>
							<jsp:include page="../operation/includeCreatOperate.jsp"></jsp:include>
							<div style="float: right;"><jsp:include
									page="../operation/includeGroupSiteOperate.jsp"></jsp:include></div>
							<div style="clear: both; margin: 4px;"></div>
							${home.texts.text1}
						</div>
					</div>
					<div class="panel panel-default">
						<div class="panel-body">
							<jsp:include page="commons/includeGroupSiteDocs.jsp"></jsp:include>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- /.modal -->
	<jsp:include page="../commons/footServer.jsp"></jsp:include>
	<jsp:include page="../commons/foot.jsp"></jsp:include>
</body>
</html>