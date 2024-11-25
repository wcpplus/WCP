<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<c:if test="${VERSIONS != null }">
	<div class="row doc_column_box">
		<div class="col-sm-12">
			<span class="glyphicon glyphicon-dashboard   column_title">历史版本</span>
			<a name="markdocVersions"></a>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12">
			<hr class="hr_split" />
			<div id="DocVersionsBoxId">
				<table class="table table-bordered table-hover"
					style="font-size: 12px;">
					<tr  class="active">
						<td><b>修改日期</b></td>
						<td><b>修改人</b></td>
						<td><b>备注</b></td>
					</tr>
					<c:set var="isShowAllVersion" value="false"></c:set>
					<c:forEach items="${VERSIONS}" var="node" varStatus="status">
						<c:if test="${status.index==0}">
							<tr>
								<td><a href="webdoc/view/Pub${DOCE.doc.id}.html"><PF:FormatTime
											date="${node.etime}" yyyyMMddHHmmss="yyyy-MM-dd HH:mm:ss" />[当前版本]
								</a></td>
								<td>${node.cusername}</td>
								<td>${node.pcontent}</td>
							</tr>
						</c:if>
						<c:if test="${status.index<=3&&status.index>0}">
							<tr>
								<td><a href="webdoc/PubVersion.do?textid=${node.id}"><PF:FormatTime
											date="${node.etime}" yyyyMMddHHmmss="yyyy-MM-dd HH:mm:ss" />
								</a></td>
								<td>${node.cusername}</td>
								<td>${node.pcontent}</td>
							</tr>
						</c:if>
						<c:if test="${status.index>3}">
							<c:set var="isShowAllVersion" value="true"></c:set>
							<tr class="hideVersionTr" style="display: none;">
								<td><a href="webdoc/PubVersion.do?textid=${node.id}"><PF:FormatTime
											date="${node.etime}" yyyyMMddHHmmss="yyyy-MM-dd HH:mm:ss" /></a>
								</td>
								<td>${node.cusername}</td>
								<td>${node.pcontent}</td>
							</tr>
						</c:if>
					</c:forEach>
					<c:if test="${isShowAllVersion}">
						<tr>
							<td colspan="3" style="text-align: center;">
								<button type="button" id="ShowAllVersionId"
									class="btn btn-primary btn-xs">显示全部版本</button>
							</td>
						</tr>
					</c:if>
				</table>
				<script type="text/javascript">
					$(function() {
						$('#ShowAllVersionId').bind('click', function() {
							$('.hideVersionTr').show();
							$('#ShowAllVersionId').hide();
						});
					});
				</script>
			</div>
		</div>
	</div>
</c:if>


