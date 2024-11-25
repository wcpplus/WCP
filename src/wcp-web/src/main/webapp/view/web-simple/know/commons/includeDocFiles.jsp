<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:if test="${fn:length(DOCE.files)>0}">
<div class="row doc_column_box" >
	<div class="col-sm-12">
		<span
			class="glyphicon glyphicon-paperclip column_title">附件</span>
			<a name="markdocFile"></a>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<hr class="hr_split" />
		<c:set var="isHaveImg" value="false"></c:set>
		<c:set var="isHaveFile" value="false"></c:set>
		<c:set var="fileNum" value="0"></c:set>
		<c:forEach var="node" items="${DOCE.files}" varStatus="status">
			<c:set var="isHaveFile" value="true"></c:set>
			<c:set var="exname"
				value="${fn:toUpperCase(fn:replace(node.exname,'.',''))}"></c:set>
			<c:if
				test="${exname=='PNG'||exname=='JPG'||exname=='JPEG'||exname=='GIF'}">
				<c:set var="isHaveImg" value="true"></c:set>
			</c:if>
			<c:set var="fileNum" value="${fileNum+1}"></c:set>
		</c:forEach>
		<!-- 图片显示_开始 -->
		<c:if test="${isHaveImg}">
			<div class="panel panel-default">
				<div class="panel-body"
					style="background-color: #666666; height: 230px;">
					<div id="carousel-example-generic" class="carousel slide"
						style="vertical-align: middle;" data-ride="carousel">
						<div class="carousel-inner" role="listbox"
							style="height: 200px; vertical-align: middle;">
							<c:set var="active" value="active"></c:set>
							<c:forEach var="node" items="${DOCE.files}" varStatus="status">
								<c:set var="exname"
									value="${fn:toUpperCase(fn:replace(node.exname,'.',''))}"></c:set>
								<c:if
									test="${exname=='PNG'||exname=='JPG'||exname=='JPEG'||exname=='GIF'}">
									<div class="item ${active}">
										<div style="text-align: center;">
											<img style="max-height: 160px;" src="${node.url}"
												alt="${node.name}">
										</div>
										<div style="text-align: center;">
											<a href="${node.url}" style="color: white;" > ${node.name}</a>
										</div>
									</div>
									<c:set var="active" value=""></c:set>
								</c:if>
							</c:forEach>
						</div>
						<!-- Controls -->
						<a class="left carousel-control" href="#carousel-example-generic"
							role="button" data-slide="prev"> <span
							class="glyphicon glyphicon-chevron-left"></span> <span
							class="sr-only">Previous</span>
						</a> <a class="right carousel-control"
							href="#carousel-example-generic" role="button" data-slide="next">
							<span class="glyphicon glyphicon-chevron-right"></span> <span
							class="sr-only">Next</span>
						</a>
					</div>
				</div>
			</div>
		</c:if>
		<!-- 图片显示_结束 -->
		<!-- 需要特殊显示的文件_开始 -->
		<p style="text-align: center;">
			<c:forEach var="node" items="${FILETYPES}">
				<img style="max-height: 64px; max-width: 64px;" alt="${node}"
					src="<PF:basePath/>text/img/fileicon/${node}.png" />
			</c:forEach>
		</p>
		<!-- 需要特殊显示的文件_结束 -->
		<!-- 详细附件列表_开始 -->
		<c:if test="${isHaveFile}">
			<p style="text-align: center;">
				<button type="button" id="ShowAllDocFileId"
					class="btn btn-primary btn-xs">显示附件列表(${fileNum})</button>
			</p>
			<div id="docFilesListPId" style="display: none;">
				<c:set var="etime" value="0"></c:set>
				<c:forEach var="node" items="${DOCE.files}">
					<c:if test="${etime!=node.etime}">
						<h4 style="color: green;">
							<PF:FormatTime date="${node.etime}"
								yyyyMMddHHmmss="yyyy年MM月dd日 HH:mm:ss" />
							[版本]
						</h4>
					</c:if>
					<c:set var="etime" value="${node.etime}"></c:set>
					<p>
						<a href="${node.url}"><span
							class="glyphicon glyphicon-paperclip"></span> ${node.name}</a>(${node.len}b)
					</p>
				</c:forEach>
			</div>
			<script type="text/javascript">
				$(function() {
					$('#ShowAllDocFileId').bind('click', function() {
						$('#docFilesListPId').show();
						$('#ShowAllDocFileId').hide();
					});
				});
			</script>
		</c:if>
	</div>
</div>
<!-- 详细附件列表_结束 -->
</c:if>