<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fun"%>
<!-- DOCID,title,DOCDESCRIBE,AUTHOR,PUBTIME,TAGKEY
	 *         ,IMGID,VISITNUM,PRAISEYES,PRAISENO,HOTNUM,EVALUATE,ANSWERINGNUM,
	 *         TYPENAME -->
<div class="row">
	<div class="col-sm-12">
		<c:forEach items="${docs.resultList}" var="node">
			<div class="col-sm-12">
				<hr class="hr_split" />
			</div>
			<div class="col-sm-12 doc_node">
				<div class="media">
					<a class="pull-right"
						href="webuser/PubHome.do?userid=${node.USERID }"
						style="max-width: 200px; text-align: center;"
						title="${node.TITLE }"> <img
						class="img-rounded side_unit_author_img" alt="${node.USERNAME}"
						src="<PF:basePath/>${node.PHOTOURL }"> <br /> <span
						class="side_unit_info">${node.USERNAME}</span>
					</a>
					<div class="media-body">
						<div style="margin-left: 4px;" class="pull-left">
							<div class="doc_node_title_box">
								<a class="doc_node_title_min"
									href="webdoc/view/Pub${node.DOCID}.html">${node.TITLE}<c:if
										test="${node.DOMTYPE=='3'}">
										<span class="glyphicon glyphicon-home"></span>
									</c:if> <c:if test="${node.DOMTYPE=='1'}">
										<span class="glyphicon glyphicon-file"></span>
									</c:if>
									<c:if test="${node.DOMTYPE=='4'}">
										<span class="glyphicon glyphicon-bookmark"></span>
									</c:if> <c:if test="${node.DOMTYPE=='5'}">
										<span style="color: #d9534f;"
											class="glyphicon glyphicon-download-alt"></span>
									</c:if></a>
							</div>
							<div class="doc_node_content_box">
								<p class="doc_node_content">${node.DOCDESCRIBE}</p>
							</div>
							<div class="side_unit_info side_unit_info_left_box">
								<a href="webtype/view${node.TYPEID}/Pub1.html">${node.TYPENAME}</a>
								阅读(${node.VISITNUM}) 评论(${node.ANSWERINGNUM})${node.PUBTIME}
							</div>
						</div>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
</div>
<c:if test="${fun:length(docs.resultList) > 0}">
	<div class="row">
		<div class="col-xs-12">
			<ul class="pagination pagination-sm">
				<c:forEach var="page" begin="${docs.startPage}"
					end="${docs.endPage}" step="1">
					<c:if test="${page==docs.currentPage}">
						<li class="active"><a>${page} </a></li>
					</c:if>
					<c:if test="${page!=docs.currentPage}">
						<li><a href="webtype/view${typeid}/Pub${page}.html?pagenum=">${page}</a></li>
					</c:if>
				</c:forEach>
			</ul>
		</div>
	</div>
</c:if>