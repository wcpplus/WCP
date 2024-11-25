<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div class="widget-box shadow-box">
	<div class="title">
		<h3>
			<i class="glyphicon glyphicon-star"></i>推荐阅读
		</h3>
	</div>
	<div class="stream-list p-stream">
		<c:forEach var="topDoc" items="${topDocList}">
			<div class="stream-item" id="loop-30">
				<div class="media">
					<c:if test="${topDoc.imgurl!=null}">
						<a class="pull-right " href="webdoc/view/Pub${topDoc.docid}.html"
							style="max-height: 100px; max-width: 250px;"><img alt="${topDoc.title}"
							class="doc_max_img" src="${topDoc.imgurl}"><br> <span
							class="side_unit_info"></span> </a>
					</c:if>
					<div class="media-body">
						<a class="doc_node_max_title" style="font-size: 16px;"
							href="webdoc/view/Pub${topDoc.docid}.html">${topDoc.title}</a>
						<div class="doc_node_info">阅读(${topDoc.visitnum }) |
							评论(${topDoc.answeringnum })</div>
						<div class="doc_node_content">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							${topDoc.text } &nbsp;</div>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
</div>