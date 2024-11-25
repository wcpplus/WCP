<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib uri="/view/conf/farmdoc.tld" prefix="DOC"%>
<div class="row">
	<div class="col-md-12">
		<div style="color: #008000; font-size: 12px; font-weight: lighter;">
			作者:<span class="authortagsearch" title="${DOCE.doc.author}"
				style="color: #D9534F; font-size: 14px; font-weight: bold; cursor: pointer; text-decoration: underline;">${DOCE.doc.author}</span>于
			<PF:FormatTime date="${DOCE.doc.pubtime}"
				yyyyMMddHHmmss="yyyy年MM月dd日" />
			<b>发布在分类</b>
			<c:forEach var="node" items="${DOCE.currenttypes}" varStatus="status">
					/
					<a href="webtype/view${node.id}/Pub1.html" title="${node.name}">${node.name}</a>
			</c:forEach>
			<b>下,并于</b>
			<PF:FormatTime date="${DOCE.texts.ctime}"
				yyyyMMddHHmmss="yyyy年MM月dd日" />
			<b>编辑</b>
		</div>
		<div class="doc_title">${DOCE.doc.title}</div>
		<c:if test="${DOCE.imgUrl!=null }">
			<img src="${DOCE.imgUrl}" alt="内容图"
				class="img-thumbnail center-block" />
		</c:if>
		<c:if test="${DOCE.audit==null }">
			<jsp:include page="../../operation/includeDocOperate.jsp"></jsp:include>
		</c:if>
		<c:if test="${DOCE.audit!=null }">
			<div class="alert alert-warning" role="alert">版本注释：${DOCE.auditTemp.pcontent}</div>
		</c:if>
	</div>
</div>
<div class="row">
	<c:if test="${DOCE.texts.id==DOCE.auditTemp.id||DOCE.auditTemp==null}">
		<div class="col-md-12" id="docContentsId">
			<div class="table-responsive" style="overflow: auto; border: 0px;">
				<hr />
				${DOCE.texts.text1}
			</div>
		</div>
	</c:if>
	<c:if test="${DOCE.texts.id!=DOCE.auditTemp.id&&DOCE.auditTemp!=null}">
		<div class="col-md-6">
			<div class="table-responsive"
				style="overflow: auto; border: 0px; border: 1px solid white;">
				当前版本
				<hr />
				${DOCE.texts.text1}
			</div>
		</div>
		<div class="col-md-6" id="docContentsId">
			<div class="table-responsive"
				style="overflow: auto; border: 0px; border-left: 2px solid red;">
				待审核版本
				<hr />
				${DOCE.auditTemp.text1}
			</div>
		</div>
	</c:if>
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