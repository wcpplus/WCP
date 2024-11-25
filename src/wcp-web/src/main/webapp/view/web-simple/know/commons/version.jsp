<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib uri="/view/conf/farmdoc.tld" prefix="DOC"%><div
	class="panel panel-default">
	<div class="panel-body">
		<div class="row">
			<div class="col-md-12">
				<div style="color: #008000; font-size: 12px; font-weight: lighter;">
					作者:<span class="authortagsearch" title="${DOCE.doc.author}"
						style="color: #D9534F; font-size: 14px; font-weight: bold; cursor: pointer; text-decoration: underline;">${DOCE.doc.author}</span>于
					<PF:FormatTime date="${DOCE.doc.pubtime}"
						yyyyMMddHHmmss="yyyy年MM月dd日" />
					<b>发布在分类</b>
					<c:forEach var="node" items="${DOCE.currenttypes}"
						varStatus="status">
					/
					<a href="webtype/view${node.id}/Pub1.html" title="${node.name}">${node.name}</a>
					</c:forEach>
					<b>下,并于</b>
					<PF:FormatTime date="${DOCE.texts.ctime}"
						yyyyMMddHHmmss="yyyy年MM月dd日" />
					<b>编辑</b>
				</div>
				<div class="doc_title">
					<span style="color: #d9534f;"><PF:FormatTime
							date="${DOCE.texts.etime}" yyyyMMddHHmmss="yyyy-MM-dd HH:mm:ss" />版本</span>:
					${DOCE.doc.title}
				</div>
				<jsp:include page="../../know/commons/includeDocVersions.jsp"></jsp:include>
			</div>
		</div>

	</div>
</div>
<div class="panel panel-default">
	<div class="panel-body">
		<div class="row">
			<div class="col-md-12" id="docContentsId">
				<c:if test="${DOCE.imgUrl!=null}">
					<img class="center-block" alt="${DOCE.doc.title}" src="${DOCE.imgUrl}">
				</c:if>
				<div class="table-responsive" style="overflow: auto; border: 0px;">
					<hr />
					${DOCE.texts.text1}
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		//分类
		$('.typetagsearch').bind('click', function() {
			luceneSearch('TYPE:' + $(this).attr('title'));
		});
		//作者
		$('.authortagsearch').bind('click', function() {
			luceneSearch('AUTHOR:' + $(this).attr('title'));
		});
		//标签
		$('.tagsearch').bind('click', function() {
			luceneSearch('TAG:' + $(this).attr('title'));
		});
	});
</script>