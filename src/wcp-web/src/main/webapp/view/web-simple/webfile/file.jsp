<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/view/conf/wda.tld" prefix="WDA"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<PF:basePath/>" />
<title>${file.name}-<PF:ParameterValue key="config.sys.title" /></title>
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
			<div class="row">
				<div class="col-md-4"></div>
			</div>
			<div class="row" style="margin-top: 70px;">
				<div class="col-md-3  visible-lg visible-md"></div>
				<div class="col-md-6">
					<div class="panel panel-default">
						<div class="panel-body">
							<div class="row doc_column_box">
								<div class="col-sm-12">
									<div style="text-align: center;">
										<hr class="hr_split" />
										<span class="column_title">${file.name}</span>
										<hr class="hr_split" />
										<c:if
											test="${fn:toUpperCase(fn:replace(file.exname,'.',''))=='PNG'||fn:toUpperCase(fn:replace(file.exname,'.',''))=='JPG'||fn:toUpperCase(fn:replace(file.exname,'.',''))=='JPEG'||fn:toUpperCase(fn:replace(file.exname,'.',''))=='GIF'}">
											<img class="img-responsive" alt="${file}" src="${file.url}" />
										</c:if>
										<c:if
											test="${fn:toUpperCase(fn:replace(file.exname,'.',''))!='PNG'&&fn:toUpperCase(fn:replace(file.exname,'.',''))!='JPG'&&fn:toUpperCase(fn:replace(file.exname,'.',''))!='JPEG'&&fn:toUpperCase(fn:replace(file.exname,'.',''))!='GIF'}">
											<img style="max-height: 128px; max-width: 128px;"
												alt="${file}"
												src="text/img/fileicon/${fn:toUpperCase(fn:replace(file.exname,'.',''))}.png" />
										</c:if>

									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-12">
									<table class="table table-bordered table-hover"
										style="font-size: 12px;">
										<tr class="downloadButton_wcp">
											<td class="active" style="width: 100px;"><b>操作</b></td>
											<td><a  href="${file.url}">&nbsp;下载</a> <WDA:IsSupport
													fileid="${file.id}">
													<a target="_blank"
														href="<WDA:DocWebViewUrl fileid='${file.id}' docid='none'></WDA:DocWebViewUrl>">&nbsp;预览<img
														alt="" src="text/img/wda.png"></a>
												</WDA:IsSupport></td>
										</tr>
										<tr>
											<td class="active" style="width: 100px;"><b>文件大小</b></td>
											<td>${file.len/1000}kb</td>
										</tr>
										<tr>
											<td class="active" style="width: 100px;"><b>创建时间</b></td>
											<td><PF:FormatTime date="${file.ctime}"
													yyyyMMddHHmmss="yyyy-MM-dd HH:mm:ss" /></td>
										</tr>
										<tr>
											<td class="active" style="width: 100px;"><b>创建人</b></td>
											<td>${file.cusername}</td>
										</tr>
										<tr>
											<td class="active" style="width: 100px;"><b>扩展名</b></td>
											<td>${file.exname}</td>
										</tr>
										<tr>
											<td class="active" style="width: 100px;"><b>ID</b></td>
											<td>${file.id}</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-3  visible-lg visible-md"></div>
			</div>
		</div>
	</div>
	<a name="markdocbottom"></a>
	<jsp:include page="../commons/footServer.jsp"></jsp:include>
	<jsp:include page="../commons/foot.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
			$('img', '#docContentsId').addClass("img-thumbnail");
			if(is_weixin()){
				$('.downloadButton_wcp').hide();
			}
		});
		function is_weixin(){
			var ua = navigator.userAgent.toLowerCase();
			if(ua.match(/MicroMessenger/i)=="micromessenger") {
				return true;
		 	} else {
				return false;
			}
		}
	</script>
</body>
</html>