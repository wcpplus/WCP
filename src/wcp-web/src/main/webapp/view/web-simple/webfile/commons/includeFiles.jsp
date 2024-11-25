<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib uri="/view/conf/wda.tld" prefix="WDA"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="row">
	<c:forEach var="node" items="${DOCE.files}">
		<div class="col-md-4">
			<div class="panel panel-default">
				<div class="panel-body">
					<div style="height: 128px;">
						<a href="webdoc/view/PubFile${node.id}.html">
							<c:if test="${fn:toUpperCase(fn:replace(node.exname,'.',''))=='PNG'||fn:toUpperCase(fn:replace(node.exname,'.',''))=='JPG'||fn:toUpperCase(fn:replace(node.exname,'.',''))=='JPEG'||fn:toUpperCase(fn:replace(node.exname,'.',''))=='GIF'}">
								<img style="max-height: 128px; max-width: 128px;" alt="${node}"
									src="${node.url}" />
							</c:if> 
							<c:if test="${fn:toUpperCase(fn:replace(node.exname,'.',''))!='PNG'&&fn:toUpperCase(fn:replace(node.exname,'.',''))!='JPG'&&fn:toUpperCase(fn:replace(node.exname,'.',''))!='JPEG'&&fn:toUpperCase(fn:replace(node.exname,'.',''))!='GIF'}">
								<img style="max-height: 128px; max-width: 128px;" alt="${node}"
									src="text/img/fileicon/${fn:toUpperCase(fn:replace(node.exname,'.',''))}.png" />
							</c:if>
						</a>
					</div>${node.len/1000}kb
					<div
							style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
							${node.name}</div>
				</div>
				<div class="panel-footer">
					<div style="overflow: hidden; height: 20px;">
						<a href="webdoc/view/PubFile${node.id}.html">&nbsp;查看</a>
						<a  class="downloadButton_wcp" href="${node.url}">&nbsp;下载</a>
						<WDA:IsSupport fileid="${node.id}">
							<a target="_blank"
								href="<WDA:DocWebViewUrl fileid="${node.id}" docid="${DOCE.doc.id}"></WDA:DocWebViewUrl>">&nbsp;预览<img
								alt="" src="text/img/wda.png"></a>
						</WDA:IsSupport>
					</div>
				</div>
			</div>
		</div>
	</c:forEach>
</div>
<script type="text/javascript">
		$(function() {
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