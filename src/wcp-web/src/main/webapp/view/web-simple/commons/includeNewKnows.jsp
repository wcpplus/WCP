<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div class="widget-box shadow-box">
	<div class="title">
		<h3>
			<i class="glyphicon glyphicon-star"></i> 最新知识
		</h3>
	</div>
	<div class="stream-list p-stream">
		<c:forEach items="${newdocs}" var="node">

			<div class="stream-item" id="loop-30">
				<div class="media">
			<a class="pull-right"
				href="webuser/PubHome.do?userid=${node.userid }"
				style="max-width: 200px; text-align: center;" title="${node.title }">
				<img class="img-rounded side_unit_author_img" alt="${node.username}"
				src="<PF:basePath/>${node.photourl }"> <br /> <span
				class="side_unit_info">${node.username}</span>
			</a>
			<div class="media-body">
				<div style="margin-left: 4px;" class="pull-left">
					<div class="doc_node_title_box">
						<a class="doc_node_title" href="webdoc/view/Pub${node.docid}.html">${node.title}
							<c:if test="${node.domtype=='3'}">
								<span class="glyphicon glyphicon-home"></span>
							</c:if> <c:if test="${node.domtype=='4'}">
								<span class="glyphicon glyphicon-bookmark"></span>
							</c:if> <c:if test="${node.domtype=='1'}">
								<span class="glyphicon glyphicon-file"></span>
							</c:if> <c:if test="${node.domtype=='5'}">
								<span style="color: #d9534f;"
									class="glyphicon glyphicon-download-alt"></span>
							</c:if>
						</a>
					</div>
					<c:if test="${node.text!=null&&node.text!=''}">
						<div >
							<p class="doc_node_content">${node.text}</p>
						</div>
					</c:if>
					<div class="side_unit_info side_unit_info_left_box">${node.typename}
						(<i class="glyphicon glyphicon-hand-up"></i>${node.visitnum} <i class="glyphicon glyphicon-comment"></i>${node.answeringnum})${node.pubtime}</div>
				</div>
			</div>
		</div>
			</div>
		</c:forEach>
	</div>
</div>
