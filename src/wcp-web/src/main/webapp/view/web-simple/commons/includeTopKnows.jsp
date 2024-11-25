<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!-- ID,TITLE,DOCDESCRIBE,AUTHOR,PUBTIME,TAGKEY ,IMGID,VISITNUM,PRAISEYES,PRAISENO,HOTNUM,TYPENAME -->
<div class="widget-box shadow-box">
	<div class="title">
		<h3>
			<i class="glyphicon glyphicon-star"></i>推荐阅读
		</h3>
	</div>
	<div class="stream-list p-stream">
		<div class="stream-item" id="loop-30">
			<div class="row">
				<c:forEach var="topDoc" items="${topDocList}">
					<div class="col-md-6" style="min-height: 350px;">
						<div class="doc_node_max_title" style="max-height: 50px;overflow: hidden;">
							<a class="doc_node_max_title" style="font-size: 28px;"
								href="webdoc/view/Pub${topDoc.docid}.html">${topDoc.title}</a>
						</div>
						<c:if test="${topDoc.imgurl!=null}">
							<div class="doc_max_img_box">
								<img class="doc_max_img" src="${topDoc.imgurl}" alt="${topDoc.title}">
							</div>
							<br />
						</c:if>
						<div class="doc_node_info">阅读(${topDoc.visitnum }) |
							评论(${topDoc.answeringnum })</div>
						<div class="doc_node_content">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							${topDoc.text } &nbsp;</div>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
</div>


