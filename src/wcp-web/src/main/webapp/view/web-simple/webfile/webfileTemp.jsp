<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<PF:basePath/>" />
<title>${DOCE.doc.title}-<PF:ParameterValue key="config.sys.title" /></title>
<meta name="description"
	content='<PF:ParameterValue key="config.sys.mate.description"/>'>
<meta name="keywords"
	content='<PF:ParameterValue key="config.sys.mate.keywords"/>'>
<meta name="author"
	content='<PF:ParameterValue key="config.sys.mate.author"/>'>
<meta name="robots" content="noindex,nofllow">
<jsp:include page="../atext/include-web.jsp"></jsp:include>
<script charset="utf-8"
	src="<PF:basePath/>text/lib/super-validate/validate.js"></script>
</head>
<body>
	<jsp:include page="../commons/head.jsp"></jsp:include>
	<div class="containerbox">
		<div class="container">
			<div class="row" style="margin-top: 70px;">
				<div class="col-md-3  visible-lg visible-md">
					<!-- 需要特殊显示的文件_开始 -->
					<p style="text-align: center;">
						<c:forEach var="node" items="${fileTypes}">
							<img style="max-height: 128px; max-width: 128px;" alt="${node}"
								src="text/img/fileicon/${node}.png" />
						</c:forEach>
					</p>
					<!-- 需要特殊显示的文件_结束 -->
					<jsp:include page="../know/commons/includeNavigationAudit.jsp"></jsp:include>
				</div>
				<div class="col-md-9">
					<jsp:include page="commons/doc.jsp"></jsp:include><br /> <br /> <br />
					<c:if test="${TYPE=='AUDIT' }">
						<div class="panel panel-default">
							<div class="panel-body">
								<jsp:include page="../know/commons/includeAuditForm.jsp"></jsp:include>
							</div>
						</div>
					</c:if>
				</div>
			</div>
		</div>
	</div>
	<a name="markdocbottom"></a>
	<jsp:include page="../commons/footServer.jsp"></jsp:include>
	<jsp:include page="../commons/foot.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
			$('img', '#docContentsId').addClass("img-thumbnail");
		});
	</script>
</body>
</html>